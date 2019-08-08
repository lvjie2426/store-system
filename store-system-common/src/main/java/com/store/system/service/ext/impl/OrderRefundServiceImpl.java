package com.store.system.service.ext.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.store.system.dao.PayInfoDao;
import com.store.system.dao.impl.PayInfoDaoImpl;
import com.store.system.model.PayInfo;
import com.store.system.service.PayInfoService;
import com.store.system.service.PayService;
import com.store.system.service.ext.OrderRefundService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/** 一键退款
 * @ClassName OrderRefundServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/28 11:57
 * @Version 1.0
 **/
@Service
public class OrderRefundServiceImpl implements OrderRefundService{

    @Resource
    private PayInfoService payInfoService;

    @Override
    @Transactional
    public void successHandleBusiness(int payType, String typeInfo) throws Exception {
        Map<String, Object> info = JsonUtils.fromJson(typeInfo, new TypeReference<Map<String, Object>>() {});
        Object object = info.get("boId");
        String id = String.valueOf(object);
        long boId = Long.valueOf(id);

        List<PayInfo> payInfoList = payInfoService.getAllList(boId,PayInfo.status_pay);
        for(PayInfo payInfo:payInfoList){
            if(payType== PayInfo.pay_type_ali){
                payInfo.setStatus(PayInfo.status_refund);
            }
            if(payType== PayInfo.pay_type_wx){
                payInfo.setStatus(PayInfo.status_refund);
            }
            if(payType== PayInfo.pay_type_cash){
                payInfo.setStatus(PayInfo.status_refund);
            }
            if(payType== PayInfo.pay_type_stored){
                payInfo.setStatus(PayInfo.status_refund);
            }
            payInfoService.update(payInfo);
        }



    }

    @Override
    public void failHandleBusiness(int payType, String typeInfo) {


    }
}
