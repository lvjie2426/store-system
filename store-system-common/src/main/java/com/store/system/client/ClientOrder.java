package com.store.system.client;

import com.store.system.model.Order;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

/**
 * @program: qianyi
 * @description:
 * @author: zhangmeng
 * @create: 2019-04-03 16:50
 **/
@Data
public class ClientOrder extends Order {

    private String uName;
    private String uPhone;
    private String personnelName;
    private double descSubtract;// 折扣（打折）、金额
    private int descSubtractType;// 类型(1-金额 2-百分比)

    public ClientOrder(){};
    public ClientOrder(Order order){
        try {
            BeanUtils.copyProperties(this, order);
        } catch (Exception e) {
            throw new IllegalStateException("ClientOrder construction error!");
        }

    };


}
