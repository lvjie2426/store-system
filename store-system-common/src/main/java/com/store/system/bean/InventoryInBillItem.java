package com.store.system.bean;

import java.io.Serializable;

/**
 * 进销存入库单子条目
 * class_name: InventoryInBillItem
 * package: com.store.system.bean
 * creat_user: lihao
 * creat_date: 2018/12/11
 * creat_time: 11:24
 **/
public class InventoryInBillItem implements Serializable {

    private long spuid;

    private String code; //产品编码

    private String propertyJson; //属性json

    private int retailPrice; //零售价(分)

    private int costPrice; //成本价(分)

    private int integralPrice; //积分价

    private int quantity; //备货量

    private int num;

    public InventoryInBillItem() {
    }

    public long getSpuid() {
        return spuid;
    }

    public void setSpuid(long spuid) {
        this.spuid = spuid;
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

    public int getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(int retailPrice) {
        this.retailPrice = retailPrice;
    }

    public int getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(int costPrice) {
        this.costPrice = costPrice;
    }

    public int getIntegralPrice() {
        return integralPrice;
    }

    public void setIntegralPrice(int integralPrice) {
        this.integralPrice = integralPrice;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
