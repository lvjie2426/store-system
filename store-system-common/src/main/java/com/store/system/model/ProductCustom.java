package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;

import java.io.Serializable;

/**
 * 定制商品
 * class_name: ProductCustom
 * package: com.store.system.model
 * creat_user: lihao
 * creat_date: 2019/3/18
 * creat_time: 14:29
 **/
@HyperspaceDomain(identityType = IdentityType.origin_indentity, domainType = HyperspaceDomainType.mainDataStructure)
public class ProductCustom implements Serializable {

    public static final int status_wait_pay = 0; //等待付款
    public static final int status_making = 1; //制作中
    public static final int status_wait_take = 2; //未取货
    public static final int status_end = 3; //已完成
    public static final int status_cancel = 4; //已取消

    @PrimaryKey
    private long id;

    private long soid; //销售订单

    private long psid; //公司ID

    private long sid; //分店ID

    private long spuid; //SPU的ID

    private long skuid; //SKU的ID

    private int num; //定制数量

    private int price; //定制单价

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

    public long getPsid() {
        return psid;
    }

    public void setPsid(long psid) {
        this.psid = psid;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public long getSpuid() {
        return spuid;
    }

    public void setSpuid(long spuid) {
        this.spuid = spuid;
    }

    public long getSkuid() {
        return skuid;
    }

    public void setSkuid(long skuid) {
        this.skuid = skuid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public long getSoid() {
        return soid;
    }

    public void setSoid(long soid) {
        this.soid = soid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
