package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;

import java.io.Serializable;

@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
public class Order implements Serializable {

    public static final int status_no_pay = 0; //未缴费
    public static final int status_pay = 1; //已缴费
    public static final int status_expire = 2; //已过期
    public static final int status_refund = 3; //已退款

    public static final int pay_type_ali = 1; //支付宝
    public static final int pay_type_wx = 2; //微信

    public static final int pay_mode_app = 1; //app支付
    public static final int pay_mode_wap = 2; //手机网页支付
    public static final int pay_mode_public = 3; //公众号支付
    public static final int pay_mode_mini_app = 4; //小程序支付
    public static final int pay_mode_barcode = 5; //条形码支付


    @PrimaryKey
    private long id;

    private long passportId;

    private String orderNo; // 支付系统订单编号

    private int payType; //支付类型

    private int payMode; //支付方式

    private long gmt; //yyyyMMddHHmmss

    private int type; //类型

    private String typeInfo;

    private String title;

    private String desc; //描述

    private double price;

    private int expireUnitId;

    private int expireNum;

    private long expireTime; //超时时间

    private long payTime; //支付时间

    private int status; //订单状态

    private String authCode; //条形码

    private String detail; //返回详情

    @SortKey
    private long ctime;

    private long utime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public long getGmt() {
        return gmt;
    }

    public void setGmt(long gmt) {
        this.gmt = gmt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeInfo() {
        return typeInfo;
    }

    public void setTypeInfo(String typeInfo) {
        this.typeInfo = typeInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
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

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public long getPassportId() {
        return passportId;
    }

    public void setPassportId(long passportId) {
        this.passportId = passportId;
    }

    public int getExpireUnitId() {
        return expireUnitId;
    }

    public void setExpireUnitId(int expireUnitId) {
        this.expireUnitId = expireUnitId;
    }

    public int getExpireNum() {
        return expireNum;
    }

    public void setExpireNum(int expireNum) {
        this.expireNum = expireNum;
    }

    public int getPayMode() {
        return payMode;
    }

    public void setPayMode(int payMode) {
        this.payMode = payMode;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", passportId=" + passportId +
                ", orderNo='" + orderNo + '\'' +
                ", payType=" + payType +
                ", payMode=" + payMode +
                ", gmt=" + gmt +
                ", type=" + type +
                ", typeInfo='" + typeInfo + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", price=" + price +
                ", expireUnitId=" + expireUnitId +
                ", expireNum=" + expireNum +
                ", expireTime=" + expireTime +
                ", payTime=" + payTime +
                ", status=" + status +
                ", authCode='" + authCode + '\'' +
                ", ctime=" + ctime +
                ", utime=" + utime +
                '}';
    }

}
