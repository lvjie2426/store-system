package com.store.system.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SalePresentItem
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/25 17:49
 * @Version 1.0
 **/
@Data
public class SalePresentItem implements Serializable{

    public static final int TYPE_GOODS = 1;//商品
    public static final int TYPE_COUPON = 2;//优惠券

    private int type;

    private long typeId;

    private int itemNum;

}
