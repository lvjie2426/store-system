package com.store.system.model;

import com.s7.space.annotation.domain.HyperspaceDomain;
import com.s7.space.annotation.domain.PrimaryKey;
import com.s7.space.annotation.domain.SortKey;
import com.s7.space.enums.HyperspaceDomainType;
import com.s7.space.enums.IdentityType;

import java.io.Serializable;

@HyperspaceDomain(identityType = IdentityType.origin_indentity, domainType = HyperspaceDomainType.mainDataStructure)
public class GoodsProvider implements Serializable {

    public static final int status_nomore=0;//正常
    public static final int status_delete=1;//删除

    @PrimaryKey
    private long id;

    private String name;

    private String icon;

    private String desc;

    @SortKey
    private long sort;

    private int status;

    private long ctime;

    private long utime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
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
