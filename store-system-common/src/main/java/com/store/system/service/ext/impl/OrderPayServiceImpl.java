package com.store.system.service.ext.impl;

import com.store.system.service.ext.OrderPayService;
import org.springframework.stereotype.Service;

@Service
public class OrderPayServiceImpl implements OrderPayService {

    @Override
    public void successHandleBusiness(int payType, int type, String typeInfo) {
        System.out.println("支付成功");
    }

}
