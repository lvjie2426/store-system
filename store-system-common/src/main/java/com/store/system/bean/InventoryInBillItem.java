package com.store.system.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 进销存入库单子条目
 * class_name: InventoryInBillItem
 * package: com.store.system.bean
 * creat_user: lihao
 * creat_date: 2018/12/11
 * creat_time: 11:24
 **/
@Data
public class InventoryInBillItem implements Serializable {

    private long spuid;

    private String code; //产品编码

    private Map<Long, Object> properties; //属性json

    private int retailPrice; //零售价(分)

    private int costPrice; //成本价(分)

    private int integralPrice; //积分价

    private int num;

    public InventoryInBillItem() {
    }

}
