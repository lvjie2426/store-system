package com.store.system.bean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.store.system.model.InBillItem;
import com.s7.baseFramework.jackson.JsonUtils;

import java.io.Serializable;

public class InBillCareProductItem implements Serializable {

    private long id;

    private long ibid; //入库单ID

    private long gbid; //品牌ID

    private long gsid; //系列ID

    private long gpid; //供应商ID

    private long gid; //商品ID

    private String name; //商品名称

    private int num;

    private long ctime;

    private long utime;



    private String productNum; //生产批号

    private long shelfLifeStartTime; //保质期开始时间

    private long ShelfLifeEndTime; //保质期结束时间

    public static InBillItem toInBillItem(InBillCareProductItem one) {
        InBillItem res = new InBillItem();
        res.setId(one.getId());
        res.setType(InBillItem.type_care_product);
        res.setIbid(one.getIbid());
        res.setGbid(one.getGbid());
        res.setGsid(one.getGsid());
        res.setGpid(one.getGpid());
        res.setGid(one.getGid());
        res.setName(one.getName());
        res.setNum(one.getNum());
        res.setCtime(one.getCtime());
        res.setUtime(one.getUtime());

        InBillCareProductItem.OtherFields otherFieldsObj = new InBillCareProductItem.OtherFields();
        otherFieldsObj.setProductNum(one.getProductNum());
        otherFieldsObj.setShelfLifeStartTime(one.getShelfLifeStartTime());
        otherFieldsObj.setShelfLifeEndTime(one.getShelfLifeEndTime());
        String otherFields = JsonUtils.toJson(otherFieldsObj);
        res.setOtherFields(otherFields);
        return res;
    }

    public static InBillCareProductItem fromInBillItem(InBillItem one) {
        if(one.getType() != InBillItem.type_care_product) return null;
        InBillCareProductItem res = new InBillCareProductItem();
        res.setId(one.getId());
        res.setIbid(one.getIbid());
        res.setGbid(one.getGbid());
        res.setGsid(one.getGsid());
        res.setGpid(one.getGpid());
        res.setGid(one.getGid());
        res.setName(one.getName());
        res.setNum(one.getNum());
        res.setCtime(one.getCtime());
        res.setUtime(one.getUtime());

        InBillCareProductItem.OtherFields otherFields = JsonUtils.fromJson(one.getOtherFields(),
                new TypeReference<InBillCareProductItem.OtherFields>() {});
        res.setProductNum(otherFields.getProductNum());
        res.setShelfLifeStartTime(otherFields.getShelfLifeStartTime());
        res.setShelfLifeEndTime(otherFields.getShelfLifeEndTime());
        return res;
    }

    static class OtherFields {

        private String productNum; //生产批号
        private long shelfLifeStartTime; //保质期开始时间
        private long ShelfLifeEndTime; //保质期结束时间

        public String getProductNum() {
            return productNum;
        }

        public void setProductNum(String productNum) {
            this.productNum = productNum;
        }

        public long getShelfLifeStartTime() {
            return shelfLifeStartTime;
        }

        public void setShelfLifeStartTime(long shelfLifeStartTime) {
            this.shelfLifeStartTime = shelfLifeStartTime;
        }

        public long getShelfLifeEndTime() {
            return ShelfLifeEndTime;
        }

        public void setShelfLifeEndTime(long shelfLifeEndTime) {
            ShelfLifeEndTime = shelfLifeEndTime;
        }

        @Override
        public String toString() {
            return "OtherFields{" +
                    "productNum='" + productNum + '\'' +
                    ", shelfLifeStartTime=" + shelfLifeStartTime +
                    ", ShelfLifeEndTime=" + ShelfLifeEndTime +
                    '}';
        }
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

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public long getShelfLifeStartTime() {
        return shelfLifeStartTime;
    }

    public void setShelfLifeStartTime(long shelfLifeStartTime) {
        this.shelfLifeStartTime = shelfLifeStartTime;
    }

    public long getShelfLifeEndTime() {
        return ShelfLifeEndTime;
    }

    public void setShelfLifeEndTime(long shelfLifeEndTime) {
        ShelfLifeEndTime = shelfLifeEndTime;
    }

    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "InBillCareProductItem{" +
                "id=" + id +
                ", ibid=" + ibid +
                ", gbid=" + gbid +
                ", gsid=" + gsid +
                ", gpid=" + gpid +
                ", gid=" + gid +
                ", name='" + name + '\'' +
                ", num=" + num +
                ", ctime=" + ctime +
                ", utime=" + utime +
                ", productNum='" + productNum + '\'' +
                ", shelfLifeStartTime=" + shelfLifeStartTime +
                ", ShelfLifeEndTime=" + ShelfLifeEndTime +
                '}';
    }
}
