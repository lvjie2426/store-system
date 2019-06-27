package com.store.system.bean;

import com.quakoo.baseFramework.jackson.JsonUtils;

import java.io.Serializable;

/**
 * @ClassName OrderTypeInfo
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/3/25 19:10
 * @Version 1.0
 **/
public class OrderTypeInfo implements Serializable{

    private long uid;

    private long buyId;

    private long money;

    public OrderTypeInfo() {}

    public OrderTypeInfo(long uid, long buyId, long money) {
        this.uid = uid;
        this.buyId = buyId;
        this.money = money;
    }

    public static String getJsonStr(OrderTypeInfo orderTypeInfo) {
        return JsonUtils.toJson(orderTypeInfo);
    }

    public static OrderTypeInfo getObject(String jsonStr) {
        return JsonUtils.fromJson(jsonStr, OrderTypeInfo.class);
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getBuyId() {
        return buyId;
    }

    public void setBuyId(long buyId) {
        this.buyId = buyId;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }
}
