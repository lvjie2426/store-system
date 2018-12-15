package com.store.system.model;

import com.s7.space.annotation.domain.HyperspaceDomain;
import com.s7.space.annotation.domain.PrimaryKey;
import com.s7.space.annotation.domain.SortKey;
import com.s7.space.enums.HyperspaceDomainType;
import com.s7.space.enums.IdentityType;

import java.io.Serializable;

/**
 * 进销存仓库明细
 * class_name: InventoryDetail
 * package: com.store.system.model
 * creat_user: lihao
 * creat_date: 2018/12/10
 * creat_time: 18:01
 **/
@HyperspaceDomain(identityType = IdentityType.origin_indentity, domainType = HyperspaceDomainType.mainDataStructure)
public class InventoryDetail implements Serializable {

    @PrimaryKey
    private long id;

    private long wid; //仓库id

    private long p_cid; //产品类目的id

    private long p_spuid; //产品SPU的id

    private long p_skuid; //产品SKU的id

    private int num;

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

    public long getP_skuid() {
        return p_skuid;
    }

    public void setP_skuid(long p_skuid) {
        this.p_skuid = p_skuid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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

    public long getP_spuid() {
        return p_spuid;
    }

    public void setP_spuid(long p_spuid) {
        this.p_spuid = p_spuid;
    }

    public long getP_cid() {
        return p_cid;
    }

    public void setP_cid(long p_cid) {
        this.p_cid = p_cid;
    }
}
