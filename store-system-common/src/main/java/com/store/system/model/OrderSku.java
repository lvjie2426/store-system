package com.store.system.model;

import lombok.Data;

/**
 * @program: store-system
 * @description: 订单sku相关信息。金额数量折扣抵用券小计。
 * @author: zhangmeng
 * @create: 2019-05-23 13:50
 **/
@Data
public class OrderSku {

    private long skuid;
    private int num;
    private double price;//单价
    private long couponid;
    private double subtotal;//小计 price*num*coupon折扣

}
