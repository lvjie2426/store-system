package com.store.system.client;

import com.store.system.model.Payment;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

/**
 * @ClassName ClientPayment
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/5/17 14:33
 * @Version 1.0
 **/
@Data
public class ClientPayment extends Payment {

    private String subName; //门店名称

    public ClientPayment(Payment payment) {
        try {
            BeanUtils.copyProperties(this, payment);
        } catch (Exception e) {
            throw new IllegalStateException("ClientPayment construction error!");
        }
    }
}
