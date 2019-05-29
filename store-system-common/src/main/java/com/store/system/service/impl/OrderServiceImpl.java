package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.Uninterruptibles;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.bean.OrderExpireUnit;
import com.store.system.client.ClientOrder;
import com.store.system.client.ClientProductSKU;
import com.store.system.client.ClientSubordinate;
import com.store.system.dao.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.OrderService;
import com.store.system.service.ext.OrderPayService;
import com.store.system.util.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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

    @Resource
    private PayPassportDao payPassportDao;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private OrderDao orderDao;
    @Resource
    private ProductSKUDao productSKUDao;
    @Resource
    private UserDao userDao;
    @Resource
    private SubordinateDao subordinateDao;
    @Resource
    private MarketingCouponDao marketingCouponDao;

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

    private Order createAliOrder(long passportId, int payMode, int type, String typeInfo, String title, String desc,
                                 double price, OrderExpireUnit expireUnit, int expireNum) throws Exception {
        if(price == 0) throw new StoreSystemException("price is zero!");
        if(payMode != Order.pay_mode_barcode && (expireUnit.getId() == 0 || expireNum == 0)) throw new StoreSystemException("expire is error!");
        PayPassport payPassport = payPassportDao.load(passportId);
        if(null == payPassport) throw new StoreSystemException("passport is null!");
        Order order = new Order();
        long gmt = NumberUtils.toLong(gmtFormat.format(new Date()));
        order.setGmt(gmt);
        order.setPassportId(passportId);
        order.setPayType(Order.pay_type_ali);
        order.setPayMode(payMode);
        order.setType(type);
        order.setTypeInfo(typeInfo);
        order.setTitle(title);
        order.setDesc(desc);
        order.setPrice(price);
        order.setStatus(Order.status_no_pay);
        if(payMode != Order.pay_mode_barcode) {
            order.setExpireUnitId(expireUnit.getId());
            order.setExpireNum(expireNum);
            long expireTime = PayUtils.getExpireTime(System.currentTimeMillis(), expireUnit, expireNum);
            order.setExpireTime(expireTime);
        }
        return orderDao.insert(order);
    }

    @Override
    public boolean handleAliBarcodeOrder(long passportId, String authCode, int type, String typeInfo, String title, String desc, double price) throws Exception {
        Order order = createAliOrder(passportId, Order.pay_mode_barcode, type, typeInfo, title, desc, price, OrderExpireUnit.nvl, 0);
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
        if(code.equals("10000")) {
            res = true;
            order.setStatus(Order.status_pay);
        }
        if(order.getStatus() == Order.status_pay) {
            orderPayService.successHandleBusiness(order.getPayType(), type, typeInfo);
        }
        orderDao.update(order);
        return res;
    }


    private Map<String, String> aliOrderPayParam(long oid, int payMode, String authCode) throws Exception {
        if(payMode == Order.pay_mode_barcode && StringUtils.isBlank(authCode)) throw new StoreSystemException("authCode is null!");
        Order order = orderDao.load(oid);
        if(null == order) throw new StoreSystemException("order is null!");
        if(order.getPayType() != Order.pay_type_ali) throw new StoreSystemException("order is not ali!");
        if(order.getPayMode() != payMode) throw new StoreSystemException("order pay mode is error!");
        if(order.getStatus() != Order.status_no_pay) throw new StoreSystemException("order status is error!");
        Date now = new Date();
        if(payMode != Order.pay_mode_barcode && now.getTime() > order.getExpireTime()) throw new StoreSystemException("order is expire!");
        long passportId = order.getPassportId();
        PayPassport payPassport = payPassportDao.load(passportId);
        if(null == payPassport) throw new StoreSystemException("passport is null!");
        String aliAppid = payPassport.getAliAppid();
        String aliPrivateKey = payPassport.getAliPrivateKey();
        String aliOrderNotifyUrl = payPassport.getAliOrderNotifyUrl();
        if(StringUtils.isBlank(aliAppid)) throw new StoreSystemException("aliAppid is null!");
        if(payMode != Order.pay_mode_barcode && StringUtils.isBlank(aliOrderNotifyUrl)) throw new StoreSystemException("aliOrderNotifyUrl is null!");
        if(StringUtils.isBlank(aliPrivateKey)) throw new StoreSystemException("aliPrivateKey is null!");
        String itBPay = null;
        if(payMode != Order.pay_mode_barcode) {
            OrderExpireUnit expireUnit = OrderExpireUnit.get(order.getExpireUnitId());
            if(expireUnit == OrderExpireUnit.inday) itBPay = expireUnit.getSign();
            else itBPay = order.getExpireNum() + expireUnit.getSign();
        }
        String method = null;
        String product_code = null;
        if(payMode == Order.pay_mode_app) {
            product_code = "QUICK_MSECURITY_PAY";
            method = "alipay.trade.app.pay";
        } else if(payMode == Order.pay_mode_wap) {
            product_code = "QUICK_WAP_WAY";
            method = "alipay.trade.wap.pay";
        } else if(payMode == Order.pay_mode_barcode) {
            product_code = "FACE_TO_FACE_PAYMENT";
            method = "alipay.trade.pay";
        }
        Map<String, String> bizContentMap = Maps.newLinkedHashMap();
        bizContentMap.put("body", StringUtils.isNotBlank(order.getDesc()) ? order.getDesc() : "");
        bizContentMap.put("subject", StringUtils.isNotBlank(order.getTitle()) ? order.getTitle() : "");
        bizContentMap.put("out_trade_no", PayUtils.getOutTradeNo(oid, order.getGmt()));
        if(payMode == Order.pay_mode_barcode) bizContentMap.put("scene", "bar_code");
        if(payMode == Order.pay_mode_barcode) bizContentMap.put("auth_code", authCode);
        if(StringUtils.isNotBlank(itBPay)) bizContentMap.put("timeout_express", String.valueOf(itBPay));
        bizContentMap.put("total_amount", String.valueOf(order.getPrice()));
        bizContentMap.put("product_code", product_code);
        String biz_content = JsonUtils.toJson(bizContentMap);
        Map<String, String> sParaTemp = Maps.newHashMap();
        sParaTemp.put("app_id", aliAppid); //
        sParaTemp.put("method", method);
        sParaTemp.put("charset", Constant.defaultCharset);
        sParaTemp.put("sign_type", "RSA2");
        sParaTemp.put("timestamp", DateUtils.simpleDateFormat(now, "yyyy-MM-dd HH:mm:ss"));
        sParaTemp.put("version", "1.0");
        if(payMode != Order.pay_mode_barcode) sParaTemp.put("notify_url", aliOrderNotifyUrl);
        if(payMode == Order.pay_mode_wap && StringUtils.isNotBlank(payPassport.getAliWapOrderReturnUrl())) {
            sParaTemp.put("return_url", payPassport.getAliWapOrderReturnUrl());
        }
        sParaTemp.put("biz_content", biz_content);
        String prestr = PayUtils.createLinkString(sParaTemp);
        String mysign = RSA.sign256(prestr, aliPrivateKey, Constant.defaultCharset);
        sParaTemp.put("sign", mysign);
        return sParaTemp;
    }

    private Order createWxOrder(long passportId, int payMode, int type, String typeInfo, String title, String desc,
                                double price, OrderExpireUnit expireUnit, int expireNum) throws Exception {
        if(price == 0) throw new StoreSystemException("price is zero!");
        if(payMode != Order.pay_mode_barcode && (expireUnit.getId() == 0 || expireNum == 0))throw new StoreSystemException("expire is error!");
        PayPassport payPassport = payPassportDao.load(passportId);
        if(null == payPassport) throw new StoreSystemException("passport is null!");
        long gmt = NumberUtils.toLong(gmtFormat.format(new Date()));
        Order order = new Order();
        order.setPayType(Order.pay_type_wx);
        order.setPayMode(payMode);
        order.setGmt(gmt);
        order.setPassportId(passportId);
        order.setPrice(price);
        order.setType(type);
        order.setStatus(Order.status_no_pay);
        order.setTypeInfo(typeInfo);
        order.setTitle(title);
        order.setDesc(desc);
        if(payMode != Order.pay_mode_barcode) {
            order.setExpireUnitId(expireUnit.getId());
            order.setExpireNum(expireNum);
            long expireTime = PayUtils.getExpireTime(System.currentTimeMillis(), expireUnit, expireNum);
            order.setExpireTime(expireTime);
        }
        return orderDao.insert(order);
    }

    private boolean validSign(Map<String, String> params, String wxpayApiKey) throws Exception  {
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

    public static void main(String[] args) throws  Exception {
        Map<String, String> map  =  Maps.newHashMap();

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
        map.put("appid","wx2feae2c615187670");
        map.put("mch_id","1510090681");
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
    public boolean handleWxBarcodeOrder(HttpServletRequest request, long passportId, String authCode, int type, String typeInfo, String title, String desc, double price, String ip) throws Exception {
        Order order = createWxOrder(passportId, Order.pay_mode_barcode, type, typeInfo, title, desc, price, OrderExpireUnit.nvl, 0);
        if(StringUtils.isBlank(authCode)) throw new StoreSystemException("authCode is null!");
        PayPassport payPassport = payPassportDao.load(passportId);
        if(null == payPassport) throw new StoreSystemException("passport is null!");
        String wxAppid = payPassport.getWxAppid();
        String wxMerchantId = payPassport.getWxMerchantId();
        String wxApiKey = payPassport.getWxApiKey();
        String wxPkcs12CertificateName = payPassport.getWxPkcs12CertificateName();
        if(StringUtils.isBlank(wxAppid)) throw new StoreSystemException("wxAppid is null!");
        if(StringUtils.isBlank(wxMerchantId)) throw new StoreSystemException("wxMerchantId is null!");
        if(StringUtils.isBlank(wxApiKey)) throw new StoreSystemException("wxApiKey is null!");
        if(StringUtils.isBlank(wxPkcs12CertificateName)) throw new StoreSystemException("wxPkcs12CertificateName is null!");
        int totalFee = (int) ArithUtils.mul(order.getPrice() , 100);
        Map<String, String> sParaTemp = Maps.newHashMap();
        sParaTemp.put("appid", wxAppid);// 应用ID
        sParaTemp.put("mch_id", wxMerchantId);// 商户号
        sParaTemp.put("nonce_str", RandomStringUtils.randomAlphanumeric(16));// 随机字符串
        sParaTemp.put("body", StringUtils.isNotBlank(order.getTitle()) ? order.getTitle() : "");// 商品描述String(128)
        sParaTemp.put("detail", StringUtils.isNotBlank(order.getDesc()) ? order.getDesc() : "");// 商品详情String(8192)
        sParaTemp.put("out_trade_no", PayUtils.getOutTradeNo(order.getId(), order.getGmt()));// 商户订单号
        sParaTemp.put("total_fee", String.valueOf(totalFee));// total_fee,Int,单位为分
        sParaTemp.put("spbill_create_ip", ip);
        sParaTemp.put("auth_code", authCode);
        String mysign = PayUtils.buildSign(sParaTemp, wxApiKey);
        sParaTemp.put("sign", URLEncoder.encode(mysign, Constant.defaultCharset));
        String xml = PayUtils.createXmlString(sParaTemp);
        String sendRes = PayUtils.send(wxpayBarcodeUrl, xml);
        Map<String, String> resMap = PayUtils.xmlStrToMap(sendRes);
        String code = resMap.get("return_code");
        boolean res = false;
        if(code.equals("SUCCESS")) {
            String result_code = resMap.get("result_code");
            if("SUCCESS".equals(result_code)) {
                order.setStatus(Order.status_pay);
                order.setDetail(sendRes);
            } else {
                String path = request.getServletContext().getRealPath("");
                File file = new File(path);
                path = file.getParent();
                file = new File(path + "/" + wxPkcs12CertificateName);
                String out_trade_no = PayUtils.getOutTradeNo(order.getId(), order.getGmt());
                waitWxBarcodeOrderRes(file, payPassport, out_trade_no, order);
            }
        }
        if(order.getStatus() == Order.status_pay) {
            res = true;
            orderPayService.successHandleBusiness(order.getPayType(), type, typeInfo);
        }
        orderDao.update(order);
        return res;
    }

    @Override
    public Pager getAll(Pager pager, long startTime, long endTime, long personnelid, int status,long uid,String name,int makeStatus,long subid) throws Exception {
        String sql = "SELECT  *  FROM `order`   where  1=1 ";
        String sqlCount = "SELECT  COUNT(*)  FROM `order` where 1=1";
        String limit = "  limit %d , %d ";
        if (subid > 0) {
            sql = sql + " and `subid` = " + subid;
            sqlCount = sqlCount + " and `subid` = " + subid;
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
        } if (uid > 0) {
            sql = sql + " and `uid` =" + uid;
            sqlCount = sqlCount + " and `uid` =" + uid;
        }if (StringUtils.isNotBlank(name)) {
            sql = sql + " and  `uid` in (select id from `user`  where name like '%"+name+"%')";
            sqlCount = sqlCount + " and  uid in (select id from `user`  where name like '%"+name+"%')";
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
    public Order saveOrder(Order order) throws Exception {
        return  orderDao.insert(order);
    }

    @Override
    public List<ClientOrder> getAllBySubid(long subid) throws Exception {
        List<Order> list= orderDao.getAllBySubid(subid,Order.status_pay,Order.makestatus_qu_yes);
        return  transformClient(list);
    }

    @Override
    public Order countPrice(Order order) throws Exception {
        Order orderPrice=new Order();
        double totalPrice=0.0;
        //获取skuid 去拿到金额。
        List<OrderSku> skuids = order.getSkuids();
        for(OrderSku sku:skuids){
            ProductSKU productSKU = productSKUDao.load(sku.getSkuid());
            totalPrice+=sku.getNum()*productSKU.getRetailPrice();
        }
        orderPrice.setTotalPrice(totalPrice);
        long couponid = order.getCouponid();
        if(couponid>0){
            MarketingCoupon marketingCoupon = marketingCouponDao.load(couponid);
            if(marketingCoupon.getDescSubtractType() == MarketingCoupon.desc_subtract_type_money) {
                orderPrice.setDicountPrice( totalPrice-marketingCoupon.getDescSubtract());
            }
            if(marketingCoupon.getDescSubtractType() == MarketingCoupon.desc_subtract_type_rate) {
                orderPrice.setDicountPrice( totalPrice-totalPrice*marketingCoupon.getDescSubtract());
            }
        }
        return orderPrice;
    }

    @Override
    public List<ClientOrder> getTemporaryOrder(long subid) throws Exception {
        List<Order> list=   orderDao.getTemporaryOrder(subid,Order.makestatus_temporary);

        return  transformClientTem(list);
    }

    private List<ClientOrder> transformClientTem(List<Order> list) {
        List<ClientOrder> clientOrderList = new ArrayList<>();
        for(Order order:list){
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
        }
        return clientOrderList;
    }

    private List<ClientOrder> transformClient(List<Order> orderList) throws Exception {
        List<ClientOrder> clientOrderList = new ArrayList<>();
        for (Order order : orderList) {
           double totalPrice= order.getTotalPrice()*0.01;
            order.setTotalPrice(totalPrice);
            double price= order.getPrice()*0.01;
            order.setPrice(price);
            ClientOrder clientOrder = new ClientOrder(order);
            User user = userDao.load(order.getUid());
            if (user != null) {
                clientOrder.setUName(user.getName());
                clientOrder.setUPhone(user.getPhone());
            }
            Subordinate subordinate = subordinateDao.load(order.getSubid());
            clientOrder.setSubName(subordinate.getName());
            User passportUser = userDao.load(order.getPassportId());
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
            clientOrderList.add(clientOrder);
        }


        return clientOrderList;
    }

    private void waitWxBarcodeOrderRes(File file, PayPassport payPassport, String outTradeNo, Order order) throws Exception {
        boolean reverse = true;
        for(int i = 1; i <= 3; i++) {
            Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
            String queryRes = wxOrderQuery(payPassport, outTradeNo);
            order.setDetail(queryRes);
            Map<String, String> map = PayUtils.xmlStrToMap(queryRes);
            if(map.get("return_code").equals("SUCCESS") && map.get("result_code").equals("SUCCESS") && map.get("trade_state").equals("SUCCESS")) {
                order.setStatus(Order.status_pay);
                reverse = false;
                break;
            }
        }
        if(reverse) {
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
