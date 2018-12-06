package com.store.system.bean;

import com.store.system.model.Goods;

import java.io.Serializable;

/**
 * 太阳镜
 * class_name: SunGlasses
 * package: com.glasses.bean
 * creat_user: lihao
 * creat_date: 2018/11/27
 * creat_time: 17:10
 **/
public class SunGlasses implements Serializable {

    private long id;

    private long gbid; //品牌ID

    private long gsid; //系列ID

    private long gpid; //供应商ID

    private String name; //名称

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


    public static Goods toGoods(SunGlasses sunGlasses) {
        Goods goods = new Goods();
        goods.setId(sunGlasses.getId());
        goods.setType(Goods.type_sun_glasses);
        goods.setGbid(sunGlasses.getGbid());
        goods.setGsid(sunGlasses.getGsid());
        goods.setGpid(sunGlasses.getGpid());
        goods.setName(sunGlasses.getName());
        goods.setQualityTime(sunGlasses.getQualityTime());
        goods.setPriceType(Goods.price_type_once);
        goods.setRetailPrice(String.valueOf(sunGlasses.getRetailPrice()));
        goods.setCostPrice(String.valueOf(sunGlasses.getCostPrice()));
        goods.setIntegralPrice(String.valueOf(sunGlasses.getIntegralPrice()));
        goods.setNumType(Goods.num_type_once);
        goods.setRealNum(String.valueOf(sunGlasses.getRealNum()));
        goods.setHangupNum(String.valueOf(sunGlasses.getHangupNum()));
        goods.setDesc(sunGlasses.getDesc());
        goods.setMaxIntegralChangeNum(sunGlasses.getMaxIntegralChangeNum());
        goods.setIntegralChangeNum(sunGlasses.getIntegralChangeNum());
        goods.setIntegralStartTime(sunGlasses.getIntegralStartTime());
        goods.setIntegralEndTime(sunGlasses.getIntegralEndTime());
        goods.setCtime(sunGlasses.getCtime());
        goods.setUtime(sunGlasses.getUtime());
        return goods;
    }

    public static SunGlasses fromGoods(Goods goods) {
        if(goods.getType() != Goods.type_sun_glasses) return null;
        SunGlasses sunGlasses = new SunGlasses();
        sunGlasses.setId(goods.getId());
        sunGlasses.setGbid(goods.getGbid());
        sunGlasses.setGsid(goods.getGsid());
        sunGlasses.setGpid(goods.getGpid());
        sunGlasses.setName(goods.getName());
        sunGlasses.setQualityTime(goods.getQualityTime());
        sunGlasses.setRetailPrice(Long.parseLong(goods.getRetailPrice()));
        sunGlasses.setCostPrice(Long.parseLong(goods.getCostPrice()));
        sunGlasses.setIntegralPrice(Long.parseLong(goods.getIntegralPrice()));
        sunGlasses.setRealNum(Integer.parseInt(goods.getRealNum()));
        sunGlasses.setHangupNum(Integer.parseInt(goods.getHangupNum()));
        sunGlasses.setDesc(goods.getDesc());
        sunGlasses.setMaxIntegralChangeNum(goods.getMaxIntegralChangeNum());
        sunGlasses.setIntegralChangeNum(goods.getIntegralChangeNum());
        sunGlasses.setIntegralStartTime(goods.getIntegralStartTime());
        sunGlasses.setIntegralEndTime(goods.getIntegralEndTime());
        sunGlasses.setCtime(goods.getCtime());
        sunGlasses.setUtime(goods.getUtime());
        return sunGlasses;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
