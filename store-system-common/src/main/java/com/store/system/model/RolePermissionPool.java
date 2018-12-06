package com.store.system.model;

import com.s7.baseFramework.model.pagination.PagerCursor;
import com.s7.space.annotation.domain.CombinationKey;
import com.s7.space.annotation.domain.HyperspaceDomain;
import com.s7.space.annotation.domain.ShardingKey;
import com.s7.space.annotation.domain.SortKey;
import com.s7.space.enums.HyperspaceDomainType;

import java.io.Serializable;


@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure)
public class RolePermissionPool implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @CombinationKey
    @ShardingKey
    private long rid;
    @CombinationKey
    private long pid;


    private long ctime;

    private long utime;

    @PagerCursor
    @SortKey
    private long sort;

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
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

    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
    }

}
