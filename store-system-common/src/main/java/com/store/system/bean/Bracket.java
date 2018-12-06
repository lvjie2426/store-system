package com.store.system.bean;

import com.store.system.model.Goods;

import java.io.Serializable;

/**
 * 镜架
 * class_name: Bracket
 * package: com.glasses.bean
 * creat_user: lihao
 * creat_date: 2018/11/27
 * creat_time: 16:42
 **/
public class Bracket implements Serializable {

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

    /**
     * method_name: toGoods
     * params: [bracket]
     * return: Goods
     * creat_user: lihao
     * creat_date: 2018/11/27
     * creat_time: 16:42
     **/
    public static Goods toGoods(Bracket bracket) {
        Goods goods = new Goods();
        goods.setId(bracket.getId());
        goods.setType(Goods.type_bracket);
        goods.setGbid(bracket.getGbid());
        goods.setGsid(bracket.getGsid());
        goods.setGpid(bracket.getGpid());
        goods.setName(bracket.getName());
        goods.setQualityTime(bracket.getQualityTime());
        goods.setPriceType(Goods.price_type_once);
        goods.setRetailPrice(String.valueOf(bracket.getRetailPrice()));
        goods.setCostPrice(String.valueOf(bracket.getCostPrice()));
        goods.setIntegralPrice(String.valueOf(bracket.getIntegralPrice()));
        goods.setNumType(Goods.num_type_once);
        goods.setRealNum(String.valueOf(bracket.getRealNum()));
        goods.setHangupNum(String.valueOf(bracket.getHangupNum()));
        goods.setDesc(bracket.getDesc());
        goods.setMaxIntegralChangeNum(bracket.getMaxIntegralChangeNum());
        goods.setIntegralChangeNum(bracket.getIntegralChangeNum());
        goods.setIntegralStartTime(bracket.getIntegralStartTime());
        goods.setIntegralEndTime(bracket.getIntegralEndTime());
        goods.setCtime(bracket.getCtime());
        goods.setUtime(bracket.getUtime());
        return goods;
    }

    /**
     * method_name: fromGoods
     * params: [goods]
     * return: Bracket
     * creat_user: lihao
     * creat_date: 2018/11/27
     * creat_time: 16:42
     **/
    public static Bracket fromGoods(Goods goods) {
        if(goods.getType() != Goods.type_bracket) return null;
        Bracket bracket = new Bracket();
        bracket.setId(goods.getId());
        bracket.setGbid(goods.getGbid());
        bracket.setGsid(goods.getGsid());
        bracket.setGpid(goods.getGpid());
        bracket.setName(goods.getName());
        bracket.setQualityTime(goods.getQualityTime());
        bracket.setRetailPrice(Long.parseLong(goods.getRetailPrice()));
        bracket.setCostPrice(Long.parseLong(goods.getCostPrice()));
        bracket.setIntegralPrice(Long.parseLong(goods.getIntegralPrice()));
        bracket.setRealNum(Integer.parseInt(goods.getRealNum()));
        bracket.setHangupNum(Integer.parseInt(goods.getHangupNum()));
        bracket.setDesc(goods.getDesc());
        bracket.setMaxIntegralChangeNum(goods.getMaxIntegralChangeNum());
        bracket.setIntegralChangeNum(goods.getIntegralChangeNum());
        bracket.setIntegralStartTime(goods.getIntegralStartTime());
        bracket.setIntegralEndTime(goods.getIntegralEndTime());
        bracket.setCtime(goods.getCtime());
        bracket.setUtime(goods.getUtime());
        return bracket;
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

    @Override
    public String toString() {
        return "Bracket{" +
                "id=" + id +
                ", gbid=" + gbid +
                ", gsid=" + gsid +
                ", gpid=" + gpid +
                ", name='" + name + '\'' +
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
                '}';
    }
}
