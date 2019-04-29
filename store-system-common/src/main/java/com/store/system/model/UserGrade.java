package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

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
@Data
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

}
