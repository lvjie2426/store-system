package com.store.system.service.ext.impl;

import com.google.common.collect.Lists;
import com.store.system.bean.OrderTypeInfo;
import com.store.system.dao.*;
import com.store.system.model.*;
import com.store.system.service.*;
import com.store.system.service.ext.OrderPayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class OrderPayServiceImpl implements OrderPayService {

    @Resource
    private FinanceLogService financeLogService;
    @Resource
    private ProductSPUDao productSPUDao;
    @Resource
    private UserDao userDao;
    @Resource
    private MissionService missionService;
    @Resource
    private SubordinateMissionPoolDao subordinateMissionPoolDao;
    @Resource
    private MissionDao missionDao;
    @Resource
    private UserMissionPoolDao userMissionPoolDao;
    @Resource
    private CommissionDao commissionDao;
    @Resource
    private CommissionRewardService commissionRewardService;
    @Resource
    private PayInfoService payInfoService;
    @Resource
    private BusinessOrderService businessOrderService;

    @Override
    public void successHandleBusiness(Order order,long boId) throws Exception {

        BusinessOrder businessOrder = businessOrderService.load(boId);
        PayInfo payInfo = new PayInfo();
        payInfo.setSubId(businessOrder.getSubId());
        payInfo.setUid(businessOrder.getUid());
        payInfo.setPrice(order.getPrice());
        payInfo.setPayType(order.getPayType());
        payInfo.setStatus(PayInfo.status_pay);
        payInfo.setBoId(boId);
        payInfoService.insert(payInfo);

        businessOrder.setMakeStatus(BusinessOrder.makeStatus_qu_yes);
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
        if(businessOrder.getRechargePrice()>0){
            User user = userDao.load(businessOrder.getUid());
            if(user!=null){
                user.setMoney(user.getMoney()+businessOrder.getRechargePrice());
                userDao.update(user);
            }
        }
        businessOrderService.update(businessOrder);

        /**
         * 任务进度修改
         */
        User user = userDao.load(order.getId());
        if(user != null) {
            long userId = user.getId();
            long subid = user.getSid();
            long sid = user.getPsid();
            // TODO: 2019/7/24
            List<OrderSku> skuList = Lists.newArrayList();
//        List<OrderSku> skuList= order.getSkuList();
            for (OrderSku orderSku : skuList) {
                long skuId = orderSku.getSkuId();
                /**查找任务**/
                List<Mission> missions = missionService.checkMission(skuId, sid, subid, userId);
                if (missions.size() > 0) {
                    for (Mission mission : missions) {
                        boolean missionStatus = false;
                        /**团队任务**/
                        if (mission.getType() == Mission.type_tem) {
                            SubordinateMissionPool subordinateMissionPool = new SubordinateMissionPool();
                            subordinateMissionPool.setMid(mission.getId());
                            subordinateMissionPool.setSid(subid);//门店ID
                            subordinateMissionPool = subordinateMissionPoolDao.load(subordinateMissionPool);
                            /**判断任务完成类型**/
                            if (mission.getAmountType() == Mission.amountType_number) {//数量
                                subordinateMissionPool.setNumber(subordinateMissionPool.getNumber() + orderSku.getNum());
                                subordinateMissionPool.setProgress(getProgress(subordinateMissionPool.getNumber(), mission.getTarget()));
                            }
                            if (mission.getAmountType() == Mission.amountType_money) {//金额
                                subordinateMissionPool.setPrice(subordinateMissionPool.getPrice() + (int) (orderSku.getPrice() * 100));
                                subordinateMissionPool.setProgress(getProgress(subordinateMissionPool.getPrice(), mission.getTarget()));
                            }
                            /**保存订单ID**/
                            subordinateMissionPool.getOids().add(order.getId());
                            boolean flag = subordinateMissionPoolDao.update(subordinateMissionPool);
                            /**任务完成修改状态**/
                            if (flag && subordinateMissionPool.getNumber() >= mission.getTarget() || subordinateMissionPool.getPrice() >= mission.getTarget()) {
                                mission.setStatus(Mission.status_yes);
                                missionStatus = missionDao.update(mission);
                            }
                        }
                        /**个人任务**/
                        if (mission.getType() == Mission.type_user) {
                            UserMissionPool userMissionPool = new UserMissionPool();
                            userMissionPool.setUid(userId);
                            userMissionPool.setMid(mission.getId());
                            userMissionPool = userMissionPoolDao.load(userMissionPool);
                            /**判断任务完成类型**/
                            if (mission.getAmountType() == Mission.amountType_number) {//数量
                                userMissionPool.setNumber(userMissionPool.getNumber() + orderSku.getNum());
                                userMissionPool.setProgress(getProgress(userMissionPool.getNumber(), mission.getTarget()));
                            }
                            if (mission.getAmountType() == Mission.amountType_money) {//金额
                                userMissionPool.setPrice(userMissionPool.getPrice() + (int) (orderSku.getPrice() * 100));
                                userMissionPool.setProgress(getProgress(userMissionPool.getPrice(), mission.getTarget()));
                            }
                            /**保存订单ID**/
                            userMissionPool.getOids().add(order.getId());
                            boolean flag = userMissionPoolDao.update(userMissionPool);
                            /**任务完成修改状态**/
                            if (flag && userMissionPool.getNumber() >= mission.getTarget() || userMissionPool.getPrice() >= mission.getTarget()) {
                                mission.setStatus(Mission.status_yes);
                                missionStatus = missionDao.update(mission);
                            }
                        }
                        /**任务完成 记录表增加一条数据**/
                        if (missionStatus) {
                            CommissionReward commissionReward = new CommissionReward();
                            commissionReward.setMid(mission.getId());
                            commissionReward.setSid(subid);
                            commissionReward.setUid(userId);
                            commissionReward.setPrice(mission.getAmount());
                            commissionReward.setType(CommissionReward.type_mission);
                            commissionRewardService.add(commissionReward);
                        }
                    }
                }
            }
            // TODO: 2019/7/24
            for (OrderSku orderSku : skuList) {
                Commission commission = new Commission();
                commission.setSpuId(orderSku.getSpuId());
                commission.setSubId(subid);
                commission = commissionDao.load(commission);
                if (commission != null) {
                    /**有提成的商品增加 提成的记录**/
                    CommissionReward commissionReward = new CommissionReward();
                    commissionReward.setSid(subid);
                    commissionReward.setUid(userId);
                    List<OrderSku> orderSkuList = Lists.newArrayList();
                    orderSkuList.add(orderSku);
                    commissionReward.setSkuList(orderSkuList);
                    commissionReward.setType(CommissionReward.type_reward);
                    /**算个人提成**/
                int users = commission.getUsers();
                            commissionReward.setPrice((int) (orderSku.getPrice() * users));
                            commissionRewardService.add(commissionReward);
                }
            }
        }


        // TODO: 2019/7/24
        if(StringUtils.isNotBlank(order.getTypeInfo())) {
            OrderTypeInfo orderTypeInfo = OrderTypeInfo.getObject(order.getTypeInfo());
            long uid = orderTypeInfo.getUid();
            long money = orderTypeInfo.getMoney();
            financeLogService.insertLog(FinanceLog.ownType_user, businessOrder.getSubId(), uid, order.getPayType() , FinanceLog.type_in, 0, money,
                    "用户支付", true);
        }
    }

    private int getProgress(int now,int target)throws Exception{
        float a = now;//当前完成目标
        float b = target;//总目标
        return (int) (a/b*100);
    }
}
