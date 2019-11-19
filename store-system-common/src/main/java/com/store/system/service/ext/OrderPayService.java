package com.store.system.service.ext;


import com.store.system.client.ResultClient;
import com.store.system.model.Order;

public interface OrderPayService {

    public ResultClient successHandleBusiness(Order order, long boId) throws Exception;

}
