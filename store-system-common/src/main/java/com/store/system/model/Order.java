package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceColumn;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
@Data
public class Order implements Serializable {

    public static final int status_no_pay = 0; //未缴费
    public static final int status_pay = 1; //已缴费
    public static final int status_expire = 2; //作废
    public static final int status_no_ok = 3; //未完成：包含未取货，未加工，欠500（拿走货，没给钱）

    public static final int pay_type_ali = 1; //支付宝
    public static final int pay_type_wx = 2; //微信

    public static final int pay_mode_app = 1; //app支付
    public static final int pay_mode_wap = 2; //手机网页支付
    public static final int pay_mode_public = 3; //公众号支付
    public static final int pay_mode_mini_app = 4; //小程序支付
    public static final int pay_mode_barcode = 5; //条形码支付

    public static final int marketingtype_coupon = 1; //抵用券

    public static final int makestatus_no = 1; //未加工
    public static final int makestatus_qu_no = 3; //未取货
    public static final int makestatus_qu_yes = 4; //已取货


    @PrimaryKey
    private long id;
    private long uid;// 顾客id
    private long personnelid;// 员工id

    private long passportId;

    private String orderNo; // 支付系统订单编号

    private int payType; //支付类型

    private int payMode; //支付方式

    private long gmt; //yyyyMMddHHmmss

    private int type; //类型

    private String typeInfo;

    private String title;

    private String desc; //描述

    private double totalPrice;//总金额
    private double price;//实际支付金额
    private long couponid;//营销券id
    private int marketingType;//促销类型
    private int MakeStatus;//加工状态/取货状态

    @HyperspaceColumn(isJson = true)
    //skuids; (long-skuid,object-num,price)
    private List<OrderSku> skuids=new ArrayList<>();


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

}
