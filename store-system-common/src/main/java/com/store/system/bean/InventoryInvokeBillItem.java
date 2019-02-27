package com.store.system.bean;

import java.io.Serializable;

public class InventoryInvokeBillItem implements Serializable {

    private long did;

    private int num;

    public InventoryInvokeBillItem() {
    }

    public long getDid() {
        return did;
    }

    public void setDid(long did) {
        this.did = did;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
