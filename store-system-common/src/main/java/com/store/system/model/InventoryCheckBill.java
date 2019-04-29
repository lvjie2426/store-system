package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceColumn;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import com.store.system.bean.InventoryCheckBillItem;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 盘点单
 * class_name: InventoryCheckBill
 * package: com.store.system.model
 * creat_user: lihao
 * creat_date: 2018/12/15
 * creat_time: 11:20
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure, identityType = IdentityType.origin_indentity)
@Data
public class InventoryCheckBill implements Serializable {

    public static final int status_edit = 0; //编辑状态
    public static final int status_wait_check = 1; //等待盘点
    public static final int status_end = 2; //完结状态

    @PrimaryKey
    private long id;

    private long subid; //店铺ID

    private long wid; //仓库ID

    private long createUid; //创建人

    private long initUid; //发起人

    private long checkUid; //盘点人

    @HyperspaceColumn(isJson = true)
    private List<InventoryCheckBillItem> items; //子条目JSON

    private int status;

    @SortKey
    private long ctime;

    private long utime;

}
