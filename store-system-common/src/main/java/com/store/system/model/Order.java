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
    public static final int status_expire = 2; //已过期
    public static final int status_refund = 3; //已退款

    public static final int pay_type_ali = 1; //支付宝
    public static final int pay_type_wx = 2; //微信

    public static final int type_goods = 1; //商品购买

    public static final int pay_mode_app = 1; //app支付
    public static final int pay_mode_wap = 2; //手机网页支付
    public static final int pay_mode_public = 3; //公众号支付
    public static final int pay_mode_mini_app = 4; //小程序支付
    public static final int pay_mode_barcode = 5; //条形码支付

    public static final int marketingtype_coupon = 1; //抵用券

    public static final int makestatus_no = 1; //未加工
    public static final int makestatus_no_pay = 2; //欠款
    public static final int makestatus_qu_no = 3; //未取货
    public static final int makestatus_qu_yes = 4; //已完成
    public static final int makestatus_invalid = 5; //已作废
    public static final int makestatus_temporary = 6; //临时订单


    @PrimaryKey
    private long id;
    private long uid;// 顾客id
    private long personnelid;// 员工id
    private long machiningid;// 加工师id
    private long subid;// 门店id

    private long passportId;

    private String orderNo; // 支付系统订单编号

    private int payType; //支付类型

    private int payMode; //支付方式

    private long gmt; //yyyyMMddHHmmss

    private int type; //类型

    private String typeInfo;

    private String title;

    private String desc; //订单备注

    private String receiptDesc; //小票备注

    private int totalPrice;//总金额(分)
    private double discount;//折扣
    private int dicountPrice;//折后金额(分)0
    private int price;//实际支付金额(分)
    private long couponid;//营销券id
    private int marketingType;//促销类型
    private int makeStatus;//加工状态/取货状态
    private long oiId;//验光单id

    @HyperspaceColumn(isJson = true)
    private List<OrderSku> skuids=new ArrayList<>();
    @HyperspaceColumn(isJson = true)
    private List<Surcharge> surcharges=new ArrayList<>(); //附加费用


    private int expireUnitId;

    private int expireNum;

    private long expireTime; //超时时间

    private long payTime; //支付时间

    private int status; //订单状态

    private String authCode; //条形码

    private String detail; //返回详情

    private int arrears;//欠款金额(分)

    @SortKey
    private long ctime;

    private long utime;

}
