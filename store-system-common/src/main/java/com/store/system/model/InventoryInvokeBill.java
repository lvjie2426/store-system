package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;

import java.io.Serializable;

/**
 * 进销存调货单
 * class_name: InventoryInvokeBill
 * package: com.store.system.model
 * creat_user: lihao
 * creat_date: 2019/1/1
 * creat_time: 16:12
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure, identityType = IdentityType.origin_indentity)
public class InventoryInvokeBill implements Serializable {

    public static final int status_edit = 0; //编辑状态
    public static final int status_wait_check = 1; //等待审核
    public static final int status_end = 2; //完结状态

    public static final int check_pass = 1; //审核通过
    public static final int check_no_pass = 2; //审核未通过

    @PrimaryKey
    private long id;

    private long inSubid; //要入库的店铺ID

    private long inWid; //要入库的仓库ID

    private long outSubid; //要出库的店铺ID

    private long outWid; //要出库的仓库ID

    private long createUid; //创建人

    private long checkUid; //审核人

    private long outUid; //出库人

    private long inUid; //入库人

    private String itemsJson; //子条目JSON

    private int status;

    private int check; //审核状态

    @SortKey
    private long ctime;

    private long utime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInSubid() {
        return inSubid;
    }

    public void setInSubid(long inSubid) {
        this.inSubid = inSubid;
    }

    public long getInWid() {
        return inWid;
    }

    public void setInWid(long inWid) {
        this.inWid = inWid;
    }

    public long getOutSubid() {
        return outSubid;
    }

    public void setOutSubid(long outSubid) {
        this.outSubid = outSubid;
    }

    public long getOutWid() {
        return outWid;
    }

    public void setOutWid(long outWid) {
        this.outWid = outWid;
    }

    public long getCreateUid() {
        return createUid;
    }

    public void setCreateUid(long createUid) {
        this.createUid = createUid;
    }

    public long getCheckUid() {
        return checkUid;
    }

    public void setCheckUid(long checkUid) {
        this.checkUid = checkUid;
    }

    public long getOutUid() {
        return outUid;
    }

    public void setOutUid(long outUid) {
        this.outUid = outUid;
    }

    public long getInUid() {
        return inUid;
    }

    public void setInUid(long inUid) {
        this.inUid = inUid;
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

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
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
