package com.store.system.model;

import com.s7.space.annotation.domain.HyperspaceDomain;
import com.s7.space.annotation.domain.PrimaryKey;
import com.s7.space.enums.HyperspaceDomainType;
import com.s7.space.enums.IdentityType;

import java.io.Serializable;

@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure, identityType = IdentityType.origin_indentity)
public class Goods implements Serializable {

    public static final int type_lens = 1; //镜片
    public static final int type_bracket = 2; //镜架
    public static final int type_contact_lenses = 3; //隐形眼镜
    public static final int type_sun_glasses = 4; //太阳镜
    public static final int type_care_product = 5; //护理产品
    public static final int type_special_goods = 6; //特殊商品

    public static final int status_nomore=0;//正常
    public static final int status_delete=1;//删除

    public static final int price_type_once = 0; //统一价格
    public static final int price_type_multi = 1; //多种价格

    public static final int num_type_once = 0; //统一数量
    public static final int num_type_multi = 1; //多种数量

    @PrimaryKey
    private long id;

    private long gbid; //品牌ID

    private long gsid; //系列ID

    private long gpid; //供应商ID

    private int type; //商品类型

    private String name; //名称

    private String icon; //图标

    private int qualityTime; //质保时长(天)

    private int priceType;  //价格类型

    private String retailPrice; //零售价(分)

    private String costPrice; //成本价(分)

    private String integralPrice; //积分价

    private String desc; //描述

    private int numType; //数量类型

    private String realNum;  //实际数量

    private String hangupNum; //挂起数量

    private int maxIntegralChangeNum; //兑换数量上限

    private int integralChangeNum; //兑换数量

    private long integralStartTime; //积分有效开始时间

    private long integralEndTime; //积分有效结束时间

    private String otherFields; //其他属性

    private int status;

    private long ctime;

    private long utime;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getQualityTime() {
        return qualityTime;
    }

    public void setQualityTime(int qualityTime) {
        this.qualityTime = qualityTime;
    }

    public int getPriceType() {
        return priceType;
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getIntegralPrice() {
        return integralPrice;
    }

    public void setIntegralPrice(String integralPrice) {
        this.integralPrice = integralPrice;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getNumType() {
        return numType;
    }

    public void setNumType(int numType) {
        this.numType = numType;
    }

    public String getRealNum() {
        return realNum;
    }

    public void setRealNum(String realNum) {
        this.realNum = realNum;
    }

    public String getHangupNum() {
        return hangupNum;
    }

    public void setHangupNum(String hangupNum) {
        this.hangupNum = hangupNum;
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

    public String getOtherFields() {
        return otherFields;
    }

    public void setOtherFields(String otherFields) {
        this.otherFields = otherFields;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", gbid=" + gbid +
                ", gsid=" + gsid +
                ", gpid=" + gpid +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", qualityTime=" + qualityTime +
                ", priceType=" + priceType +
                ", retailPrice='" + retailPrice + '\'' +
                ", costPrice='" + costPrice + '\'' +
                ", integralPrice='" + integralPrice + '\'' +
                ", desc='" + desc + '\'' +
                ", numType=" + numType +
                ", realNum='" + realNum + '\'' +
                ", hangupNum='" + hangupNum + '\'' +
                ", maxIntegralChangeNum=" + maxIntegralChangeNum +
                ", integralChangeNum=" + integralChangeNum +
                ", integralStartTime=" + integralStartTime +
                ", integralEndTime=" + integralEndTime +
                ", otherFields='" + otherFields + '\'' +
                ", status=" + status +
                ", ctime=" + ctime +
                ", utime=" + utime +
                '}';
    }
}
