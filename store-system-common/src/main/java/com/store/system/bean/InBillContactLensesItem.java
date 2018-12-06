package com.store.system.bean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.store.system.model.InBillItem;
import com.s7.baseFramework.jackson.JsonUtils;

import java.io.Serializable;

public class InBillContactLensesItem implements Serializable {

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



    private String ball; //球

    private String column; //柱

    private String productNum; //生产批号

    private long shelfLifeStartTime; //保质期开始时间

    private long ShelfLifeEndTime; //保质期结束时间

    public static InBillItem toInBillItem(InBillContactLensesItem one) {
        InBillItem res = new InBillItem();
        res.setId(one.getId());
        res.setType(InBillItem.type_contact_lenses);
        res.setIbid(one.getIbid());
        res.setGbid(one.getGbid());
        res.setGsid(one.getGsid());
        res.setGpid(one.getGpid());
        res.setGid(one.getGid());
        res.setName(one.getName());
        res.setNum(one.getNum());
        res.setCtime(one.getCtime());
        res.setUtime(one.getUtime());

        InBillContactLensesItem.OtherFields otherFieldsObj = new InBillContactLensesItem.OtherFields();
        otherFieldsObj.setBall(one.getBall());
        otherFieldsObj.setColumn(one.getColumn());
        otherFieldsObj.setProductNum(one.getProductNum());
        otherFieldsObj.setShelfLifeStartTime(one.getShelfLifeStartTime());
        otherFieldsObj.setShelfLifeEndTime(one.getShelfLifeEndTime());
        String otherFields = JsonUtils.toJson(otherFieldsObj);
        res.setOtherFields(otherFields);
        return res;
    }

    public static InBillContactLensesItem fromInBillItem(InBillItem one) {
        if(one.getType() != InBillItem.type_contact_lenses) return null;
        InBillContactLensesItem res = new InBillContactLensesItem();
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

        InBillContactLensesItem.OtherFields otherFields = JsonUtils.fromJson(one.getOtherFields(),
                new TypeReference<InBillContactLensesItem.OtherFields>() {});
        res.setBall(otherFields.getBall());
        res.setColumn(otherFields.getColumn());
        res.setProductNum(otherFields.getProductNum());
        res.setShelfLifeStartTime(otherFields.getShelfLifeStartTime());
        res.setShelfLifeEndTime(otherFields.getShelfLifeEndTime());
        return res;
    }

    static class OtherFields {

        private String ball; //球
        private String column; //柱
        private String productNum; //生产批号
        private long shelfLifeStartTime; //保质期开始时间
        private long ShelfLifeEndTime; //保质期结束时间

        public String getBall() {
            return ball;
        }

        public void setBall(String ball) {
            this.ball = ball;
        }

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
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

        @Override
        public String toString() {
            return "OtherFields{" +
                    "ball='" + ball + '\'' +
                    ", column='" + column + '\'' +
                    ", productNum='" + productNum + '\'' +
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

    public String getBall() {
        return ball;
    }

    public void setBall(String ball) {
        this.ball = ball;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "InBillContactLensesItem{" +
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
                ", ball='" + ball + '\'' +
                ", column='" + column + '\'' +
                ", productNum='" + productNum + '\'' +
                ", shelfLifeStartTime=" + shelfLifeStartTime +
                ", ShelfLifeEndTime=" + ShelfLifeEndTime +
                '}';
    }
}
