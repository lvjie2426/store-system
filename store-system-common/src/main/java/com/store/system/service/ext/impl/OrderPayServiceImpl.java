package com.store.system.service.ext.impl;

import com.store.system.bean.OrderTypeInfo;
import com.store.system.model.FinanceLog;
import com.store.system.model.Order;
import com.store.system.service.FinanceLogService;
import com.store.system.service.ext.OrderPayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderPayServiceImpl implements OrderPayService {

    @Resource
    private FinanceLogService financeLogService;

    @Override
    public void successHandleBusiness(Order order) throws Exception {


        if(StringUtils.isNotBlank(order.getTypeInfo())) {
            OrderTypeInfo orderTypeInfo = OrderTypeInfo.getObject(order.getTypeInfo());
            long uid = orderTypeInfo.getUid();
            long money = orderTypeInfo.getMoney();
            financeLogService.insertLog(FinanceLog.ownType_user, uid, order.getPayType(), FinanceLog.type_in, 0, money,
                    "用户支付", true);
        }
    }

}
