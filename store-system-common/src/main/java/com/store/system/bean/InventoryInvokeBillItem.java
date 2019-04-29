package com.store.system.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class InventoryInvokeBillItem implements Serializable {

    private long did;

    private int num;

    public InventoryInvokeBillItem() {
    }

}
