package com.store.system.bean;

import com.store.system.model.InBillItem;

import java.io.Serializable;

public class InBillSpecialGoodsItem implements Serializable {

    private long id;

    private long ibid; //入库单ID

    private long gbid; //品牌ID

    private long gsid; //系列ID

    private long gpid; //供应商ID

    private long gid; //商品ID

    private int num;

    private long ctime;

    private long utime;



    private String name; //名称

    public static InBillItem toInBillItem(InBillSpecialGoodsItem one) {
        InBillItem res = new InBillItem();
        res.setId(one.getId());
        res.setType(InBillItem.type_special_goods);
        res.setIbid(one.getIbid());
        res.setGbid(one.getGbid());
        res.setGsid(one.getGsid());
        res.setGpid(one.getGpid());
        res.setGid(one.getGid());
        res.setNum(one.getNum());
        res.setCtime(one.getCtime());
        res.setUtime(one.getUtime());
        res.setName(one.getName());
        return res;
    }

    public static InBillSpecialGoodsItem fromInBillItem(InBillItem one) {
        if(one.getType() != InBillItem.type_special_goods) return null;
        InBillSpecialGoodsItem res = new InBillSpecialGoodsItem();
        res.setId(one.getId());
        res.setIbid(one.getIbid());
        res.setGbid(one.getGbid());
        res.setGsid(one.getGsid());
        res.setGpid(one.getGpid());
        res.setGid(one.getGid());
        res.setNum(one.getNum());
        res.setCtime(one.getCtime());
        res.setUtime(one.getUtime());
        res.setName(one.getName());
        return res;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIbid() {
        return ibid;
    }

    public void setIbid(long ibid) {
        this.ibid = ibid;
    }

    public long getGbid() {
        return gbid;
    }

    public void setGbid(long gbid) {
        this.gbid = gbid;
    }

    public long getGsid() {
        return gsid;
    }

    public void setGsid(long gsid) {
        this.gsid = gsid;
    }

    public long getGpid() {
        return gpid;
    }

    public void setGpid(long gpid) {
        this.gpid = gpid;
    }

    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "InBillSpecialGoodsItem{" +
                "id=" + id +
                ", ibid=" + ibid +
                ", gbid=" + gbid +
                ", gsid=" + gsid +
                ", gpid=" + gpid +
                ", gid=" + gid +
                ", num=" + num +
                ", ctime=" + ctime +
                ", utime=" + utime +
                ", name='" + name + '\'' +
                '}';
    }
}
