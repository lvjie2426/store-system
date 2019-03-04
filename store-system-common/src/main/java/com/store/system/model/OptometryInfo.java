package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;

import java.io.Serializable;

/**
 * 验光信息
 * class_name: OptometryInfo
 * package: com.store.system.model
 * creat_user: lihao
 * creat_date: 2019/3/4
 * creat_time: 17:23
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure, identityType = IdentityType.origin_indentity)
public class OptometryInfo implements Serializable {

    public static final int eye_left = 1; //左眼
    public static final int eye_right = 2; //右眼

    public static final int wear_prop_jin_shi = 1; //近视眼镜
    public static final int wear_prop_yuan_shi = 2; //远视眼镜
    public static final int wear_prop_yin_xing = 3; //隐形眼镜

    @PrimaryKey
    private long id;

    private long pSubid; //公司ID

    private long subid; //分店ID

    private long cid; //顾客ID

    private long optUid; //验光师

    @SortKey
    private long optTime; //验光时间

    private String desc; //验光备注

    private String yuanYongJson; //远用

    private String yinXingJson; //隐形

    private String jinYongJson; //近用

    private String jianJinDuoJiaoDianJson; //渐进多焦点

    private int pd; //瞳距

    private int eye; //左右眼

    private int wearProp; //佩戴属性

    private long res; //结果

    private long support; //建议

    private long ctime;

    private long utime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getpSubid() {
        return pSubid;
    }

    public void setpSubid(long pSubid) {
        this.pSubid = pSubid;
    }

    public long getSubid() {
        return subid;
    }

    public void setSubid(long subid) {
        this.subid = subid;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public long getOptUid() {
        return optUid;
    }

    public void setOptUid(long optUid) {
        this.optUid = optUid;
    }

    public long getOptTime() {
        return optTime;
    }

    public void setOptTime(long optTime) {
        this.optTime = optTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getYuanYongJson() {
        return yuanYongJson;
    }

    public void setYuanYongJson(String yuanYongJson) {
        this.yuanYongJson = yuanYongJson;
    }

    public String getYinXingJson() {
        return yinXingJson;
    }

    public void setYinXingJson(String yinXingJson) {
        this.yinXingJson = yinXingJson;
    }

    public String getJinYongJson() {
        return jinYongJson;
    }

    public void setJinYongJson(String jinYongJson) {
        this.jinYongJson = jinYongJson;
    }

    public String getJianJinDuoJiaoDianJson() {
        return jianJinDuoJiaoDianJson;
    }

    public void setJianJinDuoJiaoDianJson(String jianJinDuoJiaoDianJson) {
        this.jianJinDuoJiaoDianJson = jianJinDuoJiaoDianJson;
    }

    public int getPd() {
        return pd;
    }

    public void setPd(int pd) {
        this.pd = pd;
    }

    public int getEye() {
        return eye;
    }

    public void setEye(int eye) {
        this.eye = eye;
    }

    public int getWearProp() {
        return wearProp;
    }

    public void setWearProp(int wearProp) {
        this.wearProp = wearProp;
    }

    public long getRes() {
        return res;
    }

    public void setRes(long res) {
        this.res = res;
    }

    public long getSupport() {
        return support;
    }

    public void setSupport(long support) {
        this.support = support;
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
}
