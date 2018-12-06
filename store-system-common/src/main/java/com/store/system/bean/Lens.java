package com.store.system.bean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.store.system.model.Goods;
import com.google.common.collect.Lists;
import com.s7.baseFramework.jackson.JsonUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 镜片
 * class_name: Lens
 * package: com.glasses.bean
 * creat_user: lihao
 * creat_date: 2018/11/28
 * creat_time: 14:12
 **/
public class Lens implements Serializable {

    private long id;

    private long gbid; //品牌ID

    private long gsid; //系列ID

    private long gpid; //供应商ID

    private String name; //名称

    private int qualityTime; //质保时长(天)

    private List<GlassesInformation> retailPrices; //零售价(分)

    private List<GlassesInformation> costPrices; //成本价(分)

    private List<GlassesInformation> integralPrices; //积分价

    private List<GlassesInformation> realNums;  //实际数量

    private List<GlassesInformation> hangupNums; //挂起数量

    private String desc; //描述

    private int maxIntegralChangeNum; //兑换数量上限

    private int integralChangeNum; //兑换数量

    private long integralStartTime; //积分有效开始时间

    private long integralEndTime; //积分有效结束时间

    private long ctime;

    private long utime;


    private int focus; //焦点

    private int refractive; //折射率

    private int function; //功能

    private List<GlassesInformation> currentRanges; //现货范围

    private List<GlassesInformation> customRanges; //定制范围

    public static Goods toGoods(Lens lens) {
        Goods goods = new Goods();
        goods.setId(lens.getId());
        goods.setType(Goods.type_lens);
        goods.setGbid(lens.getGbid());
        goods.setGsid(lens.getGsid());
        goods.setGpid(lens.getGpid());
        goods.setName(lens.getName());
        goods.setQualityTime(lens.getQualityTime());
        goods.setPriceType(Goods.price_type_multi);
        String retailPrice = JsonUtils.toJson(Lists.newArrayList());
        if(null != lens.getRetailPrices() && lens.getRetailPrices().size() > 0)
            retailPrice = JsonUtils.toJson(lens.getRetailPrices());
        goods.setRetailPrice(retailPrice);

        String costPrice = JsonUtils.toJson(Lists.newArrayList());
        if(null != lens.getCostPrices() && lens.getCostPrices().size() > 0)
            costPrice = JsonUtils.toJson(lens.getCostPrices());
        goods.setCostPrice(costPrice);

        String integralPrice = JsonUtils.toJson(Lists.newArrayList());
        if(null != lens.getIntegralPrices() && lens.getIntegralPrices().size() > 0)
            integralPrice = JsonUtils.toJson(lens.getIntegralPrices());
        goods.setIntegralPrice(integralPrice);

        goods.setNumType(Goods.num_type_multi);

        String realNum = JsonUtils.toJson(Lists.newArrayList());
        if(null != lens.getRealNums() && lens.getRealNums().size() > 0)
            realNum = JsonUtils.toJson(lens.getRealNums());
        goods.setRealNum(realNum);

        String hangupNum = JsonUtils.toJson(Lists.newArrayList());
        if(null != lens.getHangupNums() && lens.getHangupNums().size() > 0)
            hangupNum = JsonUtils.toJson(lens.getHangupNums());
        goods.setHangupNum(hangupNum);

        goods.setDesc(lens.getDesc());
        goods.setMaxIntegralChangeNum(lens.getMaxIntegralChangeNum());
        goods.setIntegralChangeNum(lens.getIntegralChangeNum());
        goods.setIntegralStartTime(lens.getIntegralStartTime());
        goods.setIntegralEndTime(lens.getIntegralEndTime());
        goods.setCtime(lens.getCtime());
        goods.setUtime(lens.getUtime());

        Lens.OtherFields otherFieldsObj = new Lens.OtherFields();
        otherFieldsObj.setFocus(lens.getFocus());
        otherFieldsObj.setFunction(lens.getFunction());
        otherFieldsObj.setRefractive(lens.getRefractive());
        otherFieldsObj.setCurrentRanges(lens.getCurrentRanges());
        otherFieldsObj.setCustomRanges(lens.getCustomRanges());
        String otherFields = JsonUtils.toJson(otherFieldsObj);
        goods.setOtherFields(otherFields);
        return goods;
    }

