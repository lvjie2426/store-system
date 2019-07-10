package com.store.system.model;


import lombok.Data;

import java.io.Serializable;

@Data
public class FinanceLog implements Serializable {

    public static final int ownType_user = 1; //用户
    public static final int ownType_other = 0; //其他

    public static final int mode_ali = 1; //支付宝
    public static final int mode_wx = 2; //微信
    public static final int mode_cash = 3; //现金
    public static final int mode_stored = 4; //储值

    public static final int type_in = 1;  //+
    public static final int type_out = 2; //-

    private long id;

    private int ownType;

    private long subId;

    private long ownId;

    private int mode;

    private int type;

    private int subType;

    private double money;

    private String desc;

    private long day;

    private long time;
}
