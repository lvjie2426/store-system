package com.store.system.client;

import com.store.system.model.Order;
import com.store.system.model.OrderSku;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;

/**
 * @program: store-system
 * @description: 订单sku相关信息。金额数量折扣抵用券小计。
 * @author: zhangmeng
 * @create: 2019-05-23 13:50
 **/
@Data
public class ClientOrderSku implements Serializable{

    public static final int type_past = 1; //过期
    public static final int type_repair = 2; //保修
    public static final int type_renew = 3; //换新

    public static final int status_update = 1; //更换
    public static final int status_return = 2; //退货
    public static final int status_loss = 3; //报损

    private long skuid;
    private long spuid;

    private int num;

    private String name;

    private double price;//单价

    private double discount;//折扣

    private double subtotal;//小计 price*num*spu折扣

    private double lastSubtotal;//小计 price*num*spu折扣*usergrade折扣

    private int qualityType;//质保类型

    private int optStatus;//操作状态

    public ClientOrderSku(){};
    public ClientOrderSku(OrderSku orderSku){
        try {
            BeanUtils.copyProperties(this, orderSku);
        } catch (Exception e) {
            throw new IllegalStateException("ClientOrderSku construction error!");
        }

    };

}
