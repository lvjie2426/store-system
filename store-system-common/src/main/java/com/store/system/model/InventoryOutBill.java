package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;

import java.io.Serializable;

/**
 * 进销存出库单
 * class_name: InventoryOutBill
 * package: com.store.system.model
 * creat_user: lihao
 * creat_date: 2019/1/1
 * creat_time: 16:15
 **/
@HyperspaceDomain(identityType = IdentityType.origin_indentity, domainType = HyperspaceDomainType.mainDataStructure)
public class InventoryOutBill implements Serializable {

    public static final int status_edit = 0; //编辑状态
    public static final int status_wait_check = 1; //等待审核
    public static final int status_end = 2; //完结状态

    public static final int check_pass = 1; //审核通过
    public static final int check_no_pass = 2; //审核未通过

    @PrimaryKey
    private long id;

    private long subid; //店铺ID

    private long wid; //仓库ID

    private long outUid; //出库人

    private long createUid; //创建人

    private long checkUid; //审核人

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

    public long getWid() {
        return wid;
    }

    public void setWid(long wid) {
        this.wid = wid;
    }

    public long getOutUid() {
        return outUid;
    }

    public void setOutUid(long outUid) {
        this.outUid = outUid;
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

    public long getSubid() {
        return subid;
    }

    public void setSubid(long subid) {
        this.subid = subid;
    }
}