package com.store.system.model;

import com.s7.space.annotation.domain.HyperspaceDomain;
import com.s7.space.annotation.domain.PrimaryKey;
import com.s7.space.enums.HyperspaceDomainType;
import com.s7.space.enums.IdentityType;

import java.io.Serializable;

/**
 * 入库单
 * class_name: InBill
 * package: com.glasses.model
 * creat_user: lihao
 * creat_date: 2018/12/3
 * creat_time: 12:05
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure, identityType = IdentityType.origin_indentity)
public class InBill implements Serializable {

    public static final int status_edit = 0; //编辑状态
    public static final int status_wait_check = 1; //等待确认
    public static final int status_end = 2; //已确认
    public static final int status_delete = 3; //删除

    @PrimaryKey
    private long id;

    private long inUid; //入库人

    private long createUid; //创建人

    private int status;

    private long ctime;

    private long utime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInUid() {
        return inUid;
    }

    public void setInUid(long inUid) {
        this.inUid = inUid;
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

    public long getCreateUid() {
        return createUid;
    }

    public void setCreateUid(long createUid) {
        this.createUid = createUid;
    }
}
