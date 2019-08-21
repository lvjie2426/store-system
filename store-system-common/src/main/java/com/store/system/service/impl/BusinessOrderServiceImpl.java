package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.quakoo.ext.RowMapperHelp;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.store.system.bean.CalculateOrder;
import com.store.system.client.*;
import com.store.system.dao.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.*;
import com.store.system.util.ArithUtils;
import com.store.system.util.FilterStringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName BusinessOrderServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/7/23 17:20
 * @Version 1.0
 **/
@Service
public class BusinessOrderServiceImpl implements BusinessOrderService {

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private BusinessOrderDao businessOrderDao;
    @Resource
    private PayInfoService payInfoService;
    @Resource
    private UserDao userDao;
    @Resource
    private SubordinateDao subordinateDao;
    @Resource
    private OptometryInfoDao optometryInfoDao;
    @Resource
    private AfterSaleDetailDao afterSaleDetailDao;
    @Resource
    private AfterSaleDetailService afterSaleDetailService;
    @Resource
    private ProductSKUDao productSKUDao;
    @Resource
    private ProductSPUDao productSPUDao;
    @Resource
    private UserGradeService userGradeService;
    @Resource
    private MarketingCouponService marketingCouponService;
    @Resource
    private FinanceLogService financeLogService;

    private RowMapperHelp<BusinessOrder> rowMapper = new RowMapperHelp<>(BusinessOrder.class);
    private TransformMapUtils skuMapUtils = new TransformMapUtils(ProductSKU.class);
    private TransformMapUtils spuMapUtils = new TransformMapUtils(ProductSPU.class);

