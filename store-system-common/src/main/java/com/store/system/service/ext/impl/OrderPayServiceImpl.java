package com.store.system.service.ext.impl;

import com.store.system.bean.OrderTypeInfo;
import com.store.system.dao.ProductSPUDao;
import com.store.system.model.FinanceLog;
import com.store.system.model.Order;
import com.store.system.model.OrderSku;
import com.store.system.model.ProductSPU;
import com.store.system.service.FinanceLogService;
import com.store.system.service.ext.OrderPayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderPayServiceImpl implements OrderPayService {

    @Resource
    private FinanceLogService financeLogService;
    @Resource
    private ProductSPUDao productSPUDao;

    @Override
    public void successHandleBusiness(Order order) throws Exception {
        for(OrderSku sku:order.getSkuids()){
            ProductSPU spu = productSPUDao.load(sku.getSpuid());
            int days = (int) spu.getProperties().get(4L);



        }

        if(StringUtils.isNotBlank(order.getTypeInfo())) {
            OrderTypeInfo orderTypeInfo = OrderTypeInfo.getObject(order.getTypeInfo());
            long uid = orderTypeInfo.getUid();
            long money = orderTypeInfo.getMoney();
            for(Integer type:order.getPayTypes()) {
                financeLogService.insertLog(FinanceLog.ownType_user, order.getSubid(), uid, type, FinanceLog.type_in, 0, money,
                        "用户支付", true);
            }
        }
    }

}
