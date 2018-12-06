package com.store.system.bean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.store.system.model.Goods;
import com.s7.baseFramework.jackson.JsonUtils;

import java.io.Serializable;

/**
 * 护理产品
 * class_name: CareProduct
 * package: com.glasses.bean
 * creat_user: lihao
 * creat_date: 2018/11/28
 * creat_time: 14:13
 **/
public class CareProduct implements Serializable {

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


    private int type; //类型

    private int pack; //包装

    private int openLife; //开瓶寿命

    public static Goods toGoods(CareProduct careProduct) {
        Goods goods = new Goods();
        goods.setId(careProduct.getId());
        goods.setType(Goods.type_care_product);
        goods.setGbid(careProduct.getGbid());
        goods.setGsid(careProduct.getGsid());
        goods.setGpid(careProduct.getGpid());
        goods.setName(careProduct.getName());
        goods.setQualityTime(careProduct.getQualityTime());
        goods.setPriceType(Goods.price_type_once);
        goods.setRetailPrice(String.valueOf(careProduct.getRetailPrice()));
        goods.setCostPrice(String.valueOf(careProduct.getCostPrice()));
        goods.setIntegralPrice(String.valueOf(careProduct.getIntegralPrice()));
        goods.setNumType(Goods.num_type_once);
        goods.setRealNum(String.valueOf(careProduct.getRealNum()));
        goods.setHangupNum(String.valueOf(careProduct.getHangupNum()));
        goods.setDesc(careProduct.getDesc());
        goods.setMaxIntegralChangeNum(careProduct.getMaxIntegralChangeNum());
        goods.setIntegralChangeNum(careProduct.getIntegralChangeNum());
        goods.setIntegralStartTime(careProduct.getIntegralStartTime());
        goods.setIntegralEndTime(careProduct.getIntegralEndTime());
        goods.setCtime(careProduct.getCtime());
        goods.setUtime(careProduct.getUtime());

        CareProduct.OtherFields otherFieldsObj = new CareProduct.OtherFields();
        otherFieldsObj.setType(careProduct.getType());
        otherFieldsObj.setPack(careProduct.getPack());
        otherFieldsObj.setOpenLife(careProduct.getOpenLife());
        String otherFields = JsonUtils.toJson(otherFieldsObj);
        goods.setOtherFields(otherFields);
        return goods;
    }

    public static CareProduct fromGoods(Goods goods) {
        if(goods.getType() != Goods.type_care_product) return null;
        CareProduct careProduct = new CareProduct();
        careProduct.setId(goods.getId());
        careProduct.setGbid(goods.getGbid());
        careProduct.setGsid(goods.getGsid());
        careProduct.setGpid(goods.getGpid());
        careProduct.setName(goods.getName());
        careProduct.setQualityTime(goods.getQualityTime());
        careProduct.setRetailPrice(Long.parseLong(goods.getRetailPrice()));
        careProduct.setCostPrice(Long.parseLong(goods.getCostPrice()));
        careProduct.setIntegralPrice(Long.parseLong(goods.getIntegralPrice()));
        careProduct.setRealNum(Integer.parseInt(goods.getRealNum()));
        careProduct.setHangupNum(Integer.parseInt(goods.getHangupNum()));
        careProduct.setDesc(goods.getDesc());
        careProduct.setMaxIntegralChangeNum(goods.getMaxIntegralChangeNum());
        careProduct.setIntegralChangeNum(goods.getIntegralChangeNum());
        careProduct.setIntegralStartTime(goods.getIntegralStartTime());
        careProduct.setIntegralEndTime(goods.getIntegralEndTime());
        careProduct.setCtime(goods.getCtime());
        careProduct.setUtime(goods.getUtime());

        CareProduct.OtherFields otherFields = JsonUtils.fromJson(goods.getOtherFields(),
                new TypeReference<CareProduct.OtherFields>() {});
        careProduct.setType(otherFields.getType());
        careProduct.setPack(otherFields.getPack());
        careProduct.setOpenLife(otherFields.getOpenLife());
        return careProduct;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPack() {
        return pack;
    }

    public void setPack(int pack) {
        this.pack = pack;
    }

    public int getOpenLife() {
        return openLife;
    }

    public void setOpenLife(int openLife) {
        this.openLife = openLife;
    }

    static class OtherFields {
        private int type; //类型
        private int pack; //包装
        private int openLife; //开瓶寿命

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getPack() {
            return pack;
        }

        public void setPack(int pack) {
            this.pack = pack;
        }

        public int getOpenLife() {
            return openLife;
        }

        public void setOpenLife(int openLife) {
            this.openLife = openLife;
        }

        @Override
        public String toString() {
            return "OtherFields{" +
                    "type=" + type +
                    ", pack=" + pack +
                    ", openLife=" + openLife +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CareProduct{" +
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
                ", type=" + type +
                ", pack=" + pack +
                ", openLife=" + openLife +
                '}';
    }

}
