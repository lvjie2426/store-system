package com.store.system.bean;

import com.quakoo.baseFramework.jackson.JsonUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName OrderTypeInfo
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/3/25 19:10
 * @Version 1.0
 **/
@Data
public class OrderTypeInfo implements Serializable{

    private long uid;

    private int payType;

    private int money;

    private long boId;

    private int payModel;

    public OrderTypeInfo() {}

    public OrderTypeInfo(long uid, int payType, int money, long boId, int payModel) {
        this.uid = uid;
        this.payType = payType;
        this.money = money;
        this.boId = boId;
        this.payModel = payModel;
    }

    public static String getJsonStr(OrderTypeInfo orderTypeInfo) {
        return JsonUtils.toJson(orderTypeInfo);
    }

    public static OrderTypeInfo getObject(String jsonStr) {
        return JsonUtils.fromJson(jsonStr, OrderTypeInfo.class);
    }

}
