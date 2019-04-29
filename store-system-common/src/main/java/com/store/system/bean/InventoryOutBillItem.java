package com.store.system.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class InventoryOutBillItem implements Serializable {

    private long did;

    private int num;

    public InventoryOutBillItem() {
    }

}
