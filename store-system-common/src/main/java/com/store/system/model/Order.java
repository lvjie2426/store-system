package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName Order
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/7/23 14:30
 * @Version 1.0
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
@Data
public class Order implements Serializable {

    public static final int status_no_pay = 0; //未缴费
    public static final int status_pay = 1; //已缴费
    public static final int status_expire = 2; //已过期
    public static final int status_refund = 3; //已退款

    public static final int pay_type_ali = 1; //支付宝
    public static final int pay_type_wx = 2; //微信

    public static final int type_goods = 1; //商品购买
    public static final int type_medical = 2; //医疗器械商品购买
    public static final int type_other = 3; //其他购买

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

    private int price;

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
