package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.store.system.client.ClientPayment;
import com.store.system.client.ClientProductSPU;
import com.store.system.dao.PaymentDao;
import com.store.system.dao.SubordinateDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.PaymentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName PaymentServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/5/17 14:26
 * @Version 1.0
 **/
@Service
public class PaymentServiceImpl implements PaymentService{

    @Resource
    private PaymentDao paymentDao;
    @Resource
    private SubordinateDao subordinateDao;

    private TransformMapUtils subordinateMapUtils = new TransformMapUtils(Subordinate.class);

    @Override
    public Payment insert(Payment payment) throws Exception {
        check(payment);
        Payment dbInfo = paymentDao.load(payment);
        if(dbInfo==null) payment = paymentDao.insert(payment);
        return payment;
    }


    private void check(Payment payment) throws StoreSystemException {
        long subid = payment.getSubid();
        long psid = payment.getPsid();
        String merchantsWx = payment.getMerchantsWx();
        String subMerchantsWx = payment.getSubMerchantsWx();
        String personalWx = payment.getPersonalWx();
        String merchantsAli = payment.getMerchantsAli();
        String personalAli = payment.getPersonalAli();
        if(subid<=0) throw new StoreSystemException("门店ID不能为空！");
        if(psid<=0) throw new StoreSystemException("企业ID不能为空！");
        if(payment.getPayType()!=Payment.pay_type_ali && payment.getPayType()!=Payment.pay_type_wx)
            throw new StoreSystemException("支付类型有误！");
        if(payment.getPayType()==Payment.pay_type_wx){
            if(payment.getType()==Payment.type_public){
                if(StringUtils.isNotBlank(merchantsWx)) throw new StoreSystemException("商户微信号不能为空！");
                if(payment.getMerchantsWxStatus()!=Payment.status_on && payment.getMerchantsWxStatus()!=Payment.status_off)
                    throw new StoreSystemException("商户微信号状态有误！");
                if(StringUtils.isNotBlank(subMerchantsWx)) throw new StoreSystemException("子商户微信号不能为空！");
                if(payment.getSubMerchantsWxStatus()!=Payment.status_on && payment.getSubMerchantsWxStatus()!=Payment.status_off)
                    throw new StoreSystemException("子商户微信号状态有误！");
            }
            if(payment.getType()==Payment.tyoe_personal){
                if(StringUtils.isNotBlank(personalWx)) throw new StoreSystemException("子商户微信号不能为空！");
                if(payment.getPersonalWxStatus()!=Payment.status_on && payment.getPersonalWxStatus()!=Payment.status_off)
                    throw new StoreSystemException("个人收款微信号状态有误！");
            }
        }

        if(payment.getPayType()==Payment.pay_type_ali){
            if(payment.getType()==Payment.type_public){
                if(StringUtils.isNotBlank(merchantsAli)) throw new StoreSystemException("商户支付宝账号不能为空！");
                if(payment.getMerchantsAliStatus()!=Payment.status_on && payment.getMerchantsAliStatus()!=Payment.status_off)
                    throw new StoreSystemException("商户支付宝账号状态有误！");
            }
            if(payment.getType()==Payment.tyoe_personal){
                if(StringUtils.isNotBlank(personalAli)) throw new StoreSystemException("个人收款支付宝账号不能为空！");
                if(payment.getPersonalAliStatus()!=Payment.status_on && payment.getPersonalAliStatus()!=Payment.status_off)
                    throw new StoreSystemException("个人收款支付宝账号状态有误！");
            }
        }

    }

    @Override
    public List<ClientPayment> getUsedList(long psid, int payType) throws Exception {
        List<Payment> payments = paymentDao.getUsedList(psid,payType,Payment.type_public);
        return transformClients(payments);
    }


    private List<ClientPayment> transformClients(List<Payment> payments) throws Exception {
        List<ClientPayment> res = Lists.newArrayList();
        if (payments.size() == 0) return res;
        Set<Long> subids = Sets.newHashSet();
        for (Payment one : payments) {
            subids.add(one.getSubid());
        }
        List<Subordinate> subordinates = subordinateDao.load(Lists.newArrayList(subids));
        Map<Long, Subordinate> subordinateMap = subordinateMapUtils.listToMap(subordinates, "id");

        for (Payment one : payments) {
            ClientPayment client = new ClientPayment(one);
            Subordinate subordinate = subordinateMap.get(client.getSubid());
            if (null != subordinate) {
                client.setSubName(subordinate.getName());
            }
            res.add(client);
        }
        return res;
    }
}
