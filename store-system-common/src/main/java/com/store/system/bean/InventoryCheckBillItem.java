package com.store.system.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 进销存盘点单子条目
 * class_name: InventoryCheckBillItem
 * package: com.store.system.bean
 * creat_user: lihao
 * creat_date: 2018/12/26
 * creat_time: 16:05
 **/
@Data
public class InventoryCheckBillItem implements Serializable {

    private long did;

    private int currentNum; //当前数量

    private int realNum; //实际数量

    public InventoryCheckBillItem() {
    }

}
