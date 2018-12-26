package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;

import java.io.Serializable;

/**
 * 盘点单
 * class_name: InventoryCheckBill
 * package: com.store.system.model
 * creat_user: lihao
 * creat_date: 2018/12/15
 * creat_time: 11:20
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure, identityType = IdentityType.origin_indentity)
public class InventoryCheckBill implements Serializable {

    public static final int status_edit = 0; //编辑状态
    public static final int status_wait_check = 1; //等待盘点
    public static final int status_end = 2; //完结状态

    @PrimaryKey
    private long id;

    private long wid; //仓库ID

    private long createUid; //创建人

    private long initUid; //发起人

    private long checkUid; //审核人

    private String itemsJson; //子条目JSON

    private int status;

    @SortKey
    private long ctime;

    private long utime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWid() {
        return wid;
    }

    public void setWid(long wid) {
        this.wid = wid;
    }

    public long getCreateUid() {
        return createUid;
    }

    public void setCreateUid(long createUid) {
        this.createUid = createUid;
    }

    public long getInitUid() {
        return initUid;
    }

    public void setInitUid(long initUid) {
        this.initUid = initUid;
    }

    public long getCheckUid() {
        return checkUid;
    }

    public void setCheckUid(long checkUid) {
        this.checkUid = checkUid;
    }

    public String getItemsJson() {
        return itemsJson;
    }

    public void setItemsJson(String itemsJson) {
        this.itemsJson = itemsJson;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public long getUtime() {
        return utime;
    }

    public void setUtime(long utime) {
        this.utime = utime;
    }
}
