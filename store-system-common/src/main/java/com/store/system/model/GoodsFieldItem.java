package com.store.system.model;

import com.s7.space.annotation.domain.HyperspaceDomain;
import com.s7.space.annotation.domain.PrimaryKey;
import com.s7.space.annotation.domain.SortKey;
import com.s7.space.enums.HyperspaceDomainType;
import com.s7.space.enums.IdentityType;

import java.io.Serializable;

@HyperspaceDomain(identityType = IdentityType.origin_indentity, domainType = HyperspaceDomainType.mainDataStructure)
public class GoodsFieldItem implements Serializable {

    public static final int status_nomore=0;//正常
    public static final int status_delete=1;//删除

    @PrimaryKey
    private long id;

    private long gfid;

    private String content;

    private int status;

    @SortKey
    private long sort;

    private long ctime;

    private long utime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGfid() {
        return gfid;
    }

    public void setGfid(long gfid) {
        this.gfid = gfid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
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
