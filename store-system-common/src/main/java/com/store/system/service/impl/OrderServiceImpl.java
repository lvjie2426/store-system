package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.Uninterruptibles;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.lock.ZkLock;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.model.pagination.PagerSession;
import com.quakoo.baseFramework.model.pagination.service.PagerRequestService;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.quakoo.ext.RowMapperHelp;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.store.system.bean.*;
import com.store.system.client.ClientAfterSaleDetail;
import com.store.system.client.ClientOrder;
import com.store.system.client.ResultClient;
import com.store.system.dao.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.*;
import com.store.system.service.ext.OrderPayService;
import com.store.system.service.ext.OrderRefundService;
import com.store.system.util.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl implements OrderService, InitializingBean {

    private PropertyUtil propertyUtil = PropertyUtil.getInstance("pay.properties");
    private SimpleDateFormat gmtFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    private TransformMapUtils skuMapUtils = new TransformMapUtils(ProductSKU.class);
    private TransformMapUtils spuMapUtils = new TransformMapUtils(ProductSPU.class);

    @Resource
    private PayPassportDao payPassportDao;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private OrderDao orderDao;
    @Resource
    private ProductBrandDao productBrandDao;
    @Resource
    private ProductSeriesDao productSeriesDao;

    @Resource
    private ProductSKUDao productSKUDao;
    @Resource
    private ProductSPUDao productSPUDao;
    @Resource
    private UserDao userDao;
    @Resource
    private UserGradeDao userGradeDao;
    @Resource
    private UserGradeCategoryDiscountDao userGradeCategoryDiscountDao;
    @Resource
    private SubordinateDao subordinateDao;
    @Resource
    private InventoryDetailDao inventoryDetailDao;
    @Resource
    private MarketingCouponDao marketingCouponDao;
    @Resource
    private OptometryInfoDao optometryInfoDao;
    @Resource
    private AfterSaleDetailDao afterSaleDetailDao;
    @Resource
    private AfterSaleDetailService afterSaleDetailService;
    @Resource
    private CommissionDao commissionDao;
    @Autowired(required = false)
    private OrderRefundService orderRefundService;
    @Resource
    private RefundOrderDao refundOrderDao;
    @Resource
    private OrderNotifyDao orderNotifyDao;
    @Resource
    private WalletService walletService;
    @Resource
    private PayInfoService payInfoService;
    @Resource
    private BusinessOrderService businessOrderService;

    @Autowired(required = false)
    private OrderPayService orderPayService;


    private String lockZkAddress;
    private String projectName;
    private String wxpayBarcodeUrl = "https://api.mch.weixin.qq.com/pay/micropay";
    private String wxpayQueryUrl = "https://api.mch.weixin.qq.com/pay/orderquery";
    private String wxpayReverseUrl = "https://api.mch.weixin.qq.com/secapi/pay/reverse";
    private String wxpayPrePayUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    private String wxpayRefundUrl = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    private String alipayGateway = "https://openapi.alipay.com/gateway.do?";

    private RowMapperHelp<Order> rowMapper = new RowMapperHelp<>(Order.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        this.projectName = propertyUtil.getProperty("project.name");
        this.lockZkAddress = propertyUtil.getProperty("pay.lock.zk.address");
    }

    private ResultClient createAliOrder(int payMode, int type, String typeInfo, String title, String desc,
                                        int price, OrderExpireUnit expireUnit, int expireNum, long subId) throws Exception {
        if (price == 0) throw new StoreSystemException("price is zero!");
        if (payMode != Order.pay_mode_barcode && (expireUnit.getId() == 0 || expireNum == 0))
            throw new StoreSystemException("expire is error!");
        List<PayPassport> payPassports = payPassportDao.getAllList(subId, PayPassport.status_on);
        PayPassport payPassport = null;
        if (payPassports.size() > 0) {
            payPassport = payPassports.get(0);
        }
        if (null == payPassport) return new ResultClient(false, null, "门店暂未设置支付方式,请联系管理员！");
        long gmt = NumberUtils.toLong(gmtFormat.format(new Date()));

        Order order = new Order();
        order.setGmt(gmt);
        order.setPassportId(payPassport.getId());
        order.setPayMode(payMode);
        order.setType(type);
        order.setPayType(Order.pay_type_ali);
        order.setTypeInfo(typeInfo);
        order.setTitle(title);
        order.setDesc(desc);
        order.setPrice(price);
        order.setStatus(Order.status_no_pay);
        if (payMode != Order.pay_mode_barcode) {
            order.setExpireUnitId(expireUnit.getId());
            order.setExpireNum(expireNum);
            long expireTime = PayUtils.getExpireTime(System.currentTimeMillis(), expireUnit, expireNum);
            order.setExpireTime(expireTime);
        }
        return new ResultClient(orderDao.insert(order));
    }

    @Override
    public ResultClient handleAliBarcodeOrder(String authCode, int type, String title, String desc,
                                              int price, long subId, long boId) throws Exception {
        BusinessOrder businessOrder = businessOrderService.load(boId);
        Map<String, Object> info = Maps.newHashMap();
        info.put("uid", String.valueOf(businessOrder.getUid()));
        info.put("payType", type);
        info.put("money", price);
        info.put("boId", businessOrder.getId());
        info.put("payModel", Order.pay_mode_barcode);
        ResultClient resultClient = createAliOrder(Order.pay_mode_barcode, type, JsonUtils.toJson(info), title,
                desc, price, OrderExpireUnit.nvl, 0, subId);
        Order order = (Order) resultClient.getData();
        Map<String, String> sParaTemp = this.aliOrderPayParam(order.getId(), Order.pay_mode_barcode, authCode);
        List<String> keys = new ArrayList<String>(sParaTemp.keySet());
        Collections.sort(keys);
        String urlParamStr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = sParaTemp.get(key);
            value = URLEncoder.encode(value, Constant.defaultCharset);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                urlParamStr += key + "=" + value;
            } else {
                urlParamStr += key + "=" + value + "&";
            }
        }
        String url = alipayGateway + urlParamStr;
        String repStr = PayUtils.send(url);
        order.setDetail(repStr);
        Map<String, String> resMap = (Map<String, String>) JsonUtils.fromJson(repStr, Map.class)
                .get("alipay_trade_pay_response");
        String code = resMap.get("code");
        boolean res = false;
        if (code.equals("10000")) {
            res = true;
            order.setOrderNo(resMap.get("trade_no"));
            order.setStatus(Order.status_pay);
            order.setAuthCode(authCode);
            long pay_time = 0;
            String paymentStr = resMap.get("gmt_payment"); //交易付款时间
            if (StringUtils.isNotBlank(paymentStr))
                pay_time = DateUtils.parseDate(paymentStr, "yyyy-MM-dd HH:mm:ss").getTime();
            order.setPayTime(pay_time);
        }
        ResultClient result = new ResultClient();
        if (order.getStatus() == Order.status_pay) {
            result = orderPayService.successHandleBusiness(order, boId);
        }
        orderDao.update(order);
        if (res) {
            return result;
        } else {
            return new ResultClient(false);
        }
    }

    @Override
    public boolean aliOrderNotify(Map<String, String> params) throws Exception {
        String out_trade_no = PayUtils.getParam(params, "out_trade_no"); // 商户订单号
        long orderId = PayUtils.getOrderId(out_trade_no);
        Order order = orderDao.load(orderId);
        if (null == order) throw new StoreSystemException("order is null!");
        long passportId = order.getPassportId();
        PayPassport payPassport = payPassportDao.load(passportId);
        if (null == payPassport) throw new StoreSystemException("passport is null!");
        String aliPublicKey = payPassport.getAliPublicKey();
        if (StringUtils.isBlank(aliPublicKey)) throw new StoreSystemException("aliPublicKey is null!");

        String sign = "";
        if (params.get("sign") != null) {
            sign = params.get("sign");
        }
        params.remove("sign");
        params.remove("sign_type");
        StringBuffer checkContent = new StringBuffer();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            checkContent.append((i == 0 ? "" : "&") + key + "=" + value);
        }
        boolean verify = RSA.verify256(checkContent.toString(), sign, aliPublicKey, Constant.defaultCharset);
        if (!verify) return false;
        ZkLock lock = null;
        try {
            lock = ZkLock.getAndLock(lockZkAddress, projectName, projectName + "_order_" + orderId + "_lock",
                    true, 5000, 10000);
            String trade_no = PayUtils.getParam(params, "trade_no"); // 支付宝交易号
            long notify_time = DateUtils.parseDate(PayUtils.getParam(params, "notify_time"),
                    "yyyy-MM-dd HH:mm:ss").getTime(); // 通知时间
            String trade_status = PayUtils.getParam(params, "trade_status"); // 交易状态
            long pay_time = 0;
            String paymentStr = PayUtils.getParam(params, "gmt_payment"); //交易付款时间
            if (StringUtils.isNotBlank(paymentStr))
                pay_time = DateUtils.parseDate(paymentStr, "yyyy-MM-dd HH:mm:ss").getTime();
            String detail = JsonUtils.toJson(params);
            OrderNotify orderNotify = new OrderNotify();
            orderNotify.setOid(orderId);
            orderNotify.setNotifyTime(notify_time);
            orderNotify.setDetail(detail);
            orderNotifyDao.insert(orderNotify);
            int status = order.getStatus();
            if (trade_status.equals("TRADE_SUCCESS") && status == Order.status_no_pay) {
                order.setStatus(Order.status_pay);
                order.setOrderNo(trade_no);
                order.setPayTime(pay_time);
                orderPayService.successHandleBusiness(order, 0);
                orderDao.update(order);
            }
            return true;
        } finally {
            if (null != lock) lock.release();
        }
    }

    private Map<String, String> aliOrderPayParam(long oid, int payMode, String authCode) throws Exception {
        if (payMode == Order.pay_mode_barcode && StringUtils.isBlank(authCode))
            throw new StoreSystemException("authCode is null!");
        Order order = orderDao.load(oid);
        if (null == order) throw new StoreSystemException("order is null!");
        if (order.getPayType() != Order.pay_type_ali) throw new StoreSystemException("order is not ali!");
        if (order.getPayMode() != payMode) throw new StoreSystemException("order pay mode is error!");
        if (order.getStatus() != Order.status_no_pay) throw new StoreSystemException("order status is error!");
        Date now = new Date();
        if (payMode != Order.pay_mode_barcode && now.getTime() > order.getExpireTime())
            throw new StoreSystemException("order is expire!");
        long passportId = order.getPassportId();
        PayPassport payPassport = payPassportDao.load(passportId);
        if (null == payPassport) throw new StoreSystemException("passport is null!");
        String aliAppid = payPassport.getAliAppid();
        String aliPrivateKey = payPassport.getAliPrivateKey();
        String aliOrderNotifyUrl = payPassport.getAliOrderNotifyUrl();
        if (StringUtils.isBlank(aliAppid)) throw new StoreSystemException("aliAppid is null!");
        if (payMode != Order.pay_mode_barcode && StringUtils.isBlank(aliOrderNotifyUrl))
            throw new StoreSystemException("aliOrderNotifyUrl is null!");
        if (StringUtils.isBlank(aliPrivateKey)) throw new StoreSystemException("aliPrivateKey is null!");
        String itBPay = null;
        if (payMode != Order.pay_mode_barcode) {
            OrderExpireUnit expireUnit = OrderExpireUnit.get(order.getExpireUnitId());
            if (expireUnit == OrderExpireUnit.inday) itBPay = expireUnit.getSign();
            else itBPay = order.getExpireNum() + expireUnit.getSign();
        }
        String method = null;
        String product_code = null;
        if (payMode == Order.pay_mode_app) {
            product_code = "QUICK_MSECURITY_PAY";
            method = "alipay.trade.app.pay";
        } else if (payMode == Order.pay_mode_wap) {
            product_code = "QUICK_WAP_WAY";
            method = "alipay.trade.wap.pay";
        } else if (payMode == Order.pay_mode_barcode) {
            product_code = "FACE_TO_FACE_PAYMENT";
            method = "alipay.trade.pay";
        }
        Map<String, String> bizContentMap = Maps.newLinkedHashMap();
        bizContentMap.put("body", StringUtils.isNotBlank(order.getDesc()) ? order.getDesc() : "");
        bizContentMap.put("subject", StringUtils.isNotBlank(order.getTitle()) ? order.getTitle() : "");
        bizContentMap.put("out_trade_no", PayUtils.getOutTradeNo(oid, order.getGmt()));
        if (payMode == Order.pay_mode_barcode) bizContentMap.put("scene", "bar_code");
        if (payMode == Order.pay_mode_barcode) bizContentMap.put("auth_code", authCode);
        if (StringUtils.isNotBlank(itBPay)) bizContentMap.put("timeout_express", String.valueOf(itBPay));
        double price = ArithUtils.div(order.getPrice(), 100d, 2);
        bizContentMap.put("total_amount", String.valueOf(price));
        bizContentMap.put("product_code", product_code);
        String biz_content = JsonUtils.toJson(bizContentMap);
        Map<String, String> sParaTemp = Maps.newHashMap();
        sParaTemp.put("app_id", aliAppid); //
        sParaTemp.put("method", method);
        sParaTemp.put("charset", Constant.defaultCharset);
        sParaTemp.put("sign_type", "RSA2");
        sParaTemp.put("timestamp", DateUtils.simpleDateFormat(now, "yyyy-MM-dd HH:mm:ss"));
        sParaTemp.put("version", "1.0");
        if (payMode != Order.pay_mode_barcode) sParaTemp.put("notify_url", aliOrderNotifyUrl);
        if (payMode == Order.pay_mode_wap && StringUtils.isNotBlank(payPassport.getAliWapOrderReturnUrl())) {
            sParaTemp.put("return_url", payPassport.getAliWapOrderReturnUrl());
        }
        sParaTemp.put("biz_content", biz_content);
        String prestr = PayUtils.createLinkString(sParaTemp);
        String mysign = RSA.sign256(prestr, aliPrivateKey, Constant.defaultCharset);
        sParaTemp.put("sign", mysign);
        return sParaTemp;
    }

    private ResultClient createWxOrder(int payMode, int type, String typeInfo, String title, String desc,
                                       int price, OrderExpireUnit expireUnit, int expireNum, long subId) throws Exception {
        if (price == 0) throw new StoreSystemException("price is zero!");
        if (payMode != Order.pay_mode_barcode && (expireUnit.getId() == 0 || expireNum == 0))
            throw new StoreSystemException("expire is error!");
        List<PayPassport> payPassports = payPassportDao.getAllList(subId, PayPassport.status_on);
        PayPassport payPassport = null;
        if (payPassports.size() > 0) {
            payPassport = payPassports.get(0);
        }
        if (null == payPassport) return new ResultClient(false, null, "门店暂未设置支付方式,请联系管理员！");
        long gmt = NumberUtils.toLong(gmtFormat.format(new Date()));
        Order order = new Order();
        order.setPayMode(payMode);
        order.setGmt(gmt);
        order.setPassportId(payPassport.getId());
        order.setPrice(price);
        order.setType(type);
        order.setStatus(Order.status_no_pay);
        order.setPayType(Order.pay_type_wx);
        order.setTypeInfo(typeInfo);
        order.setTitle(title);
        order.setDesc(desc);
        if (payMode != Order.pay_mode_barcode) {
            order.setExpireUnitId(expireUnit.getId());
            order.setExpireNum(expireNum);
            long expireTime = PayUtils.getExpireTime(System.currentTimeMillis(), expireUnit, expireNum);
            order.setExpireTime(expireTime);
        }
        return new ResultClient(orderDao.insert(order));
    }

    private boolean validSign(Map<String, String> params, String wxpayApiKey) throws Exception {
        String return_code = params.get("return_code");// 通信标识
        String result_code = params.get("result_code");// 业务结果
        String return_msg = params.get("return_msg");// 业务结果
        if ("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)) {// 通信成功
            String mysign = PayUtils.buildSign(params, wxpayApiKey);
            String responseSign = params.get("sign");
            if (Objects.equals(mysign, responseSign)) {// 签名校验成功
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> map = Maps.newHashMap();

//        map.put("is_subscribe","N");
//        map.put("appid","wx2feae2c615187670");
//        map.put("fee_type","CNY");
//        map.put("nonce_str","iKk3LCvZqyLBdz81");
//        map.put("out_trade_no","20180806191345303");
//        map.put("transaction_id","4200000157201808064295382872");
//        map.put("trade_type","APP");
//        map.put("result_code","SUCCESS");
//        map.put("sign","20FE167E58E1ED17FBF86FC4A0E46E7D");
//        map.put("mch_id","1510090681");
//        map.put("total_fee","1");
//        map.put("time_end","20180806191353");
//        map.put("openid","oHY611AbDOk9Zw2iZVZZsQ7Zh5vk");
//        map.put("bank_type","CFT");
//        map.put("return_code","SUCCESS");
//        map.put("cash_fee","1");

        map.put("result_code", "SUCCESS");
        map.put("return_msg", "OK");
        map.put("prepay_id", "wx0619175547311987327fb4573768020563");
        map.put("return_code", "SUCCESS");
        map.put("nonce_str", "tsDJRRtPBWdFyUOo");
        map.put("trade_type", "APP");
        map.put("sign", "9CA5AD1988FB3EE357A797159CD25194");
        map.put("appid", "wx2feae2c615187670");
        map.put("mch_id", "1510090681");
        String apiKey = "90b62d7e7f38972cdf6effe9364b21db";

        String mysign = PayUtils.buildSign(map, apiKey);

        System.out.println(mysign);


//        String mysign = PayUtils.buildSign(map, apiKey);
//        System.out.println(mysign);
    }

    private static Map<String, String> wxPayParamFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (key.equalsIgnoreCase("wxapikey")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    @Override
    public ResultClient handleWxBarcodeOrder(HttpServletRequest request, String authCode, int type, String title, String desc,
                                             int price, long subId, long boId) throws Exception {
        BusinessOrder businessOrder = businessOrderService.load(boId);
        Map<String, Object> info = Maps.newHashMap();
        info.put("uid", String.valueOf(businessOrder.getUid()));
        info.put("payType", type);
        info.put("money", price);
        info.put("boId", businessOrder.getId());
        info.put("payModel", Order.pay_mode_barcode);
        ResultClient resultClient = createWxOrder(Order.pay_mode_barcode, type, JsonUtils.toJson(info), title, desc,
                price, OrderExpireUnit.nvl, 0, subId);
        Order order = (Order) resultClient.getData();
        if (order == null) throw new StoreSystemException("order is null!");
        if (StringUtils.isBlank(authCode)) throw new StoreSystemException("authCode is null!");
        PayPassport payPassport = payPassportDao.load(order.getPassportId());
        String wxAppid = payPassport.getWxAppid();
        String wxMerchantId = payPassport.getWxMerchantId();
        String wxApiKey = payPassport.getWxApiKey();
        String wxPkcs12CertificateName = payPassport.getWxPkcs12CertificateName();
        if (StringUtils.isBlank(wxAppid)) throw new StoreSystemException("wxAppid is null!");
        if (StringUtils.isBlank(wxMerchantId)) throw new StoreSystemException("wxMerchantId is null!");
        if (StringUtils.isBlank(wxApiKey)) throw new StoreSystemException("wxApiKey is null!");
        if (StringUtils.isBlank(wxPkcs12CertificateName))
            throw new StoreSystemException("wxPkcs12CertificateName is null!");
        Map<String, String> sParaTemp = Maps.newHashMap();
        sParaTemp.put("appid", wxAppid);// 应用ID
        sParaTemp.put("mch_id", wxMerchantId);// 商户号
        sParaTemp.put("nonce_str", RandomStringUtils.randomAlphanumeric(16));// 随机字符串
        sParaTemp.put("body", StringUtils.isNotBlank(order.getTitle()) ? order.getTitle() : "");// 商品描述String(128)
        sParaTemp.put("detail", StringUtils.isNotBlank(order.getDesc()) ? order.getDesc() : "");// 商品详情String(8192)
        sParaTemp.put("out_trade_no", PayUtils.getOutTradeNo(order.getId(), order.getGmt()));// 商户订单号
        sParaTemp.put("total_fee", String.valueOf(order.getPrice()));// total_fee,Int,单位为分
        sParaTemp.put("spbill_create_ip", request.getRemoteAddr());
        sParaTemp.put("auth_code", authCode);
        String mysign = PayUtils.buildSign(sParaTemp, wxApiKey);
        sParaTemp.put("sign", URLEncoder.encode(mysign, Constant.defaultCharset));
        String xml = PayUtils.createXmlString(sParaTemp);
        String sendRes = PayUtils.send(wxpayBarcodeUrl, xml);
        Map<String, String> resMap = PayUtils.xmlStrToMap(sendRes);
        String code = resMap.get("return_code");
        boolean res = false;
        if (code.equals("SUCCESS")) {
            String result_code = resMap.get("result_code");
            if ("SUCCESS".equals(result_code)) {
                order.setStatus(Order.status_pay);
                order.setDetail(sendRes);
                order.setAuthCode(authCode);
            } else {
                String path = request.getServletContext().getRealPath("");
                File file = new File(path);
                path = file.getParent();
                file = new File(path + "/" + wxPkcs12CertificateName);
                String out_trade_no = PayUtils.getOutTradeNo(order.getId(), order.getGmt());
                waitWxBarcodeOrderRes(file, payPassport, out_trade_no, order);
            }
        }
        ResultClient result = new ResultClient();
        if (order.getStatus() == Order.status_pay) {
            res = true;
            result = orderPayService.successHandleBusiness(order, boId);
        }
        orderDao.update(order);
        if (res) {
            return result;
        } else {
            return new ResultClient(false);
        }
    }

    @Override
    public boolean wxOrderNotify(String xmlStr) throws Exception {
        Map<String, String> params = PayUtils.xmlStrToMap(xmlStr);
        String out_trade_no = PayUtils.getParam(params, "out_trade_no");// 商户订单号
        long orderId = PayUtils.getOrderId(out_trade_no);
        Order order = orderDao.load(orderId);
        if (null == order) throw new StoreSystemException("order is null!");
        long passportId = order.getPassportId();
        PayPassport payPassport = payPassportDao.load(passportId);
        if (null == payPassport) throw new StoreSystemException("passport is null!");
        String wxApiKey = payPassport.getWxApiKey();
        if (StringUtils.isBlank(wxApiKey)) throw new StoreSystemException("wxApiKey is null!");
        if (validSign(params, wxApiKey)) {
            ZkLock lock = null;
            try {
                lock = ZkLock.getAndLock(lockZkAddress, projectName, projectName + "_order_" + orderId + "_lock",
                        true, 5000, 10000);
                String transaction_id = PayUtils.getParam(params, "transaction_id");// 微信支付订单号
                long notify_time = System.currentTimeMillis();
                String result_code = PayUtils.getParam(params, "result_code");
                long time_end = DateUtils.parseDate(PayUtils.getParam(params, "time_end"), "yyyyMMddHHmmss").getTime();
                String detail = JsonUtils.toJson(params);
                OrderNotify orderNotify = new OrderNotify();
                orderNotify.setDetail(detail);
                orderNotify.setNotifyTime(notify_time);
                orderNotify.setOid(orderId);
                orderNotifyDao.insert(orderNotify);
                int status = order.getStatus();
                if (result_code.equals("SUCCESS") && status == Order.status_no_pay) {
                    order.setStatus(Order.status_pay);
                    order.setOrderNo(transaction_id);
                    order.setPayTime(time_end);
                    orderPayService.successHandleBusiness(order, 0);
                    orderDao.update(order);
                }
                return true;
            } finally {
                if (null != lock) lock.release();
            }
        }
        return false;
    }

    @Override
    public RefundOrder createAliRefundOrder(long oid) throws Exception {
        Order order = orderDao.load(oid);
        if (null == order) throw new StoreSystemException("order is null!");
        if (order.getStatus() != Order.status_pay) throw new StoreSystemException("order status is error!");
        if (order.getPayType() != Order.pay_type_ali) throw new StoreSystemException("order is not ali!");
        RefundOrder refundOrder = new RefundOrder();
        long gmt = NumberUtils.toLong(gmtFormat.format(new Date()));
        refundOrder.setGmt(gmt);
        refundOrder.setPayType(RefundOrder.pay_type_ali);
        refundOrder.setOid(oid);
        return refundOrderDao.insert(refundOrder);
    }

    @Override
    public RefundOrder createWxRefundOrder(long oid) throws Exception {
        Order order = orderDao.load(oid);
        if (null == order) throw new StoreSystemException("order is null!");
        if (order.getStatus() != Order.status_pay) throw new StoreSystemException("order status is error!");
        if (order.getPayType() != Order.pay_type_wx) throw new StoreSystemException("order is not ali!");
        RefundOrder refundOrder = new RefundOrder();
        long gmt = NumberUtils.toLong(gmtFormat.format(new Date()));
        refundOrder.setGmt(gmt);
        refundOrder.setPayType(RefundOrder.pay_type_wx);
        refundOrder.setOid(oid);
        return refundOrderDao.insert(refundOrder);
    }

    @Override
    public boolean handleAliRefundOrder(long roid) throws Exception {
        RefundOrder refundOrder = refundOrderDao.load(roid);
        if (null == refundOrder) throw new StoreSystemException("refundOrder is null!");
        if (refundOrder.getPayType() != RefundOrder.pay_type_ali)
            throw new StoreSystemException("refundOrder is not ali!");
        if (refundOrder.getStatus() != 0) throw new StoreSystemException("refundOrder status is error!");
        long oid = refundOrder.getOid();
        Order order = orderDao.load(oid);
        if (null == order) throw new StoreSystemException("order is null!");
        long passportId = order.getPassportId();
        PayPassport payPassport = payPassportDao.load(passportId);
        if (null == payPassport) throw new StoreSystemException("passport is null!");
        String aliAddid = payPassport.getAliAppid();
        String aliPrivateKey = payPassport.getAliPrivateKey();
        if (StringUtils.isBlank(aliAddid)) throw new StoreSystemException("aliAddid is null!");
        if (StringUtils.isBlank(aliPrivateKey)) throw new StoreSystemException("aliPrivateKey is null!");
        String trade_no = order.getOrderNo();
        double refund_amount = ArithUtils.div(order.getPrice(), 100.0, 2);
        String out_request_no = PayUtils.getOutTradeNo(roid, refundOrder.getGmt());
        Map<String, Object> bizContentMap = Maps.newLinkedHashMap();
        bizContentMap.put("trade_no", trade_no);
        bizContentMap.put("refund_amount", refund_amount);
        bizContentMap.put("out_request_no", out_request_no);
        String biz_content = JsonUtils.toJson(bizContentMap);
        Map<String, String> sParaTemp = Maps.newHashMap();
        sParaTemp.put("app_id", aliAddid);
        sParaTemp.put("method", "alipay.trade.refund");
        sParaTemp.put("charset", Constant.defaultCharset);
        sParaTemp.put("sign_type", "RSA2");
        sParaTemp.put("timestamp", DateUtils.simpleDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        sParaTemp.put("version", "1.0");
        sParaTemp.put("biz_content", biz_content);
        String prestr = PayUtils.createLinkString(sParaTemp);
        String mysign = RSA.sign256(prestr, aliPrivateKey, Constant.defaultCharset);
        sParaTemp.put("sign", mysign);
        List<String> keys = new ArrayList<String>(sParaTemp.keySet());
        Collections.sort(keys);
        String urlParamStr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = sParaTemp.get(key);
            value = URLEncoder.encode(value, Constant.defaultCharset);
            if (i == keys.size() - 1) { // 拼接时，不包括最后一个&字符
                urlParamStr += key + "=" + value;
            } else {
                urlParamStr += key + "=" + value + "&";
            }
        }
        String url = alipayGateway + urlParamStr;
        String repStr = PayUtils.send(url);
        refundOrder.setResDetail(repStr);
        Map<String, String> repMap = (Map<String, String>) JsonUtils.fromJson(repStr, Map.class)
                .get("alipay_trade_refund_response");
        String code = repMap.get("code");
        boolean res = false;
        if (code.equals("10000")) {
            res = true;
            refundOrder.setStatus(RefundOrder.status_refund_success);
        } else {
            refundOrder.setStatus(RefundOrder.status_refund_fail);
        }

        if (refundOrder.getStatus() == RefundOrder.status_refund_success) {
            order.setStatus(Order.status_refund);
            orderRefundService.successHandleBusiness(order.getType(), order.getTypeInfo());
            orderDao.update(order);
        } else if (refundOrder.getStatus() == RefundOrder.status_refund_fail) {
            orderRefundService.failHandleBusiness(order.getPayType(), order.getTypeInfo());
        }
        refundOrderDao.update(refundOrder);
        return res;
    }

    @Override
    public boolean handleWxRefundOrder(HttpServletRequest request, long roid) throws Exception {
        RefundOrder refundOrder = refundOrderDao.load(roid);
        if (null == refundOrder) throw new StoreSystemException("refundOrder is null!");
        if (refundOrder.getPayType() != RefundOrder.pay_type_wx)
            throw new StoreSystemException("refundOrder is not wx!");
        if (refundOrder.getStatus() != 0) throw new StoreSystemException("refundOrder status is error!");
        long oid = refundOrder.getOid();
        Order order = orderDao.load(oid);
        if (null == order) throw new StoreSystemException("order is null!");
        long passportId = order.getPassportId();
        PayPassport payPassport = payPassportDao.load(passportId);
        if (null == payPassport) throw new StoreSystemException("passport is null!");
        String wxAppid = payPassport.getWxAppid();
        String wxMerchantId = payPassport.getWxMerchantId();
        String wxApiKey = payPassport.getWxApiKey();
        String wxPkcs12CertificateName = payPassport.getWxPkcs12CertificateName();
        if (StringUtils.isBlank(wxAppid)) throw new StoreSystemException("wxAppid is null!");
        if (StringUtils.isBlank(wxMerchantId)) throw new StoreSystemException("wxMerchantId is null!");
        if (StringUtils.isBlank(wxApiKey)) throw new StoreSystemException("wxApiKey is null!");
        if (StringUtils.isBlank(wxPkcs12CertificateName))
            throw new StoreSystemException("wxPkcs12CertificateName is null!");
        String transaction_id = order.getOrderNo();
        Map<String, String> sParaTemp = Maps.newHashMap();
        sParaTemp.put("appid", wxAppid);// 应用ID
        sParaTemp.put("mch_id", wxMerchantId);// 商户号
        sParaTemp.put("nonce_str", RandomStringUtils.randomAlphanumeric(16));// 随机字符串
        sParaTemp.put("transaction_id", transaction_id);
        String out_refund_no = PayUtils.getOutTradeNo(roid, refundOrder.getGmt());
        sParaTemp.put("out_refund_no", out_refund_no);
        long total_fee = (long) (order.getPrice());
        sParaTemp.put("total_fee", String.valueOf(total_fee));
        sParaTemp.put("refund_fee", String.valueOf(total_fee));
        sParaTemp.put("op_user_id", wxMerchantId);
        String mysign = PayUtils.buildSign(sParaTemp, wxApiKey);
        sParaTemp.put("sign", URLEncoder.encode(mysign, Constant.defaultCharset));
        String xml = PayUtils.createXmlString(sParaTemp);
        String path = request.getServletContext().getRealPath("");
        File file = new File(path);
        path = file.getParent();
        file = new File(path + "/" + wxPkcs12CertificateName);
        String sendRes = PayUtils.sendPKCS(file, wxMerchantId, wxpayRefundUrl, xml);
        Map<String, String> params = PayUtils.xmlStrToMap(sendRes);
        refundOrder.setResDetail(sendRes);
        String return_code = params.get("return_code");
        boolean res = false;
        if ("SUCCESS".equals(return_code)) {
            res = true;
            refundOrder.setStatus(RefundOrder.status_refund_success);
        } else refundOrder.setStatus(RefundOrder.status_refund_fail);

        if (refundOrder.getStatus() == RefundOrder.status_refund_success) {
            order.setStatus(Order.status_refund);
            orderRefundService.successHandleBusiness(order.getType(), order.getTypeInfo());
            orderDao.update(order);
        } else {
            orderRefundService.failHandleBusiness(order.getType(), order.getTypeInfo());
        }
        refundOrderDao.update(refundOrder);
        return res;
    }

    @Override
    public ResultClient handleOtherPay(int type, int price, long boId) throws Exception {
        BusinessOrder businessOrder = businessOrderService.load(boId);
        Order order = new Order();
        order.setPrice(price);
        order.setPayType(type);
        Map<String, Object> info = Maps.newHashMap();
        info.put("uid", String.valueOf(businessOrder.getUid()));
        info.put("payType", type);
        info.put("money", price);
        info.put("boId", businessOrder.getId());
        info.put("payModel", Order.pay_mode_barcode);
        order.setTypeInfo(JsonUtils.toJson(info));
        ResultClient res = orderPayService.successHandleBusiness(order, boId);
        return res;

    }

    @Override
    public Pager getAll(Pager pager, long startTime, long endTime, long personnelid, int status, long uid, String name, int makeStatus, long subid) throws Exception {
        String sql = "SELECT  *  FROM `order`   where  1=1 ";
        String sqlCount = "SELECT  COUNT(*)  FROM `order` where 1=1";
        String limit = "  limit %d , %d ";
        if (subid > 0) {
            sql = sql + " and `subid` = " + subid;
            sqlCount = sqlCount + " and `subid` = " + subid;
        }
        if (makeStatus > 0) {
            sql = sql + " and `makeStatus` = " + makeStatus;
            sqlCount = sqlCount + " and `makeStatus` = " + makeStatus;
        }

        if (personnelid > 0) {
            sql = sql + " and `personnelid` = " + personnelid;
            sqlCount = sqlCount + " and `personnelid` = " + personnelid;
        }
        if (startTime > 0) {
            sql = sql + " and `payTime` >" + startTime;
            sqlCount = sqlCount + " and `payTime` >" + startTime;
        }
        if (endTime > 0) {
            sql = sql + " and `payTime` <" + endTime;
            sqlCount = sqlCount + " and `payTime` <" + endTime;
        }
        if (uid > 0) {
            sql = sql + " and `uid` =" + uid;
            sqlCount = sqlCount + " and `uid` =" + uid;
        }
        if (StringUtils.isNotBlank(name)) {
            sql = sql + " and  `uid` in (select id from `user`  where name like '%" + name + "%')";
            sqlCount = sqlCount + " and  uid in (select id from `user`  where name like '%" + name + "%')";
        }
        sql = sql + " order  by ctime desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        int count = 0;
        List<Order> orderList = this.jdbcTemplate.query(sql, rowMapper);
        count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);

        pager.setData(transformClient(orderList));
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public Pager getBackPager(Pager pager, long subid, String name, String phone, String orderNo) throws Exception {
        String sql = "SELECT o.* FROM `order` as o LEFT JOIN `user` as u ON o.uid=u.id WHERE 1=1 ";
        String sqlCount = "SELECT COUNT(*) FROM `order` as o LEFT JOIN `user` as u ON o.uid=u.id WHERE 1=1 ";
        String limit = "  limit %d , %d ";

        {
            sql = sql + " and o.`status` = " + Order.status_pay;
            sqlCount = sqlCount + " and o.`status` = " + Order.status_pay;
        }
        if (subid > 0) {
            sql = sql + " and o.`subid` = " + subid;
            sqlCount = sqlCount + " and o.`subid` = " + subid;
        }
        if (StringUtils.isNotBlank(name)) {
            sql = sql + " and u.`name` like ?";
            sqlCount = sqlCount + " and u.`name` like ?";
        }
        if (StringUtils.isNotBlank(phone)) {
            sql = sql + " and u.`phone` like ?";
            sqlCount = sqlCount + " and u.`phone` like ?";
        }
        if (StringUtils.isNotBlank(orderNo)) {
            sql = sql + " and o.`orderNo` like ?";
            sqlCount = sqlCount + " and o.`orderNo` like ?";
        }

        sql = sql + String.format(limit, pager.getSize() * (pager.getPage() - 1), pager.getSize());
        List<Order> orders = null;
        int count = 0;
        List<Object> objects = new ArrayList<>();
        if (StringUtils.isNotBlank(name)) {
            objects.add("%" + name + "%");
        }
        if (StringUtils.isNotBlank(phone)) {
            objects.add("%" + phone + "%");
        }
        if (StringUtils.isNotBlank(orderNo)) {
            objects.add("%" + orderNo + "%");
        }
        if (objects.size() > 0) {
            Object[] args = new Object[objects.size()];
            objects.toArray(args);
            orders = jdbcTemplate.query(sql, new HyperspaceBeanPropertyRowMapper<Order>(Order.class), args);
            count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        } else {
            orders = jdbcTemplate.query(sql, new HyperspaceBeanPropertyRowMapper<Order>(Order.class));
            count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        }
        pager.setData(transformClient(orders));
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public ResultClient saveOrder(Order order) throws Exception {
/*        List<PayPassport> payPassports = payPassportDao.getAllList(order.getSubid(),PayPassport.status_on);
        PayPassport payPassport = null;
        if(payPassports.size()>0){
            payPassport = payPassports.get(0);
        }
        if(null == payPassport) return new ResultClient(false,null,"门店暂未设置支付方式,请联系管理员！");
        order.setPassportId(payPassport.getId());
        Order insertOrder = orderDao.insert(order);
        ClientOrder clientOrder = new ClientOrder(insertOrder);
        List<OptometryInfo> optometryInfos = new ArrayList<>();
        //转换金额
        clientOrder.setTotalPriceYuan(insertOrder.getTotalPrice() / 100.0);//总金额
        clientOrder.setDicountPriceYuan(insertOrder.getDicountPrice() / 100.0);//折扣金额
*//*        if (insertOrder.getPayType() == Order.pay_type_ali) {
            clientOrder.setPriceYuan(insertOrder.getAliPrice() / 100.0); //实际支付金额
        } else if (insertOrder.getPayType() == Order.pay_type_wx) {
            clientOrder.setPriceYuan(insertOrder.getWxPrice() / 100.0); //实际支付金额
        }*//*


        if(insertOrder.getOiId()>0){
            // 验光信息
            OptometryInfo load = optometryInfoDao.load(insertOrder.getOiId());
            optometryInfos.add(load);
            clientOrder.setOptometryInfos(optometryInfos);
        }
        if(insertOrder.getCouponid()>0){
            //优惠券信息
            MarketingCoupon marketingCoupon = marketingCouponDao.load(insertOrder.getCouponid());
            clientOrder.setCouponName(marketingCoupon.getTitle());
            if(marketingCoupon.getDescSubtractType()==MarketingCoupon.desc_subtract_type_money){
                clientOrder.setDescSubtract(marketingCoupon.getDescSubtract()/100.0);
            }else{
                clientOrder.setDescSubtract(marketingCoupon.getDescSubtract());
            }
        }*/
        return new ResultClient(order);
    }

/*    @Override
    public List<ClientOrder> getAllBySubid(long subid) throws Exception {
        List<Order> list= orderDao.getAllBySubid(subid, Order.status_pay, Order.makeStatus_qu_yes);
        return  transformClient(list);
    }*/

    @Override
    public ClientOrder countPrice(Order order) throws Exception {
        ClientOrder clientOrder = new ClientOrder(order);
/*        double dicountPriceYuan=0.0d;
        double totaoPriceyuan=0.0d;
        //小计单，通过从数据库取数据计算订单上的单sku的小计
        List<OrderSku> orderSkuList = order.getSkuids();
        Map map = countSkuPrice(order.getUid(), orderSkuList, order.getCouponid(), order.getSurcharges(),order.getSubid());
        dicountPriceYuan = (double) map.get("discountPriceYuan");
        totaoPriceyuan = (double) map.get("totalPriceYuan");
        clientOrder.setClientSkuids((List<OrderSku>) map.get("clientOrderSkus"));
        clientOrder.setDicountPriceYuan(dicountPriceYuan);
        clientOrder.setTotalPriceYuan(totaoPriceyuan);*/
        return clientOrder;
    }
//    public Map<Object,Object> countSkuPriceddddd(long uid,List<OrderSku> orderSkuList,long couponid, List<Surcharge> surchargeList){
//        List<ClientOrderSku> clientOrderSkus=new ArrayList<>();
//        Map map=new HashMap();
//        int surchargePrice=0;//附加费
//        int totalPrice=0;//总金额
//        User userDb = userDao.load(uid);
//        UserGrade userGrade = userGradeDao.load(userDb.getUserGradeId());
//        double dicountPriceYuan=0.0d;
//        double totaoPriceyuan=0.0d;
//        for(OrderSku orderSku:orderSkuList) {
//            int num=0;
//            ClientOrderSku clientOrderSku=new ClientOrderSku();
//            ProductSKU productSKU = productSKUDao.load(orderSku.getSkuid());
//            //--
//            ProductSPU productSPU = productSPUDao.load(orderSku.getSpuid());
//
//            // 拿取库存
//            List<InventoryDetail> allDetails = inventoryDetailDao.getAllListBySKU(productSKU.getId());
//            for (InventoryDetail detail : allDetails) {
//                if (detail.getSubid() == userDb.getSid()) {
//                    num += detail.getNum();
//                }
//            }
//            if (productSPU.getType() == ProductSPU.type_common) {
//                if(num>orderSku.getNum()){
//                    UserGradeCategoryDiscount userGradeCategoryDiscount = new UserGradeCategoryDiscount();
//                    userGradeCategoryDiscount.setSpuid(orderSku.getSpuid());
//                    userGradeCategoryDiscount.setUgid(userDb.getUserGradeId());
//                    UserGradeCategoryDiscount discount = userGradeCategoryDiscountDao.load(userGradeCategoryDiscount);
//                    if (discount != null) {
//                        //有折扣的商品
//                        double dis = discount.getDiscount() / 10.0;
//                        clientOrderSku.setSkuid(orderSku.getSkuid());
//                        clientOrderSku.setSpuid(orderSku.getSpuid());
//                        clientOrderSku.setNum(orderSku.getNum());
//                        clientOrderSku.setPrice(productSKU.getRetailPrice() / 100.0);
//                        clientOrderSku.setSubtotal((productSKU.getRetailPrice() * orderSku.getNum() * dis) / 100.0);
//                        clientOrderSku.setDiscount(discount.getDiscount());
//                        DecimalFormat df = new DecimalFormat("#.00");
//                        double subtotal = Double.parseDouble(df.format(productSKU.getRetailPrice() * orderSku.getNum() * dis * (userGrade.getDiscount() / 10.0) * 0.01));
//                        clientOrderSku.setLastSubtotal(subtotal);
//                        totalPrice += productSKU.getRetailPrice() * orderSku.getNum() * dis;
//                    } else {
//                        //没有设置折扣的商品
//                        clientOrderSku.setSkuid(orderSku.getSkuid());
//                        clientOrderSku.setSpuid(orderSku.getSpuid());
//                        clientOrderSku.setNum(orderSku.getNum());
//                        clientOrderSku.setDiscount(0);
//                        clientOrderSku.setPrice(productSKU.getRetailPrice() / 100.0);
//                        clientOrderSku.setSubtotal((productSKU.getRetailPrice() * orderSku.getNum()) / 100.0);
//                        clientOrderSku.setLastSubtotal((productSKU.getRetailPrice() * orderSku.getNum() * (userGrade.getDiscount() / 10.0)) / 100.0);
//                        totalPrice += productSKU.getRetailPrice() * orderSku.getNum();
//                    }
//                }else{
//                    throw new StoreSystemException("当前商品库存不足！");
//                }
//
//
//            } else {
//                // 积分
//                // 判断库存
//                if(productSKU.getNum()>orderSku.getNum()){
//                    if (productSPU.getIntegralNum() < orderSku.getNum()) {
//                        throw new StoreSystemException("积分商品数量兑换上限！");
//                    }
//                    //判断用户积分。
//                    if (productSKU.getIntegralPrice() * orderSku.getNum() > userDb.getScore()) {
//                        throw new StoreSystemException("会员积分不足兑换该积分商品！");
//                    }
//                }else{
//                    throw new StoreSystemException("当前商品库存不足！");
//                }
//
//                clientOrderSku.setPrice(productSKU.getIntegralPrice());
//                clientOrderSku.setSubtotal(productSKU.getIntegralPrice() * orderSku.getNum());
//            }
//            //---
//
//             totaoPriceyuan = totalPrice;
//             dicountPriceYuan = totalPrice * userGrade.getDiscount();//折后金额
//            //计算促销券
//            if (couponid > 0) {
//                MarketingCoupon marketingCoupon = marketingCouponDao.load(couponid);
//                if (marketingCoupon.getDescSubtractType() == MarketingCoupon.desc_subtract_type_money) {
//                    dicountPriceYuan = ((dicountPriceYuan - marketingCoupon.getDescSubtract()));
//                }
//                if (marketingCoupon.getDescSubtractType() == MarketingCoupon.desc_subtract_type_rate) {
//                    dicountPriceYuan = ((dicountPriceYuan - totaoPriceyuan * (marketingCoupon.getDescSubtract() / 10.0)));
//                }
//            } else {
//                dicountPriceYuan = dicountPriceYuan;
//            }
//            if (dicountPriceYuan < 0) {
//                dicountPriceYuan = 0.0d;
//            }
//            clientOrderSkus.add(clientOrderSku);
//            if (surchargeList.size() > 0) {
//                // 拿到附加费用加入总价格
//                for (Surcharge surcharge : surchargeList) {
//                    surchargePrice += surcharge.getPrice();
//                }
//            }
//            totaoPriceyuan+=ArithUtils.add(totaoPriceyuan,(surchargePrice));
//        }
//        map.put("surchargeList",surchargeList);//附加费用
//        map.put("DicountPriceYuan",dicountPriceYuan/100.0);//折扣后金额
//        map.put("totaoPriceyuan",totaoPriceyuan/100.0);//总金额
//        map.put("clientOrderSkus",clientOrderSkus);//sku小计单
//        return  map;
//    }

    @Override
    public Map<Object, Object> countSkuPrice(long uid, List<OrderSku> orderSkuList, long couponid, List<Surcharge> surchargeList, long sid) throws Exception {
        Map<Object, Object> map = Maps.newHashMap();
       /* List<Long> skuIds = Lists.newArrayList();
        List<Long> spuIds = Lists.newArrayList();
        Map<Long, Integer> inventoryMap = Maps.newHashMap();
        User user = null;
        long userGid=0;
        long score=0;
        UserGrade userGrade = null;
        if(uid>0) {
            user = userDao.load(uid);
            if(user!=null) {
                userGrade = userGradeDao.load(user.getUserGradeId());//会员等级类目折扣
                userGid = user.getUserGradeId();
                score = user.getScore();
            }
        }

        for(OrderSku orderSku:orderSkuList){
            skuIds.add(orderSku.getSkuid());
            spuIds.add(orderSku.getSpuid());

            int inventoryNum = 0;//库存量
            List<InventoryDetail> allDetails = inventoryDetailDao.getAllListBySKU(orderSku.getSkuid());
            for (InventoryDetail detail : allDetails) {
                if (detail.getSubid() == sid) {
                    inventoryNum += detail.getNum();
                }
            }
            inventoryMap.put(orderSku.getSkuid(),inventoryNum);
       }

       List<ProductSKU> skuList = productSKUDao.load(skuIds);
        List<ProductSPU> spuList = productSPUDao.load(spuIds);
        Map<Long, ProductSKU> skuMap = skuMapUtils.listToMap(skuList, "id");
        Map<Long, ProductSPU> spuMap = spuMapUtils.listToMap(spuList, "id");

        double discountPriceYuan=0;
        double totalPrice=0;//总金额 原价
        int surchargePrice=0;//附加费

        for(OrderSku orderSku:orderSkuList){
            long skuId=orderSku.getSkuid();
            long spuId=orderSku.getSpuid();
            double spuDiscount=0;//商品折扣
            long cid = spuMap.get(spuId).getCid();//类目
            double userDisCount = 0;//会员类目折扣
            if (userGrade != null) {
                Map<Long, Object> disMap = userGrade.getDiscount();
                if(disMap.size()>0) {
                    userDisCount = (double) disMap.get(cid);
                }
            }
            //常规商品
            if(spuMap.get(spuId).getType()==ProductSPU.type_common){
                if(orderSku.getNum()>inventoryMap.get(skuId)) throw new StoreSystemException("当前常规商品库存数量不足！");
                UserGradeCategoryDiscount ugcd = new UserGradeCategoryDiscount();
                ugcd.setSpuid(spuId);
                ugcd.setUgid(userGid);
                ugcd = userGradeCategoryDiscountDao.load(ugcd);
                if(ugcd!=null){
                    spuDiscount = ugcd.getDiscount();
                }

                double retailPrice = 0;
                double subPrice = 0;
                double subTotal = 0;
                double unitPrice = skuMap.get(skuId).getRetailPrice() * orderSku.getNum();
                if (spuDiscount == 0) {
                    //小计
                    subPrice = unitPrice;
                    subTotal = unitPrice * (userDisCount / 10.0);

                } else if (userDisCount == 0) {
                    //小计
                    subPrice = unitPrice * (spuDiscount / 10.0);
                    subTotal = unitPrice * (spuDiscount / 10.0);
                } else {
                    //小计
                    subPrice = unitPrice * (spuDiscount / 10.0);
                    subTotal = unitPrice * (spuDiscount / 10.0) * (userDisCount / 10.0);
                }
                //商品单价
                retailPrice = skuMap.get(skuId).getRetailPrice() / 100.0;
                orderSku.setPrice(retailPrice);
                orderSku.setCode(skuMap.get(skuId).getCode());
                orderSku.setName(skuMap.get(skuId).getName());
                orderSku.setDiscount(spuDiscount);
                orderSku.setSubtotal(subPrice/100.0);
                orderSku.setLastSubtotal(subTotal/100.0);
                totalPrice = ArithUtils.add(subPrice,totalPrice);
            }

            //积分商品
            if(spuMap.get(spuId).getType()==ProductSPU.type_integral){

                if(orderSku.getNum()>inventoryMap.get(skuId)) {
                    throw new StoreSystemException("当前积分商品库存数量不足！");
                }
                if (spuMap.get(spuId).getIntegralNum() < orderSku.getNum()) {
                    throw new StoreSystemException("积分商品数量兑换上限！");
                }
                if (skuMap.get(skuId).getIntegralPrice() * orderSku.getNum() > score) {
                    throw new StoreSystemException("会员积分不足以兑换该积分商品！");
                }
                double integralPrice = 0;
                double subPrice = 0;
                double subTotal = 0;
                integralPrice = skuMap.get(skuId).getIntegralPrice() / 100.0;
                double unitPrice = integralPrice * orderSku.getNum();
                if (userDisCount == 0) {
                    subTotal = (unitPrice) / 100.0;
                } else {
                    subTotal = (unitPrice * (userDisCount / 10.0)) / 100.0;
                }
                orderSku.setPrice(integralPrice);
                orderSku.setCode(skuMap.get(skuId).getCode());
                orderSku.setName(skuMap.get(skuId).getName());
                orderSku.setDiscount(0);//积分商品没有折扣
                orderSku.setSubtotal(subPrice);
                orderSku.setLastSubtotal(subTotal);
                totalPrice = ArithUtils.add(subPrice,totalPrice);
            }

        if(userDisCount==0) {
            discountPriceYuan = totalPrice;
        }else{
            discountPriceYuan = totalPrice * userDisCount/10.0;//折后金额
        }
        //todo:
        //计算促销券
*//*        if (couponid > 0) {
            MarketingCoupon marketingCoupon = marketingCouponDao.load(couponid);
            if (marketingCoupon.getDescSubtractType() == MarketingCoupon.desc_subtract_type_money) {
                discountPriceYuan = ((discountPriceYuan - marketingCoupon.getDescSubtract()));
            }
            if (marketingCoupon.getDescSubtractType() == MarketingCoupon.desc_subtract_type_rate) {
                discountPriceYuan = ((discountPriceYuan - totalPrice * (marketingCoupon.getDescSubtract() / 10.0)));
            }
        }*//*
        //添加附加费用
        if (surchargeList.size() > 0) {
            for (Surcharge surcharge : surchargeList) {
                surchargePrice += surcharge.getPrice();
            }
        }
            totalPrice = ArithUtils.add(totalPrice, surchargePrice);
        }
        map.put("surchargeList",surchargeList);//附加费用
        map.put("discountPriceYuan",ArithUtils.div(discountPriceYuan,100.0,2));//折扣后金额
        map.put("totalPriceYuan",ArithUtils.div(totalPrice,100.0,2));//总金额
        map.put("orderSkus",orderSkuList);//sku小计单*/
        return map;
    }

/*    @Override
    public List<ClientOrder> getTemporaryOrder(long subid) throws Exception {
        List<Order> list = orderDao.getTemporaryOrder(subid, Order.makeStatus_temporary);
        return transformClientTem(list);
    }*/

    @Override
    public Pager getAllIncomplete(Pager pager, long startTime, long endTime, long personnelid, int status, long uid, String name, long subid, int makeStatus) throws Exception {
        String sql = "SELECT  *  FROM `order`   where  1=1  ";
        String sqlCount = "SELECT  COUNT(*)  FROM `order` where 1=1  ";
        String limit = "  limit %d , %d ";

        if (subid > 0) {
            sql = sql + " and `subid` = " + subid;
            sqlCount = sqlCount + " and `subid` = " + subid;
        }
        if (makeStatus > 0) {
            sql = sql + " and `makeStatus` = " + makeStatus;
            sqlCount = sqlCount + " and `makeStatus` = " + makeStatus;
        } else {
            sql = sql + " and (`makeStatus` = 1 OR `makeStatus` = 2 OR `makeStatus` = 3 ) ";
            sqlCount = sqlCount + " and  (`makeStatus` = 1 OR `makeStatus` = 2 OR `makeStatus` = 3) ";
        }
        if (personnelid > 0) {
            sql = sql + " and `personnelid` = " + personnelid;
            sqlCount = sqlCount + " and `personnelid` = " + personnelid;
        }
        if (startTime > 0) {
            sql = sql + " and `payTime` >" + startTime;
            sqlCount = sqlCount + " and `payTime` >" + startTime;
        }
        if (endTime > 0) {
            sql = sql + " and `payTime` <" + endTime;
            sqlCount = sqlCount + " and `payTime` <" + endTime;
        }
        if (uid > 0) {
            sql = sql + " and `uid` =" + uid;
            sqlCount = sqlCount + " and `uid` =" + uid;
        }
        if (StringUtils.isNotBlank(name)) {
            sql = sql + " and  `uid` in (select id from `user`  where name like '%" + name + "%')";
            sqlCount = sqlCount + " and  uid in (select id from `user`  where name like '%" + name + "%')";
        }
        sql = sql + " order  by ctime desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        int count = 0;
        List<Order> orderList = this.jdbcTemplate.query(sql, rowMapper);
        count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);

        pager.setData(transformClient(orderList));
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public Map<String, Object> loadOrder(long id) throws Exception {
        Map<String, Object> map = Maps.newHashMap();
        ClientOrder clientOrder = transformClient(orderDao.load(id));
        List<ClientAfterSaleDetail> details = afterSaleDetailService.getAllListByOid(id);
        map.put("order", clientOrder);
        map.put("afterSaleDetails", details);
        return map;
    }

    @Override
    public boolean updateOrder(Order order) throws Exception {
        Order dbInfo = orderDao.load(order.getId());
/*        if(order.getRechargePrice()>0){
            dbInfo.setRechargePrice(order.getRechargePrice());
            walletService.refresh(dbInfo.getUid(),order.getRechargePrice()/100.0);
            dbInfo.setMakeStatus(Order.makestatus_recharge);
        }
        if(order.getCashPrice()>0){
            dbInfo.setCashPrice(order.getCashPrice());
            dbInfo.getPayTypes().add(Order.pay_type_cash);
        }
        if(order.getStoredPrice()>0){
            dbInfo.setStoredPrice(order.getStoredPrice());
            dbInfo.getPayTypes().add(Order.pay_type_stored);
        }
        if(StringUtils.isNotBlank(order.getReceiptDesc())){
            dbInfo.setReceiptDesc(order.getReceiptDesc());
        }*/
        return orderDao.update(dbInfo);
    }

//    @Override
//    public Map<String,Object> saleReward(long subid) throws Exception {
//        Map<String,Object> map = Maps.newLinkedHashMap();
//        List<SaleReward> saleRewards = Lists.newArrayList();
//        int total = 0;//总奖励
//        List<Order> orders = orderDao.getAllBySubid(subid,Order.status_pay,Order.makestatus_qu_yes);
//        if(orders.size()>0){
//            for(Order order:orders){
//                List<OrderSku> orderSkus = order.getSkuids();
//                for (OrderSku orderSku:orderSkus){
//                    Commission personal = null;
//                    Commission team = null;
//                    ProductSKU productSKU = productSKUDao.load(orderSku.getSkuid());
//                    if(productSKU!=null){
//                        long spuId = productSKU.getSpuid();
//                        //获得商品的个人提成
//                        List<Commission> commissionsP = commissionDao.getAllList(subid,spuId,Commission.type_personal);
//                        if(commissionsP.size()>0){ personal=commissionsP.get(0); }
//                        //获得商品的团队提成
//                        List<Commission> commissionsT = commissionDao.getAllList(subid,spuId,Commission.type_team);
//                        if(commissionsT.size()>0){ team = commissionsT.get(0); }
//                        if(personal!=null&&team!=null){
//                            SaleReward saleReward = new SaleReward();
//                            saleReward.setNumber(orderSku.getNum());
//                            saleReward.setProductName(orderSku.getName());
//                            saleReward.setRewardPersonal(personal.getPrice()/100);//个人提成
//                            saleReward.setRewardTeam(team.getPrice()/100);//团队提成
//                            saleReward.setRoyaltyPersonal((team.getPrice()*orderSku.getNum())/100);//团队奖励 数量*提成
//                            saleReward.setRoyaltyTeam((personal.getPrice()*orderSku.getNum())/100);//个人奖励
//                            saleRewards.add(saleReward);
//                            total+=saleReward.getRoyaltyPersonal()+saleReward.getRoyaltyTeam();
//                        }
//                    }
//                }
//            }
//        }
//        map.put("saleRewards",saleRewards);
//        map.put("total",total);
//        return map;
//    }

    @Override
    public Map<String, Object> saleReward(long subid) throws Exception {
        Map<String, Object> map = Maps.newLinkedHashMap();
        List<SaleReward> saleRewards = Lists.newArrayList();
        int totalP = 0;
        int totalT = 0;
//        *获得门店下的商品提成集合*
        List<Commission> commissionList = commissionDao.getAllList(subid);
        if (commissionList.size() > 0) {
            for (Commission commission : commissionList) {
                int number = 0;
                int royaltyPersonal = 0;
                int royaltyTeam = 0;
//                    *根据uid查询订单*
                List<BusinessOrder> orderList = businessOrderService.getAllBySubid(commission.getSubId(),BusinessOrder.status_pay ,BusinessOrder.makeStatus_qu_yes);
                for (BusinessOrder order : orderList) {
//                        *有提成商品计算奖励*
                    SaleReward saleReward = new SaleReward();

                    List<OrderSku> orderSkuList = order.getSkuList();

                    for (OrderSku sku : orderSkuList) {

                        if (commission.getSpuId() == sku.getSpuId()) {
                            number += sku.getNum();//*完成量增加*
                            if (commission.getUsers() > 0) {
                                //个人
                                royaltyPersonal += commission.getUsers() * number; //奖励 = 提成 * 成交数量

                            }
                            if (commission.getPrice() > 0) {
                                // 团队
                                royaltyTeam += commission.getPrice() * number;
                            }

                        }
                        ProductSKU load = productSKUDao.load(sku.getSkuId());
                        if(load!=null){
                            saleReward.setProductName(load.getCode());
                        }
                        ProductSPU productSPU = productSPUDao.load(sku.getSpuId());
                        if(productSPU!=null){
                            ProductSeries productSeries = productSeriesDao.load(productSPU.getSid());
                            ProductBrand productBrand = productBrandDao.load(productSPU.getBid());
                            saleReward.setSName(productSeries!=null?productSeries.getName():"");
                            saleReward.setBName(productBrand!=null?productBrand.getName():"");
                        }

                    }

                    saleReward.setRoyaltyPersonal(royaltyPersonal);//*个人奖励*
                    saleReward.setRoyaltyTeam(royaltyTeam);//*团队奖励*
                    saleRewards.add(saleReward);

                }


                totalP += royaltyPersonal;
                totalT += royaltyTeam;
            }
        }
        map.put("saleRewards", saleRewards);
        map.put("totalT", totalT);
        map.put("totalP", totalP);
        return map;
    }

//    /**以商品为单位进行遍历**/
//    for(Commission commission:commissionList){
//        List<SaleRewardInFo> saleRewardInFos = Lists.newArrayList();
//
//
//        int number = 0;//完成量
//        int royaltyPersonal = 0;//个人奖励
//        int rewardUser = 0;//个人提成
//        int rewardTeam = 0;//团队提成
//        int royaltyTeam = 0;//团队奖励
//
//        /**获得提成用户ids**/
//        Map<Long,Object> users = commission.getUsers();
//        for(Map.Entry<Long,Object> entry : users.entrySet()){
//
//            SaleRewardInFo saleRewardInFo = new SaleRewardInFo();
//
//            /**个人提成**/
//            if(commission.getType() == Commission.type_personal){
//                rewardUser = (int) entry.getValue();
//            }
//            /**团队提成**/
//            if(commission.getType() == Commission.type_team){
//                rewardTeam = (int) entry.getValue();
//            }
//            User user = userDao.load(entry.getKey());
//            if(user != null){
//                saleRewardInFo.setName(user.getName());
//                saleRewardInFo.setUid(user.getId());
//            }
//            /**根据uid查询订单**/
//            List<Order> orderList = orderDao.getUserFinishOrders(entry.getKey(),Order.makestatus_qu_yes);
//
//
//            for(Order order:orderList){
//                /**有提成商品计算奖励**/
//                List<OrderSku> orderSkuList = order.getSkuids();
//                for(OrderSku sku:orderSkuList){
//                    if(commission.getSpuId() == sku.getSpuid()){
//
//                        number += sku.getNum();/**完成量增加**/
//
//                    }
//                }
//            }
//            /**计算奖励**/
//            royaltyPersonal += sku.getNum() * rewardUser;
//        }
//    }

    @Override
    public CalculateOrder calculateOrders(long subId, long startTime, long endTime) throws Exception {
        String sql = "SELECT  *  FROM `order`   where  1=1  ";

        {
            sql = sql + " and `status` = " + Order.status_pay;
        }
        if (subId > 0) {
            sql = sql + " and `subid` = " + subId;
        }
        if (startTime > 0) {
            sql = sql + " and `ctime` >" + startTime;
        }
        if (endTime > 0) {
            sql = sql + " and `ctime` <" + endTime;
        }
        sql = sql + " order  by ctime desc";
        List<Order> orderList = this.jdbcTemplate.query(sql, rowMapper);
        Map<String, Integer> map = calculateSale(orderList);
        CalculateOrder order = new CalculateOrder();
        order.setSale(map.get("sale") / 100.0);
        order.setAli(map.get("ali") / 100.0);
        order.setWx(map.get("wx") / 100.0);
        order.setCash(map.get("cash") / 100.0);
        order.setOther(map.get("other") / 100.0);
        order.setNum(orderList.size());
        return order;
    }

    @Override
    public Map<String, Integer> calculateSale(List<Order> orders) {
        Map<String, Integer> map = Maps.newHashMap();
        int sale = 0;
        int ali = 0;
        int wx = 0;
        int cash = 0;
        int other = 0;
/*        for(Order order:orders) {
            for (Integer type : order.getPayTypes()) {
                if (type == Order.pay_type_ali) {
                    ali += order.getAliPrice();
                }
                if (type == Order.pay_type_wx) {
                    wx += order.getWxPrice();
                }
                if (type == Order.pay_type_cash) {
                    cash += order.getCashPrice();
                }
                if (type == Order.pay_type_stored) {
                    other += order.getStoredPrice();
                }
            }
        }*/
        sale = ali + wx + cash + other;
        map.put("ali", ali);
        map.put("wx", wx);
        map.put("cash", cash);
        map.put("other", other);
        map.put("sale", sale);
        return map;
    }

    @Override
    public Pager saleRewardApp(Pager pager,final long subid) throws Exception {
        return new PagerRequestService<Commission>(pager,0){

            @Override
            public List<Commission> step1GetPageResult(String cursor, int size) throws Exception {
                List<Commission> commissionList = commissionDao.getAllList(subid,Double.parseDouble(cursor),size);
                return commissionList;
            }

            @Override
            public int step2GetTotalCount() throws Exception {
                return 0;
            }

            @Override
            public List<Commission> step3FilterResult(List<Commission> list, PagerSession pagerSession) throws Exception {
                return list;
            }

            @Override
            public List<?> step4TransformData(List<Commission> list, PagerSession pagerSession) throws Exception {
                List<SaleReward> saleRewards = Lists.newArrayList();
                int totalP = 0;
                int totalT = 0;
                if (list.size() > 0) {
                    for (Commission commission : list) {
                        int number = 0;
                        int royaltyPersonal = 0;
                        int royaltyTeam = 0;
//                    *根据uid查询订单*
                        List<BusinessOrder> orderList = businessOrderService.getAllBySubid(commission.getSubId(),BusinessOrder.status_pay ,BusinessOrder.makeStatus_qu_yes);
                        for (BusinessOrder order : orderList) {
//                        *有提成商品计算奖励*
                            SaleReward saleReward = new SaleReward();
                            saleReward.setCommission(commission);
                            List<OrderSku> orderSkuList = order.getSkuList();
                            for (OrderSku sku : orderSkuList) {
                                if (commission.getSpuId() == sku.getSpuId()) {
                                    number = sku.getNum();//*完成量增加*
                                    if (commission.getUsers() > 0) {
                                        //个人
                                        royaltyPersonal += commission.getUsers() * number; //奖励 = 提成 * 成交数量
                                    }
                                    if (commission.getPrice() > 0) {
                                        // 团队
                                        royaltyTeam += commission.getPrice() * number;
                                    }
                                }
                                ProductSKU load = productSKUDao.load(sku.getSkuId());
                                if(load!=null){
                                    saleReward.setProductName(load.getCode());
                                }
                                ProductSPU productSPU = productSPUDao.load(sku.getSpuId());
                                if(productSPU!=null){
                                    ProductSeries productSeries = productSeriesDao.load(productSPU.getSid());
                                    ProductBrand productBrand = productBrandDao.load(productSPU.getBid());
                                    saleReward.setSName(productSeries!=null?productSeries.getName():"");
                                    saleReward.setBName(productBrand!=null?productBrand.getName():"");
                                }
                                saleReward.setNumber(number);
                                saleReward.setRoyaltyPersonal(royaltyPersonal);//*个人奖励*
                                saleReward.setRoyaltyTeam(royaltyTeam);//*团队奖励*
                                if(saleReward.getNumber()>0&&saleReward.getRoyaltyPersonal()>0&&saleReward.getRoyaltyTeam()>0){
                                    saleRewards.add(saleReward);
                                }
                                totalP += royaltyPersonal;
                                totalT += royaltyTeam;
                            }
                        }
                    }
                }
                return saleRewards;
            }
        }.getPager();







//
    }

    private List<ClientOrder> transformClientTem(List<Order> list) {
        List<ClientOrder> clientOrderList = new ArrayList<>();
       /* for(Order order:list){
            ClientOrder clientOrder=new ClientOrder(order);
            if(order.getCouponid()>0){
                //促销name
                MarketingCoupon load = marketingCouponDao.load(order.getMachiningid());
                if(load!=null){
                    clientOrder.setCouponName(load.getTitle());
                }
            }
            if(order.getPersonnelid()>0){
                // 销售员name
                User load = userDao.load(order.getPersonnelid());
                if(load!=null){
                    clientOrder.setPersonnelName(load.getName());
                }
            }
            if(order.getMachiningid()>0){
                // 加工师name
                User load = userDao.load(order.getMachiningid());
                if(load!=null){
                    clientOrder.setMachiningName(load.getName());
                }
            }
            if(order.getUid()>0){
                // 顾客name
                User load = userDao.load(order.getUid());
                if(load!=null){
                    clientOrder.setUName(load.getName());
                    clientOrder.setUPhone(load.getPhone());
                }
            }
            List<OrderSku> skuids = order.getSkuids();
            List<OrderSku> Clientskuids = Lists.newArrayList();
            for(OrderSku sku:skuids){
                // sku name
                OrderSku orderSku=sku;
                ProductSKU load = productSKUDao.load(sku.getSkuid());
                if(load!=null){
                    orderSku.setName(load.getName());
                }
                Clientskuids.add(orderSku);
            }
            clientOrder.setSkuids(Clientskuids);
            clientOrderList.add(clientOrder);
        }*/
        return clientOrderList;
    }

    private List<ClientOrder> transformClient(List<Order> orderList) throws Exception {
        List<ClientOrder> clientOrderList = new ArrayList<>();
 /*       for (Order order : orderList) {
           double totalPrice= order.getTotalPrice()*0.01;
            ClientOrder clientOrder = new ClientOrder(order);

            Map<String,Integer> map = calculateSale(Lists.newArrayList(order));
            clientOrder.setPriceYuan(map.get("sale")*0.01);
            clientOrder.setTotalPriceYuan(totalPrice);
            User user = userDao.load(order.getUid());
            if (user != null) {
                clientOrder.setUName(user.getName());
                clientOrder.setUPhone(user.getPhone());
            }
            Subordinate subordinate = subordinateDao.load(order.getSubid());
            clientOrder.setSubName(subordinate.getName());
            User passportUser = userDao.load(order.getPersonnelid());
            if (passportUser != null) {
                clientOrder.setPersonnelName(passportUser.getName());
            }

            MarketingCoupon load = marketingCouponDao.load(order.getCouponid());
            if (load != null) {
                if(load.getDescSubtractType()==MarketingCoupon.desc_subtract_type_money){
                    clientOrder.setDescSubtract(load.getDescSubtract()*0.01);
                }else{
                    clientOrder.setDescSubtract(load.getDescSubtract());
                }

                clientOrder.setDescSubtractType(load.getDescSubtractType());
            }
            clientOrder.setAsCount(afterSaleDetailDao.getCount(order.getId()));//售后次数
            clientOrderList.add(clientOrder);
        }*/


        return clientOrderList;
    }

    private ClientOrder transformClient(Order order) throws Exception {
        ClientOrder clientOrder = new ClientOrder(order);
/*
        OptometryInfo optometryInfo = optometryInfoDao.load(order.getOiId());
        Subordinate subordinate = subordinateDao.load(order.getSubid());
        if(subordinate != null) {
            clientOrder.setSubName(subordinate.getName());
            clientOrder.setThreePolicy(subordinate.getThreePolicy());
            clientOrder.setPhone(subordinate.getPhone());
            clientOrder.setAddress(subordinate.getAddress());
        }

        User user = userDao.load(order.getUid());
        if(user != null) {
            clientOrder.setUName(user.getName());
            clientOrder.setUPhone(user.getPhone());
        }
        User machiningUser = userDao.load(order.getMachiningid());
        if(machiningUser!=null) clientOrder.setMachiningName(machiningUser.getName());
        if (optometryInfo != null) {
            User oiUser = userDao.load(optometryInfo.getOptUid());
            if (oiUser != null) clientOrder.setOiName(oiUser.getName());
        }
        List<OptometryInfo> optometryInfos = optometryInfoDao.getList(order.getUid(),10);
        clientOrder.setOptometryInfos(optometryInfos);
        int count = afterSaleDetailDao.getCount(order.getId());
        clientOrder.setAsCount(count);
        if(order.getOiId()>0){
            // 验光信息
            OptometryInfo load = optometryInfoDao.load(order.getOiId());
            if(load!=null) {
                clientOrder.setInfo(load);
            }
        }
        if(order.getCouponid()>0){
            //优惠券信息
            MarketingCoupon marketingCoupon = marketingCouponDao.load(order.getCouponid());
            if(marketingCoupon!=null) {
                clientOrder.setCouponName(marketingCoupon.getTitle());
                if (marketingCoupon.getDescSubtractType() == MarketingCoupon.desc_subtract_type_money) {
                    clientOrder.setDescSubtract(marketingCoupon.getDescSubtract() / 100.0);
                } else {
                    clientOrder.setDescSubtract(marketingCoupon.getDescSubtract());
                }
            }
        }*/
        return clientOrder;
    }


    private void waitWxBarcodeOrderRes(File file, PayPassport payPassport, String outTradeNo, Order order) throws Exception {
        boolean reverse = true;
        for (int i = 1; i <= 3; i++) {
            Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
            String queryRes = wxOrderQuery(payPassport, outTradeNo);
            order.setDetail(queryRes);
            Map<String, String> map = PayUtils.xmlStrToMap(queryRes);
            if (map.get("return_code").equals("SUCCESS") && map.get("result_code").equals("SUCCESS") && map.get("trade_state").equals("SUCCESS")) {
                order.setStatus(Order.status_pay);
                order.setOrderNo(map.get("transaction_id"));
                long time_end = DateUtils.parseDate(map.get("time_end"), "yyyyMMddHHmmss").getTime();
                order.setPayTime(time_end);
                reverse = false;
                break;
            }
        }
        if (reverse) {
            String reverseRes = wxOrderReverse(file, payPassport, outTradeNo);
        }
    }


    private String wxOrderQuery(PayPassport payPassport, String outTradeNo) throws Exception {
        String wxAppid = payPassport.getWxAppid();
        String wxMerchantId = payPassport.getWxMerchantId();
        String wxApiKey = payPassport.getWxApiKey();
        Map<String, String> sParaTemp = Maps.newHashMap();
        sParaTemp.put("appid", wxAppid);
        sParaTemp.put("mch_id", wxMerchantId);
        sParaTemp.put("out_trade_no", outTradeNo);
        sParaTemp.put("nonce_str", RandomStringUtils.randomAlphanumeric(16));// 随机字符串
        String mysign = PayUtils.buildSign(sParaTemp, wxApiKey);
        sParaTemp.put("sign", URLEncoder.encode(mysign, Constant.defaultCharset));
        String xml = PayUtils.createXmlString(sParaTemp);
        String sendRes = PayUtils.send(this.wxpayQueryUrl, xml);
        return sendRes;
    }

    private String wxOrderReverse(File file, PayPassport payPassport, String outTradeNo) throws Exception {
        String wxAppid = payPassport.getWxAppid();
        String wxMerchantId = payPassport.getWxMerchantId();
        String wxApiKey = payPassport.getWxApiKey();
        Map<String, String> sParaTemp = Maps.newHashMap();
        sParaTemp.put("appid", wxAppid);
        sParaTemp.put("mch_id", wxMerchantId);
        sParaTemp.put("out_trade_no", outTradeNo);
        sParaTemp.put("nonce_str", RandomStringUtils.randomAlphanumeric(16));// 随机字符串
        String mysign = PayUtils.buildSign(sParaTemp, wxApiKey);
        sParaTemp.put("sign", URLEncoder.encode(mysign, Constant.defaultCharset));
        String xml = PayUtils.createXmlString(sParaTemp);
        String sendRes = PayUtils.sendPKCS(file, wxMerchantId, wxpayReverseUrl, xml);
        return sendRes;
    }


}
