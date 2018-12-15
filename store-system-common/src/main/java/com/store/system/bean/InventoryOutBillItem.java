package com.store.system.bean;

import java.io.Serializable;

public class InventoryOutBillItem implements Serializable {

    private long did;

    private String code; //产品编码

    private String propertyJson; //属性json

    private int num;

    public InventoryOutBillItem() {
    }

    public long getDid() {
        return did;
    }

    public void setDid(long did) {
        this.did = did;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPropertyJson() {
        return propertyJson;
    }

    public void setPropertyJson(String propertyJson) {
        this.propertyJson = propertyJson;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
