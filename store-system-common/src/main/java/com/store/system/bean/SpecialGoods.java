package com.store.system.bean;

import com.store.system.model.Goods;

import java.io.Serializable;

/**
 * 特殊商品
 * class_name: SpecialGoods
 * package: com.glasses.bean
 * creat_user: lihao
 * creat_date: 2018/11/28
 * creat_time: 14:53
 **/
public class SpecialGoods implements Serializable {

    private long id;

    private long gbid; //品牌ID

    private long gsid; //系列ID

    private long gpid; //供应商ID

    private String name; //名称

    private String icon; //图标

    private int qualityTime; //质保时长(天)

    private long retailPrice; //零售价(分)

    private long costPrice; //成本价(分)

    private long integralPrice; //积分价

    private int realNum;  //实际数量

    private int hangupNum; //挂起数量

    private String desc; //描述

    private int maxIntegralChangeNum; //兑换数量上限

    private int integralChangeNum; //兑换数量

    private long integralStartTime; //积分有效开始时间

    private long integralEndTime; //积分有效结束时间

    private long ctime;

    private long utime;


    public static Goods toGoods(SpecialGoods specialGoods) {
        Goods goods = new Goods();
        goods.setId(specialGoods.getId());
        goods.setType(Goods.type_special_goods);
        goods.setGbid(specialGoods.getGbid());
        goods.setGsid(specialGoods.getGsid());
        goods.setGpid(specialGoods.getGpid());
        goods.setName(specialGoods.getName());
        goods.setIcon(specialGoods.getIcon());
        goods.setQualityTime(specialGoods.getQualityTime());
        goods.setPriceType(Goods.price_type_once);
        goods.setRetailPrice(String.valueOf(specialGoods.getRetailPrice()));
        goods.setCostPrice(String.valueOf(specialGoods.getCostPrice()));
        goods.setIntegralPrice(String.valueOf(specialGoods.getIntegralPrice()));
        goods.setNumType(Goods.num_type_once);
        goods.setRealNum(String.valueOf(specialGoods.getRealNum()));
        goods.setHangupNum(String.valueOf(specialGoods.getHangupNum()));
        goods.setDesc(specialGoods.getDesc());
        goods.setMaxIntegralChangeNum(specialGoods.getMaxIntegralChangeNum());
        goods.setIntegralChangeNum(specialGoods.getIntegralChangeNum());
        goods.setIntegralStartTime(specialGoods.getIntegralStartTime());
        goods.setIntegralEndTime(specialGoods.getIntegralEndTime());
        goods.setCtime(specialGoods.getCtime());
        goods.setUtime(specialGoods.getUtime());
        return goods;
    }

    public static SpecialGoods fromGoods(Goods goods) {
        if(goods.getType() != Goods.type_special_goods) return null;
        SpecialGoods specialGoods = new SpecialGoods();
        specialGoods.setId(goods.getId());
        specialGoods.setGbid(goods.getGbid());
        specialGoods.setGsid(goods.getGsid());
        specialGoods.setGpid(goods.getGpid());
        specialGoods.setName(goods.getName());
        specialGoods.setIcon(goods.getIcon());
        specialGoods.setQualityTime(goods.getQualityTime());
        specialGoods.setRetailPrice(Long.parseLong(goods.getRetailPrice()));
        specialGoods.setCostPrice(Long.parseLong(goods.getCostPrice()));
        specialGoods.setIntegralPrice(Long.parseLong(goods.getIntegralPrice()));
        specialGoods.setRealNum(Integer.parseInt(goods.getRealNum()));
        specialGoods.setHangupNum(Integer.parseInt(goods.getHangupNum()));
        specialGoods.setDesc(goods.getDesc());
        specialGoods.setMaxIntegralChangeNum(goods.getMaxIntegralChangeNum());
        specialGoods.setIntegralChangeNum(goods.getIntegralChangeNum());
        specialGoods.setIntegralStartTime(goods.getIntegralStartTime());
        specialGoods.setIntegralEndTime(goods.getIntegralEndTime());
        specialGoods.setCtime(goods.getCtime());
        specialGoods.setUtime(goods.getUtime());
        return specialGoods;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getQualityTime() {
        return qualityTime;
    }

    public void setQualityTime(int qualityTime) {
        this.qualityTime = qualityTime;
    }

    public long getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(long retailPrice) {
        this.retailPrice = retailPrice;
    }

    public long getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(long costPrice) {
        this.costPrice = costPrice;
    }

    public long getIntegralPrice() {
        return integralPrice;
    }

    public void setIntegralPrice(long integralPrice) {
        this.integralPrice = integralPrice;
    }

    public int getRealNum() {
        return realNum;
    }

    public void setRealNum(int realNum) {
        this.realNum = realNum;
    }

    public int getHangupNum() {
        return hangupNum;
    }

    public void setHangupNum(int hangupNum) {
        this.hangupNum = hangupNum;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getMaxIntegralChangeNum() {
        return maxIntegralChangeNum;
    }

    public void setMaxIntegralChangeNum(int maxIntegralChangeNum) {
        this.maxIntegralChangeNum = maxIntegralChangeNum;
    }

    public int getIntegralChangeNum() {
        return integralChangeNum;
    }

    public void setIntegralChangeNum(int integralChangeNum) {
        this.integralChangeNum = integralChangeNum;
    }

    public long getIntegralStartTime() {
        return integralStartTime;
    }

    public void setIntegralStartTime(long integralStartTime) {
        this.integralStartTime = integralStartTime;
    }

    public long getIntegralEndTime() {
        return integralEndTime;
    }

    public void setIntegralEndTime(long integralEndTime) {
        this.integralEndTime = integralEndTime;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "SpecialGoods{" +
                "id=" + id +
                ", gbid=" + gbid +
                ", gsid=" + gsid +
                ", gpid=" + gpid +
                ", qualityTime=" + qualityTime +
                ", retailPrice=" + retailPrice +
                ", costPrice=" + costPrice +
                ", integralPrice=" + integralPrice +
                ", realNum=" + realNum +
                ", hangupNum=" + hangupNum +
                ", desc='" + desc + '\'' +
                ", maxIntegralChangeNum=" + maxIntegralChangeNum +
                ", integralChangeNum=" + integralChangeNum +
                ", integralStartTime=" + integralStartTime +
                ", integralEndTime=" + integralEndTime +
                ", ctime=" + ctime +
                ", utime=" + utime +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
