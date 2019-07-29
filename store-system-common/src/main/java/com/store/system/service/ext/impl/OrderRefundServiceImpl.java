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
    public void successHandleBusiness(int type, String typeInfo) throws Exception {
        Map<String, Object> info = JsonUtils.fromJson(typeInfo, new TypeReference<Map<String, Object>>() {});
        long boId = (long) info.get("boId");

        List<PayInfo> payInfoList = payInfoService.getAllList(boId);
        for(PayInfo payInfo:payInfoList){
            if(type== PayInfo.pay_type_ali){



            }

        }



    }

    @Override
    public void failHandleBusiness(int type, String typeInfo) {


    }
}
