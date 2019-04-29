package com.store.system.model;


import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

/**
 * 营销管理-抵用券
 * class_name: MarketingCoupon
 * package: com.store.system.model
 * creat_user: lihao
 * creat_date: 2019/3/12
 * creat_time: 16:33
 **/
@HyperspaceDomain(identityType = IdentityType.origin_indentity, domainType = HyperspaceDomainType.mainDataStructure)
@Data
public class MarketingCoupon implements Serializable {

    public static final int status_nomore=0;//正常
    public static final int status_delete=1;//删除

    public static final int open_yes = 1; //开启
    public static final int open_no = 2; //不开启

    public static final int desc_full_type_money = 1; //金额
    public static final int desc_full_type_num = 2; //个数

    public static final int desc_subtract_type_money = 1; //金额
    public static final int desc_subtract_type_rate = 2; //百分比

    public static final int condition_type_common = 1; //全场通用
    public static final int condition_type_expense_money = 2; //消费-金额
    public static final int condition_type_expense_num = 3; //消费-个数


    @PrimaryKey
    private long id;

    private long subid; //公司ID

    private String title; //标题

    private int descFullType; //描述-消费满 类型(1-金额 2-个数)

    private int descFull; //描述-消费满

    private int descSubtractType; //描述-减 类型(1-金额 2-百分比)

    private double descSubtract; //描述-减

    private int conditionType; //获取条件 类型(1-全场通用 2-消费金额 3-消费个数)

    private int conditionFull; //获取条件-消费满

    private long startTime; //起始时间

    private long endTime; //结束时间

    @SortKey
    private long sort;

    private int open; //是否开启

    private int status; //是否删除s

    private long ctime;

    private long utime;

}