    public static Lens fromGoods(Goods goods) {
        if(goods.getType() != Goods.type_lens) return null;
        Lens lens = new Lens();
        lens.setId(goods.getId());
        lens.setGbid(goods.getGbid());
        lens.setGsid(goods.getGsid());
        lens.setGpid(goods.getGpid());
        lens.setName(goods.getName());
        lens.setQualityTime(goods.getQualityTime());

        List<GlassesInformation> retailPrices = JsonUtils.fromJson(goods.getRetailPrice(),
                new TypeReference<List<GlassesInformation>>() {});
        lens.setRetailPrices(retailPrices);

        List<GlassesInformation> costPrices = JsonUtils.fromJson(goods.getCostPrice(),
                new TypeReference<List<GlassesInformation>>() {});
        lens.setCostPrices(costPrices);

        List<GlassesInformation> integralPrices = JsonUtils.fromJson(goods.getIntegralPrice(),
                new TypeReference<List<GlassesInformation>>() {});
        lens.setIntegralPrices(integralPrices);

        List<GlassesInformation> realNums = JsonUtils.fromJson(goods.getRealNum(),
                new TypeReference<List<GlassesInformation>>() {});
        lens.setRealNums(realNums);

        List<GlassesInformation> hangupNums = JsonUtils.fromJson(goods.getHangupNum(),
                new TypeReference<List<GlassesInformation>>() {});
        lens.setHangupNums(hangupNums);

        lens.setDesc(goods.getDesc());
        lens.setMaxIntegralChangeNum(goods.getMaxIntegralChangeNum());
        lens.setIntegralChangeNum(goods.getIntegralChangeNum());
        lens.setIntegralStartTime(goods.getIntegralStartTime());
        lens.setIntegralEndTime(goods.getIntegralEndTime());
        lens.setCtime(goods.getCtime());
        lens.setUtime(goods.getUtime());

        Lens.OtherFields otherFields = JsonUtils.fromJson(goods.getOtherFields(),
                new TypeReference<Lens.OtherFields>() {});
        lens.setFocus(otherFields.getFocus());
        lens.setFunction(otherFields.getFunction());
        lens.setRefractive(otherFields.getRefractive());
        lens.setCurrentRanges(otherFields.getCurrentRanges());
        lens.setCustomRanges(otherFields.getCustomRanges());
        return lens;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQualityTime(int qualityTime) {
        this.qualityTime = qualityTime;
    }

    public List<GlassesInformation> getRetailPrices() {
        return retailPrices;
    }

    public void setRetailPrices(List<GlassesInformation> retailPrices) {
        this.retailPrices = retailPrices;
    }

    public List<GlassesInformation> getCostPrices() {
        return costPrices;
    }

    public void setCostPrices(List<GlassesInformation> costPrices) {
        this.costPrices = costPrices;
    }

    public List<GlassesInformation> getIntegralPrices() {
        return integralPrices;
    }

    public void setIntegralPrices(List<GlassesInformation> integralPrices) {
        this.integralPrices = integralPrices;
    }

    public List<GlassesInformation> getRealNums() {
        return realNums;
    }

    public void setRealNums(List<GlassesInformation> realNums) {
        this.realNums = realNums;
    }

    public List<GlassesInformation> getHangupNums() {
        return hangupNums;
    }

    public void setHangupNums(List<GlassesInformation> hangupNums) {
        this.hangupNums = hangupNums;
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

    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }

    public int getRefractive() {
        return refractive;
    }

    public void setRefractive(int refractive) {
        this.refractive = refractive;
    }

    public int getFunction() {
        return function;
    }

    public void setFunction(int function) {
        this.function = function;
    }

    public List<GlassesInformation> getCurrentRanges() {
        return currentRanges;
    }

    public void setCurrentRanges(List<GlassesInformation> currentRanges) {
        this.currentRanges = currentRanges;
    }

    public List<GlassesInformation> getCustomRanges() {
        return customRanges;
    }

    public void setCustomRanges(List<GlassesInformation> customRanges) {
        this.customRanges = customRanges;
    }

    static class OtherFields {
        private int focus; //焦点
        private int refractive; //折射率
        private int function; //功能
        private List<GlassesInformation> currentRanges; //现货范围
        private List<GlassesInformation> customRanges; //定制范围

        public int getFocus() {
            return focus;
        }

        public void setFocus(int focus) {
            this.focus = focus;
        }

        public int getRefractive() {
            return refractive;
        }

        public void setRefractive(int refractive) {
            this.refractive = refractive;
        }

        public int getFunction() {
            return function;
        }

        public void setFunction(int function) {
            this.function = function;
        }

        public List<GlassesInformation> getCurrentRanges() {
            return currentRanges;
        }

        public void setCurrentRanges(List<GlassesInformation> currentRanges) {
            this.currentRanges = currentRanges;
        }

        public List<GlassesInformation> getCustomRanges() {
            return customRanges;
        }

        public void setCustomRanges(List<GlassesInformation> customRanges) {
            this.customRanges = customRanges;
        }

        @Override
        public String toString() {
            return "OtherFields{" +
                    "focus=" + focus +
                    ", refractive=" + refractive +
                    ", function=" + function +
                    ", currentRanges=" + currentRanges +
                    ", customRanges=" + customRanges +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Lens{" +
                "id=" + id +
                ", gbid=" + gbid +
                ", gsid=" + gsid +
                ", gpid=" + gpid +
                ", name='" + name + '\'' +
                ", qualityTime=" + qualityTime +
                ", retailPrices=" + retailPrices +
                ", costPrices=" + costPrices +
                ", integralPrices=" + integralPrices +
                ", realNums=" + realNums +
                ", hangupNums=" + hangupNums +
                ", desc='" + desc + '\'' +
                ", maxIntegralChangeNum=" + maxIntegralChangeNum +
                ", integralChangeNum=" + integralChangeNum +
                ", integralStartTime=" + integralStartTime +
                ", integralEndTime=" + integralEndTime +
                ", ctime=" + ctime +
                ", utime=" + utime +
                ", focus=" + focus +
                ", refractive=" + refractive +
                ", function=" + function +
                ", currentRanges=" + currentRanges +
                ", customRanges=" + customRanges +
                '}';
    }
}
