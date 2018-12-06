package com.store.system.bean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.store.system.model.Goods;
import com.google.common.collect.Lists;
import com.s7.baseFramework.jackson.JsonUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 隐形眼镜
 * class_name: ContactLenses
 * package: com.glasses.bean
 * creat_user: lihao
 * creat_date: 2018/11/28
 * creat_time: 14:13
 **/
public class ContactLenses implements Serializable {

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


    private int type; //类型

    private int changeCircle; //更换周期

    private int pack; //包装

    private List<GlassesInformation> currentRanges; //现货范围

    private List<GlassesInformation> customRanges; //定制范围

    public static Goods toGoods(ContactLenses contactLenses) {
        Goods goods = new Goods();
        goods.setId(contactLenses.getId());
        goods.setType(Goods.type_contact_lenses);
        goods.setGbid(contactLenses.getGbid());
        goods.setGsid(contactLenses.getGsid());
        goods.setGpid(contactLenses.getGpid());
        goods.setName(contactLenses.getName());
        goods.setQualityTime(contactLenses.getQualityTime());
        goods.setPriceType(Goods.price_type_multi);
        String retailPrice = JsonUtils.toJson(Lists.newArrayList());
        if(null != contactLenses.getRetailPrices() && contactLenses.getRetailPrices().size() > 0)
            retailPrice = JsonUtils.toJson(contactLenses.getRetailPrices());
        goods.setRetailPrice(retailPrice);

        String costPrice = JsonUtils.toJson(Lists.newArrayList());
        if(null != contactLenses.getCostPrices() && contactLenses.getCostPrices().size() > 0)
            costPrice = JsonUtils.toJson(contactLenses.getCostPrices());
        goods.setCostPrice(costPrice);

        String integralPrice = JsonUtils.toJson(Lists.newArrayList());
        if(null != contactLenses.getIntegralPrices() && contactLenses.getIntegralPrices().size() > 0)
            integralPrice = JsonUtils.toJson(contactLenses.getIntegralPrices());
        goods.setIntegralPrice(integralPrice);

        goods.setNumType(Goods.num_type_multi);

        String realNum = JsonUtils.toJson(Lists.newArrayList());
        if(null != contactLenses.getRealNums() && contactLenses.getRealNums().size() > 0)
            realNum = JsonUtils.toJson(contactLenses.getRealNums());
        goods.setRealNum(realNum);

        String hangupNum = JsonUtils.toJson(Lists.newArrayList());
        if(null != contactLenses.getHangupNums() && contactLenses.getHangupNums().size() > 0)
            hangupNum = JsonUtils.toJson(contactLenses.getHangupNums());
        goods.setHangupNum(hangupNum);

        goods.setDesc(contactLenses.getDesc());
        goods.setMaxIntegralChangeNum(contactLenses.getMaxIntegralChangeNum());
        goods.setIntegralChangeNum(contactLenses.getIntegralChangeNum());
        goods.setIntegralStartTime(contactLenses.getIntegralStartTime());
        goods.setIntegralEndTime(contactLenses.getIntegralEndTime());
        goods.setCtime(contactLenses.getCtime());
        goods.setUtime(contactLenses.getUtime());

        ContactLenses.OtherFields otherFieldsObj = new ContactLenses.OtherFields();
        otherFieldsObj.setType(contactLenses.getType());
        otherFieldsObj.setChangeCircle(contactLenses.getChangeCircle());
        otherFieldsObj.setPack(contactLenses.getPack());
        otherFieldsObj.setCurrentRanges(contactLenses.getCurrentRanges());
        otherFieldsObj.setCustomRanges(contactLenses.getCustomRanges());
        String otherFields = JsonUtils.toJson(otherFieldsObj);
        goods.setOtherFields(otherFields);

        return goods;
    }

