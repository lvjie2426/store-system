package com.store.system.bean;

import java.io.Serializable;

/**
 * 进销存盘点单子条目
 * class_name: InventoryCheckBillItem
 * package: com.store.system.bean
 * creat_user: lihao
 * creat_date: 2018/12/26
 * creat_time: 16:05
 **/
public class InventoryCheckBillItem implements Serializable {

    private long did;

    private int currentNum; //当前数量

    private int realNum; //实际数量

    public InventoryCheckBillItem() {
    }

    public long getDid() {
        return did;
    }

    public void setDid(long did) {
        this.did = did;
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }

    public int getRealNum() {
        return realNum;
    }

    public void setRealNum(int realNum) {
        this.realNum = realNum;
    }
}
