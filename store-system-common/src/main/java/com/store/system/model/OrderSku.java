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
    public static final int type_past = 1; //过期
    public static final int type_repair = 2; //保修
    public static final int type_renew = 3; //换新

    public static final int status_update = 1; //更换
    public static final int status_return = 2; //退货
    public static final int status_loss = 3; //报损

    private long skuid;
    private int num;
    private String name;
    private double price;//单价
    private double subtotal;//小计 price*num*coupon折扣
    private int qualityType;//质保类型
    private int optStatus;//操作状态

}