    public static ContactLenses fromGoods(Goods goods) {
        if(goods.getType() != Goods.type_contact_lenses) return null;
        ContactLenses contactLenses = new ContactLenses();
        contactLenses.setId(goods.getId());
        contactLenses.setGbid(goods.getGbid());
        contactLenses.setGsid(goods.getGsid());
        contactLenses.setGpid(goods.getGpid());
        contactLenses.setName(goods.getName());
        contactLenses.setQualityTime(goods.getQualityTime());

        List<GlassesInformation> retailPrices = JsonUtils.fromJson(goods.getRetailPrice(),
                new TypeReference<List<GlassesInformation>>() {});
        contactLenses.setRetailPrices(retailPrices);

        List<GlassesInformation> costPrices = JsonUtils.fromJson(goods.getCostPrice(),
                new TypeReference<List<GlassesInformation>>() {});
        contactLenses.setCostPrices(costPrices);

        List<GlassesInformation> integralPrices = JsonUtils.fromJson(goods.getIntegralPrice(),
                new TypeReference<List<GlassesInformation>>() {});
        contactLenses.setIntegralPrices(integralPrices);

        List<GlassesInformation> realNums = JsonUtils.fromJson(goods.getRealNum(),
                new TypeReference<List<GlassesInformation>>() {});
        contactLenses.setRealNums(realNums);

        List<GlassesInformation> hangupNums = JsonUtils.fromJson(goods.getHangupNum(),
                new TypeReference<List<GlassesInformation>>() {});
        contactLenses.setHangupNums(hangupNums);

        contactLenses.setDesc(goods.getDesc());
        contactLenses.setMaxIntegralChangeNum(goods.getMaxIntegralChangeNum());
        contactLenses.setIntegralChangeNum(goods.getIntegralChangeNum());
        contactLenses.setIntegralStartTime(goods.getIntegralStartTime());
        contactLenses.setIntegralEndTime(goods.getIntegralEndTime());
        contactLenses.setCtime(goods.getCtime());
        contactLenses.setUtime(goods.getUtime());

        ContactLenses.OtherFields otherFields = JsonUtils.fromJson(goods.getOtherFields(),
                new TypeReference<ContactLenses.OtherFields>() {});
        contactLenses.setType(otherFields.getType());
        contactLenses.setPack(otherFields.getPack());
        contactLenses.setChangeCircle(otherFields.getChangeCircle());
        contactLenses.setCurrentRanges(otherFields.getCurrentRanges());
        contactLenses.setCustomRanges(otherFields.getCustomRanges());
        return contactLenses;
    }

    public static void main(String[] args) {
        ContactLenses cl = new ContactLenses();
        cl.setId(1);
        cl.setType(1);
        List<GlassesInformation> ranges = Lists.newArrayList();
        GlassesInformation gi = new GlassesInformation();
        gi.setBall("1");
        gi.setColumn("2");
        gi.setValue(1);
        ranges.add(gi);
        cl.setCustomRanges(ranges);

        List<GlassesInformation> prices = Lists.newArrayList();
        GlassesInformation gi2 = new GlassesInformation();
        gi2.setBall("2");
        gi2.setColumn("2");
        gi2.setValue(10);
        prices.add(gi2);

        GlassesInformation gi3 = new GlassesInformation();
        gi3.setBall("22");
        gi3.setColumn("22");
        gi3.setValue(120);
        prices.add(gi3);
        cl.setCostPrices(prices);

        Goods g = ContactLenses.toGoods(cl);
        System.out.println(g.toString());

        cl = ContactLenses.fromGoods(g);
        System.out.println(cl.toString());

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getChangeCircle() {
        return changeCircle;
    }

    public void setChangeCircle(int changeCircle) {
        this.changeCircle = changeCircle;
    }

    public int getPack() {
        return pack;
    }

    public void setPack(int pack) {
        this.pack = pack;
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
        private int type; //类型
        private int changeCircle; //更换周期
        private int pack; //包装
        private List<GlassesInformation> currentRanges; //现货范围
        private List<GlassesInformation> customRanges; //定制范围


        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getChangeCircle() {
            return changeCircle;
        }

        public void setChangeCircle(int changeCircle) {
            this.changeCircle = changeCircle;
        }

        public int getPack() {
            return pack;
        }

        public void setPack(int pack) {
            this.pack = pack;
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
                    "type=" + type +
                    ", changeCircle=" + changeCircle +
                    ", pack=" + pack +
                    ", currentRanges=" + currentRanges +
                    ", customRanges=" + customRanges +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ContactLenses{" +
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
                ", type=" + type +
                ", changeCircle=" + changeCircle +
                ", pack=" + pack +
                ", currentRanges=" + currentRanges +
                ", customRanges=" + customRanges +
                '}';
    }
}
