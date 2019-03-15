package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;

import java.io.Serializable;

/**
 * 顾客等级
 * class_name: CustomerGrade
 * package: com.store.system.model
 * creat_user: lihao
 * creat_date: 2019/3/14
 * creat_time: 15:24
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure, identityType = IdentityType.origin_indentity)
public class UserGrade implements Serializable, Comparable<UserGrade> {

    @PrimaryKey
    private long id;

    private long subid;

    private String title; //等级称号

    private int conditionScore; //等级条件

    private int substituteScore; //积分抵现-积分数值

    private int substituteMoney; //积分抵现-金额(分)

    private double substituteRate; //积分抵现-最高抵现百分比

    private int gainMoney; //积分获取-金额(分)

    private int gainScore; //积分获取-积分数值

    private double discount; //会员折扣

    @SortKey
    private long ctime;

    private long utime;

    @Override
    public int compareTo(UserGrade other) {
        int res = 0;
        if(this.getConditionScore() - other.getConditionScore() > 0) res = 1;
        else if(this.getConditionScore() - other.getConditionScore() < 0) res = -1;
        if(res == 0) {
            if(this.getId() - other.getId() < 0) res = 1;
            else res = -1;
        }
        return res;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getConditionScore() {
        return conditionScore;
    }

    public void setConditionScore(int conditionScore) {
        this.conditionScore = conditionScore;
    }

    public int getSubstituteScore() {
        return substituteScore;
    }

    public void setSubstituteScore(int substituteScore) {
        this.substituteScore = substituteScore;
    }

    public int getSubstituteMoney() {
        return substituteMoney;
    }

    public void setSubstituteMoney(int substituteMoney) {
        this.substituteMoney = substituteMoney;
    }

    public double getSubstituteRate() {
        return substituteRate;
    }

    public void setSubstituteRate(double substituteRate) {
        this.substituteRate = substituteRate;
    }

    public int getGainMoney() {
        return gainMoney;
    }

    public void setGainMoney(int gainMoney) {
        this.gainMoney = gainMoney;
    }

    public int getGainScore() {
        return gainScore;
    }

    public void setGainScore(int gainScore) {
        this.gainScore = gainScore;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
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

    public long getSubid() {
        return subid;
    }

    public void setSubid(long subid) {
        this.subid = subid;
    }
}
