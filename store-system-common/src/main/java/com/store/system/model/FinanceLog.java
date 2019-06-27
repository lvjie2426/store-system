package com.store.system.model;


import java.io.Serializable;

public class FinanceLog implements Serializable {

    public static final int ownType_user = 1; //用户
    public static final int ownType_other = 0; //其他

    public static final int mode_ali = 1; //支付宝
    public static final int mode_wx = 2; //微信

    public static final int type_in = 1;  //+
    public static final int type_out = 2; //-


    private long id;

    private int ownType;

    private long ownId;

    private int mode;

    private int type;

    private int subType;

    private double money;

    private String desc;

    private long time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOwnType() {
        return ownType;
    }

    public void setOwnType(int ownType) {
        this.ownType = ownType;
    }

    public long getOwnId() {
        return ownId;
    }

    public void setOwnId(long ownId) {
        this.ownId = ownId;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
