package com.store.system.model;

import com.s7.baseFramework.model.pagination.PagerCursor;
import com.s7.space.annotation.domain.CombinationKey;
import com.s7.space.annotation.domain.HyperspaceDomain;
import com.s7.space.annotation.domain.ShardingKey;
import com.s7.space.annotation.domain.SortKey;
import com.s7.space.enums.HyperspaceDomainType;
import com.s7.space.enums.IdentityType;

import java.io.Serializable;


/**
 * 下属单位-、用户
 */
@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure,
        identityType = IdentityType.human)
public class SubordinateUserPool implements Serializable {


    @CombinationKey
    @ShardingKey
    private long sid;


    @CombinationKey
    private long uid;


    private int status;


    @SortKey
    @PagerCursor
    private long ctime;

    private long utime;


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


    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