    @Override
    public Pager getAllList(Pager pager, long startTime, long endTime, long staffId, int status,
                            long uid, String name, int makeStatus, long subId) throws Exception {
        String sql = "SELECT  *  FROM `business_order` where  1=1 ";
        String sqlCount = "SELECT  COUNT(*)  FROM `business_order` where 1=1";
        String limit = "  limit %d , %d ";
        if (subId > 0) {
            sql = sql + " and `subId` = " + subId;
            sqlCount = sqlCount + " and `subId` = " + subId;
        }
        if (makeStatus > 0) {
            sql = sql + " and `makeStatus` = " + makeStatus;
            sqlCount = sqlCount + " and `makeStatus` = " + makeStatus;
        }
        if (staffId > 0) {
            sql = sql + " and `staffId` = " + staffId;
            sqlCount = sqlCount + " and `staffId` = " + staffId;
        }
        if (startTime > 0) {
            sql = sql + " and `ctime` >" + startTime;
            sqlCount = sqlCount + " and `ctime` >" + startTime;
        }
        if (endTime > 0) {
            sql = sql + " and `ctime` <" + endTime;
            sqlCount = sqlCount + " and `ctime` <" + endTime;
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
        List<BusinessOrder> list = this.jdbcTemplate.query(sql, rowMapper);
        count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);

        pager.setData(transformClient(list));
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public Pager getUnfinishedList(Pager pager, long startTime, long endTime, long staffId, int status,
                                   long uid, String name, long subId, int makeStatus) throws Exception {
        String sql = "SELECT  *  FROM `business_order` where  1=1  ";
        String sqlCount = "SELECT  COUNT(*)  FROM `business_order` where 1=1  ";
        String limit = "  limit %d , %d ";

        if (subId > 0) {
            sql = sql + " and `subId` = " + subId;
            sqlCount = sqlCount + " and `subId` = " + subId;
        }
        if (makeStatus > 0) {
            sql = sql + " and `makeStatus` = " + makeStatus;
            sqlCount = sqlCount + " and `makeStatus` = " + makeStatus;
        } else {
            sql = sql + " and (`makeStatus` = 1 OR `makeStatus` = 2 OR `makeStatus` = 3 ) ";
            sqlCount = sqlCount + " and  (`makeStatus` = 1 OR `makeStatus` = 2 OR `makeStatus` = 3) ";
        }
        if (staffId > 0) {
            sql = sql + " and `staffId` = " + staffId;
            sqlCount = sqlCount + " and `staffId` = " + staffId;
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
        List<BusinessOrder> list = this.jdbcTemplate.query(sql, rowMapper);
        count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);

        pager.setData(transformClient(list));
        pager.setTotalCount(count);
        return pager;
    }


    @Override
    public Pager getBackPager(Pager pager, long subId, String name, String phone, String orderNo) throws Exception {
        String sql = "SELECT o.* FROM `business_order` as o LEFT JOIN `user` as u ON o.uid=u.id WHERE 1=1 ";
        String sqlCount = "SELECT COUNT(*) FROM `business_order` as o LEFT JOIN `user` as u ON o.uid=u.id WHERE 1=1 ";
        String limit = "  limit %d , %d ";

        {
            sql = sql + " and o.`status` = " + BusinessOrder.status_pay;
            sqlCount = sqlCount + " and o.`status` = " + BusinessOrder.status_pay;
        }
        if(subId>0){
            sql = sql + " and o.`subId` = " + subId;
            sqlCount = sqlCount + " and o.`subId` = " + subId;
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
        List<BusinessOrder> businessOrders = null;
        int count=0;
        List<Object> objects=new ArrayList<>();
        if(StringUtils.isNotBlank(name)){objects.add("%"+name+"%");}
        if(StringUtils.isNotBlank(phone)){objects.add("%"+phone+"%");}
        if(StringUtils.isNotBlank(orderNo)){objects.add("%"+orderNo+"%");}
        if(objects.size()>0) {
            Object[] args = new Object[objects.size()];
            objects.toArray(args);
            businessOrders = jdbcTemplate.query(sql, new HyperspaceBeanPropertyRowMapper<BusinessOrder>(BusinessOrder.class),args);
            count = this.jdbcTemplate.queryForObject(sqlCount,Integer.class);
        }else{
            businessOrders = jdbcTemplate.query(sql, new HyperspaceBeanPropertyRowMapper<BusinessOrder>(BusinessOrder.class));
            count = this.jdbcTemplate.queryForObject(sqlCount,Integer.class);
        }
        pager.setData(transformClient(businessOrders));
        pager.setTotalCount(count);
        return pager;
    }


    @Override
    public ClientBusinessOrder add(BusinessOrder businessOrder) throws Exception {
        return transformClient(businessOrderDao.insert(businessOrder));
    }

    @Override
    public ClientBusinessOrder load(long id) throws Exception {
        return transformClient(businessOrderDao.load(id));
    }

    @Override
    public List<ClientBusinessOrder> getAllList(long subId) throws Exception {
        List<BusinessOrder> list = businessOrderDao.getAllList(subId, BusinessOrder.makeStatus_temporary);
        return transformClient(list);
    }

    @Override
    public List<ClientBusinessOrder> getAllList(long subId, int status, int makeStatus) throws Exception {
        List<BusinessOrder> list = businessOrderDao.getAllList(subId, status, makeStatus);
        return transformClient(list);
    }

    @Override
    public Map<String,Object> loadOrder(long id) throws Exception {
        Map<String,Object> map = Maps.newHashMap();
        ClientBusinessOrder client = transformClient(businessOrderDao.load(id));
        List<ClientAfterSaleDetail> details = afterSaleDetailService.getAllListByOid(id);
        map.put("order",client);
        map.put("afterSaleDetails",details);
        return map;
    }

    @Override
    public boolean update(BusinessOrder businessOrder) throws Exception {
        return businessOrderDao.update(businessOrder);
    }

    @Override
    public boolean updateMakeStatus(long id, int makeStatus)  throws Exception {
        BusinessOrder businessOrder = businessOrderDao.load(id);
        businessOrder.setMakeStatus(makeStatus);
        return businessOrderDao.update(businessOrder);
    }

    private void check(BusinessOrder businessOrder) throws Exception {
        //折扣=10 意为不打折
        if(businessOrder.getDiscount()!=null) {
            boolean flag = FilterStringUtil.checkDiscount(businessOrder.getDiscount());
            if (!flag) throw new StoreSystemException("折扣输入有误！");
        }
        if(businessOrder.getSkuList().size()<=0 && businessOrder.getSurcharges().size()<=0 && businessOrder.getRechargePrice()<=0) {
            throw new StoreSystemException("请选择需要购买的商品或单独收取的附加费用！");
        }
        for(OrderSku sku:businessOrder.getSkuList()){
            if(sku.getSkuId()<=0) throw new StoreSystemException("缺少关键属性SKU！");
            if(sku.getSpuId()<=0) throw new StoreSystemException("缺少关键属性SPU！");
            if(sku.getNum()<=0) throw new StoreSystemException("购买数量输入有误！");
            if(sku.getDiscount()!=null) {
                boolean b = FilterStringUtil.checkDiscount(sku.getDiscount());
                if (!b) throw new StoreSystemException("折扣输入有误！");
            }
        }
    }

    @Override
    public ResultClient currentCalculate(BusinessOrder businessOrder) throws Exception {
        check(businessOrder);
        List<Long> skuIds = Lists.newArrayList();
        List<Long> spuIds = Lists.newArrayList();
//        Map<Long, Integer> inventoryMap = Maps.newHashMap();
        for (OrderSku sku : businessOrder.getSkuList()) {
            skuIds.add(sku.getSkuId());
            spuIds.add(sku.getSpuId());

/*            int inventoryNum = 0;//库存量
            List<InventoryDetail> allDetails = inventoryDetailDao.getAllListBySKU(sku.getSkuId());
            for (InventoryDetail detail : allDetails) {
                if (detail.getSubid() == businessOrder.getSubId()) {
                    inventoryNum += detail.getNum();
                }
            }
            inventoryMap.put(sku.getSkuId(), inventoryNum);*/
        }

        List<ProductSKU> skuList = productSKUDao.load(skuIds);
        List<ProductSPU> spuList = productSPUDao.load(spuIds);
        Map<Long, ProductSKU> skuMap = skuMapUtils.listToMap(skuList, "id");
        Map<Long, ProductSPU> spuMap = spuMapUtils.listToMap(spuList, "id");

        Map<Long,Object> disMap = Maps.newHashMap();
        long userScore=0;
        if(businessOrder.getUid()>0) {
            User user = userDao.load(businessOrder.getUid());
            UserGrade userGrade = userGradeService.load(user.getUserGradeId());
            if(userGrade!=null) {
                disMap = userGrade.getDiscount();
            }
            userScore=user.getScore();
        }

        ClientBusinessOrder client=new ClientBusinessOrder(businessOrder);
        int originalPrice = 0;//原价(分)
        int discountPrice = 0;//折后金额(分)
        int totalPrice = 0;//总金额(分)
        int totalNum = 0;
        int coupon=0;//优惠金额(分)
        int oddsPrice=0;//特惠减免(分)
        int surchargesPrice=0;//附加费(分)

        for(OrderSku sku:businessOrder.getSkuList()){
            long skuId=sku.getSkuId();
            long spuId=sku.getSpuId();
            ProductSPU spu = spuMap.get(spuId);
            long cid=spu.getCid();
            int num=sku.getNum();
            int retailPrice=skuMap.get(skuId).getRetailPrice();
            double discount=10;
            double subtotal=0;
            if (StringUtils.isNotBlank(sku.getDiscount())) {
                discount = Double.parseDouble(sku.getDiscount());
            } else if (StringUtils.isNotBlank(sku.getDiscount()) &&
                    StringUtils.isNotBlank(businessOrder.getDiscount())) {
                discount = Double.parseDouble(sku.getDiscount());
            } else if (StringUtils.isBlank(sku.getDiscount()) && disMap.size() > 0) {
                discount = Double.parseDouble((String) disMap.get(cid));
            } else if (StringUtils.isNotBlank(businessOrder.getDiscount())){
                discount = Double.parseDouble(businessOrder.getDiscount());
            }

            //常规商品
//            if (spu.getType() == ProductSPU.type_common) {
/*                if(num>inventoryMap.get(skuId)) {
                    throw new StoreSystemException("当前购买的商品库存数量不足！");
                }*/
                if (discount > 0) {
                    subtotal = num * retailPrice * (ArithUtils.div(discount, 10.0, 1));
                    sku.setSubtotal(subtotal);
                }
                sku.setDiscount(String.valueOf(discount==0?10:discount));
                sku.setPrice(retailPrice);
                sku.setCode(skuMap.get(skuId).getCode());
                sku.setName(skuMap.get(skuId).getName());
                totalNum += num;
                originalPrice += num * retailPrice;
                discountPrice += subtotal;
//            }

            //积分商品
//            if(spu.getType()==ProductSPU.type_integral){
                int integralPrice=skuMap.get(skuId).getIntegralPrice();
                long currentTime = System.currentTimeMillis();
/*                if(num>inventoryMap.get(skuId)) {
                    throw new StoreSystemException("当前购买的商品库存数量不足！");
                }*/
                if(currentTime>spu.getIntegralStartTime()&&currentTime<spu.getIntegralEndTime()) {
                    if (spu.getIntegralNum() < sku.getNum()) {
                        throw new StoreSystemException("积分商品数量兑换上限！");
                    }
                    if (userScore > 0) {
                        if (skuMap.get(skuId).getIntegralPrice() * sku.getNum() > userScore) {
                            throw new StoreSystemException("此顾客会员积分不足以兑换该积分商品！");
                        }
                    }

                    subtotal = num * integralPrice;
                    sku.setSubtotal(subtotal);
                    sku.setIntegralPrice(integralPrice);
                    sku.setCode(skuMap.get(skuId).getCode());
                    totalNum += num;

                }
//            }
        }

        if(businessOrder.getCouponId()>0){
            coupon = marketingCouponService.calculateMoney(businessOrder.getCouponId(), totalNum, originalPrice);
            client.setCouponId(businessOrder.getCouponId());
            client.setCouponFee(coupon);
            MarketingCoupon marketingCoupon = marketingCouponService.load(businessOrder.getCouponId());
            if(marketingCoupon!=null){
                client.setCoupon(marketingCoupon);
            }
            discountPrice = discountPrice - coupon;
//            originalPrice = originalPrice - coupon;
        }

        if (businessOrder.getSurcharges().size() > 0) {
            for (Surcharge surcharge : businessOrder.getSurcharges()) {
                surchargesPrice += surcharge.getPrice();
            }
            discountPrice += surchargesPrice;
            originalPrice += surchargesPrice;
        }

        if(businessOrder.getRechargePrice()>0){
            discountPrice += businessOrder.getRechargePrice();
            originalPrice += businessOrder.getRechargePrice();
        }

        client.setOriginalPrice(originalPrice);
        client.setRechargePrice(businessOrder.getRechargePrice());
        //特惠减免
        if(businessOrder.getRealPrice()==0){
            client.setOddsPrice(oddsPrice);
            client.setRealPrice(discountPrice);
        } else if (businessOrder.getRealPrice() < discountPrice) {
            oddsPrice = discountPrice - businessOrder.getRealPrice();
            client.setOddsPrice(oddsPrice);
            if (businessOrder.getRealPrice() != 0) {
                discountPrice = discountPrice - oddsPrice;
            }
        } else {
            client.setRealPrice(discountPrice);
        }

        client.setDiscountPrice(discountPrice);
        totalPrice = discountPrice;
        client.setTotalPrice(totalPrice);
        return new ResultClient(client);
    }

    private ClientBusinessOrder transformClient(BusinessOrder businessOrder) throws Exception{
        ClientBusinessOrder client = new ClientBusinessOrder(businessOrder);

        OptometryInfo optometryInfo = optometryInfoDao.load(businessOrder.getOiId());
        Subordinate subordinate = subordinateDao.load(businessOrder.getSubId());
        if(subordinate != null) {
            client.setSubName(subordinate.getName());
            client.setThreePolicy(subordinate.getThreePolicy());
            client.setPhone(subordinate.getPhone());
            client.setAddress(subordinate.getAddress());
        }

        User user = userDao.load(businessOrder.getUid());
        if(user != null) {
            client.setUName(user.getName());
            client.setUPhone(user.getPhone());
            client.setScore(user.getScore());
            client.setMoney(user.getMoney());
            ClientUser clientUser = new ClientUser(user);
            if(user.getRecommender()>0){
                long uid = user.getRecommender();
                User recommender = userDao.load(uid);
                if(recommender!=null){
                    clientUser.setTname(recommender.getName());
                    clientUser.setTphone(recommender.getPhone());
                }
            }
            client.setUserInfo(clientUser);
        }
        User machiningUser = userDao.load(businessOrder.getMachinistId());
        if(machiningUser!=null) client.setMachiningName(machiningUser.getName());
        if (optometryInfo != null) {
            User oiUser = userDao.load(optometryInfo.getOptUid());
            if (oiUser != null) client.setOiName(oiUser.getName());
        }
        User staff = userDao.load(businessOrder.getStaffId());
        if(staff != null){
            client.setStaffName(staff.getName());
        }
        List<OptometryInfo> optometryInfos = optometryInfoDao.getList(businessOrder.getUid(),10);
        client.setOptometryInfos(optometryInfos);
        int count = afterSaleDetailDao.getCount(businessOrder.getId());
        client.setAsCount(count);
        if(businessOrder.getOiId()>0){
            // 验光信息
            OptometryInfo load = optometryInfoDao.load(businessOrder.getOiId());
            if(load!=null) {
                client.setInfo(load);
            }
        }
        MarketingCoupon marketingCoupon = marketingCouponService.load(businessOrder.getCouponId());
        if(marketingCoupon!=null){
            client.setCoupon(marketingCoupon);
        }

        List<PayInfo> payInfos = payInfoService.getAllList(businessOrder.getId(),PayInfo.status_pay);
        client.setPayInfos(payInfos);
        return client;
    }

    @Override
    public  CalculateOrder calculateBusinessOrder(long subId, long startTime, long endTime) throws Exception {
        String sql = "SELECT  *  FROM `business_order`   where  1=1  ";

        {
            sql = sql + " and `status` = " + BusinessOrder.status_pay;
        }
        if (subId > 0) {
            sql = sql + " and `subid` = " + subId;
        }
        if (endTime > 0) {
            sql = sql + " and `ctime` >=" + startTime;
        }
        if (endTime > 0) {
            sql = sql + " and `ctime` <=" + endTime;
        }
        sql = sql + " order  by ctime desc";
        List<BusinessOrder> orderList = this.jdbcTemplate.query(sql, rowMapper);
        List<PayInfo> payInfoList=Lists.newArrayList();
        for(BusinessOrder businessOrder:orderList){
            List<PayInfo> payInfos = payInfoService.getAllList(businessOrder.getId(),PayInfo.status_pay);
            payInfoList.addAll(payInfos);
        }

        Map<String,Integer> map = calculateSale(payInfoList);
        CalculateOrder order = new CalculateOrder();
        order.setSale(map.get("sale")/100.0);
        order.setAli(map.get("ali")/100.0);
        order.setWx(map.get("wx")/100.0);
        order.setCash(map.get("cash")/100.0);
        order.setOther(map.get("other")/100.0);
        order.setNum(orderList.size());
        return order;
    }

    @Override
    public Map<String,Integer> calculateSale(List<PayInfo> payInfos) {
        Map<String,Integer> map = Maps.newHashMap();
        int sale=0;
        int ali=0;
        int wx=0;
        int cash=0;
        int other=0;
        for(PayInfo payInfo:payInfos) {
                if (payInfo.getPayType() == PayInfo.pay_type_ali) {
                    ali += payInfo.getPrice();
                }
                if (payInfo.getPayType() == PayInfo.pay_type_wx) {
                    wx += payInfo.getPrice();
                }
                if (payInfo.getPayType() == PayInfo.pay_type_cash) {
                    cash += payInfo.getPrice();
                }
                if (payInfo.getPayType() == PayInfo.pay_type_stored) {
                    other += payInfo.getPrice();
                }
        }
        sale = ali + wx + cash + other;
        map.put("ali",ali);
        map.put("wx",wx);
        map.put("cash",cash);
        map.put("other",other);
        map.put("sale",sale);
        return map;
    }

    @Override
    public ClientSettlementOrder settlementPay(long boId, int cash, int stored, int otherStored) throws Exception {
        BusinessOrder businessOrder = businessOrderDao.load(boId);
        ClientSettlementOrder client = new ClientSettlementOrder();
        List<PayInfo> payInfos = payInfoService.getAllList(boId,PayInfo.status_pay);
        int totalPrice=0;
        for(PayInfo info:payInfos){
            if(info.getPayType()==PayInfo.pay_type_ali){
                client.setAli(info.getPrice());
                totalPrice += info.getPrice();
            }
            if(info.getPayType()==PayInfo.pay_type_wx){
                client.setWx(info.getPrice());
                totalPrice += info.getPrice();
            }
            if(info.getPayType()==PayInfo.pay_type_cash){
                client.setCash(info.getPrice());
                totalPrice += info.getPrice();
            }
            if(info.getPayType()==PayInfo.pay_type_stored){
                client.setStored(info.getPrice());
                totalPrice += info.getPrice();
            }
        }
        if (otherStored > 0) {
            if(otherStored>businessOrder.getRealPrice()) {
                throw new StoreSystemException("他人储值支付不能超过实收金额！");
            }
            totalPrice += otherStored;
            client.setOtherStored(otherStored);
        }
        if (cash > 0) {
            if(cash>businessOrder.getRealPrice()) {
                throw new StoreSystemException("现金支付不能超过实收金额！");
            }
            totalPrice += cash;
            client.setCash(cash);
        }
        if (stored > 0) {
            if(stored>businessOrder.getRealPrice()) {
                throw new StoreSystemException("储值支付不能超过实收金额！");
            }
            totalPrice += stored;
            client.setStored(stored);
        }
        client.setTotal(totalPrice);
        client.setAmount(businessOrder.getRealPrice()-totalPrice);
        return client;
    }

    @Override
    public ClientBusinessOrder settlementOrder(long boId, int cash, int stored, int otherStored, int score, int makeStatus,String desc) throws Exception {
        BusinessOrder businessOrder = businessOrderDao.load(boId);

        PayInfo payInfo = new PayInfo();
        payInfo.setSubId(businessOrder.getSubId());
        payInfo.setUid(businessOrder.getUid());
        payInfo.setStatus(PayInfo.status_pay);
        payInfo.setBoId(boId);
        if (cash > 0) {
            if(cash>businessOrder.getRealPrice()) {
                throw new StoreSystemException("现金支付不能超过实收金额！");
            }
            payInfo.setPrice(cash);
            payInfo.setPayType(PayInfo.pay_type_cash);
            payInfoService.insert(payInfo);
            financeLogService.insertLog(FinanceLog.ownType_user, businessOrder.getSubId(), businessOrder.getUid(), FinanceLog.mode_cash , FinanceLog.type_in, 0, cash,
                    "用户支付", true);
        }

        if (otherStored > 0) {
            if(otherStored>businessOrder.getRealPrice()) {
                throw new StoreSystemException("他人储值支付不能超过实收金额！");
            }
            payInfo.setPrice(otherStored);
            payInfo.setPayType(PayInfo.pay_type_stored);
            payInfoService.insert(payInfo);
            // TODO: 2019/8/8 他人储值需要选择对应的某个人uid，目前一个手机号可查出多个人

        }

        if (stored > 0) {
            if(stored>businessOrder.getRealPrice()) {
                throw new StoreSystemException("储值支付不能超过实收金额！");
            }
            payInfo.setPrice(stored);
            payInfo.setPayType(PayInfo.pay_type_stored);
            payInfoService.insert(payInfo);
            User user = userDao.load(businessOrder.getUid());
            if(user!=null){
                user.setMoney(user.getMoney()-stored);
                userDao.update(user);
            }
            financeLogService.insertLog(FinanceLog.ownType_user, businessOrder.getSubId(), businessOrder.getUid(), FinanceLog.mode_cash , FinanceLog.type_in, 0, stored,
                    "用户支付", true);
        }

        if(score > 0){
            User user = userDao.load(businessOrder.getUid());
            if(user!=null){
                user.setScore(user.getScore()-score);
                userDao.update(user);
            }
        }
        businessOrder.setMakeStatus(makeStatus);
        //若所有不同支付方式的支付总金额等于订单应付金额
        // 则认为此订单已缴费 否则未缴费
        List<PayInfo> payInfos = payInfoService.getAllList(boId,PayInfo.status_pay);
        long total = 0;
        for (PayInfo info : payInfos) {
            total += info.getPrice();
        }
        if (total == businessOrder.getRealPrice()) {
            businessOrder.setStatus(BusinessOrder.status_pay);
        }
        businessOrder.setReceiptDesc(desc);
        businessOrderDao.update(businessOrder);

        return transformClient(businessOrderDao.load(boId));
    }


    private List<ClientBusinessOrder> transformClient(List<BusinessOrder> businessOrders) throws Exception {
        List<ClientBusinessOrder> clients = new ArrayList<>();
        for (BusinessOrder info : businessOrders) {
            ClientBusinessOrder client = transformClient(info);
            clients.add(client);
        }
        return clients;
    }




}
