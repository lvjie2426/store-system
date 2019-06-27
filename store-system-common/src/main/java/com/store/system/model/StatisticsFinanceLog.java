package com.store.system.model;

import java.io.Serializable;

public class StatisticsFinanceLog implements Serializable {

    public static final int model_all = 0;
    public static final int mode_ali = 1; //支付宝
    public static final int mode_wx = 2; //微信

    private long id;

    private int mode;

    private int subType;

    private double totalIn;

    private double totalOut;

    private int date; //yyyyMMdd

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public double getTotalIn() {
        return totalIn;
    }

    public void setTotalIn(double totalIn) {
        this.totalIn = totalIn;
    }

    public double getTotalOut() {
        return totalOut;
    }

    public void setTotalOut(double totalOut) {
        this.totalOut = totalOut;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

}
