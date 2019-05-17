package com.store.system.service;

import com.store.system.client.ClientPayment;
import com.store.system.model.Payment;

import java.util.List;

/**
 * @ClassName PaymentService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/5/17 14:25
 * @Version 1.0
 **/
public interface PaymentService {

    public Payment insert(Payment payment) throws Exception;

    public List<ClientPayment> getUsedList(long psid, int payType) throws Exception;
}
