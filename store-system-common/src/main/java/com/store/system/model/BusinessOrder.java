package com.store.system.model;

import com.quakoo.baseFramework.model.pagination.PagerCursor;
import com.quakoo.space.annotation.domain.HyperspaceColumn;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/***
* 业务订单
* @Param:
* @return:
* @Author: LaoMa
* @Date: 2019/7/24
*/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
@Data
public class BusinessOrder implements Serializable {

    public static final int status_no_pay = 0; //未缴费
    public static final int status_pay = 1; //已缴费
    public static final int status_refund = 2; //已退款

    public static final int type_goods = 1; //商品购买
    public static final int type_medical = 2; //医疗器械商品购买
    public static final int type_other = 3; //其他购买

    public static final int makeStatus_no = 1; //未加工
    public static final int makeStatus_no_pay = 2; //欠款
    public static final int makeStatus_qu_no = 3; //未取货
    public static final int makeStatus_qu_yes = 4; //已完成
    public static final int makeStatus_invalid = 5; //已作废
    public static final int makeStatus_temporary = 6; //临时订单
    public static final int makeStatus_recharge = 7; //充值赠送订单
    public static final int makeStatus_cancel = 8; //取消订单


    @PrimaryKey
    private long id;

    private long uid;// 顾客id

    private long staffId;// 员工id

    private long machinistId;// 加工师id

    private long subId;// 门店id

    private String orderNo; //订单编号

    private String desc; //订单备注

    private String receiptDesc; //小票备注

    private long oiId;//验光单id

    private int originalPrice;//原价(分)

    private int discountPrice;//折后金额(分)

    private int rechargePrice;//充值金额(分)

    private int realPrice;//实收金额(分)

    private int totalPrice;//总金额(分)

    private int outPrice;//未收金额(分)

    private String discount;//折扣

    private long couponId;//营销券id

    private int couponFee;//优惠金额(分)

    private int oddsPrice;//特惠减免(分)
    @HyperspaceColumn(isJson = true)
    private List<OrderSku> skuList = new ArrayList<>();

    @HyperspaceColumn(isJson = true)
    private List<Surcharge> surcharges = new ArrayList<>(); //附加费用

    private int makeStatus;

    private int type;//类型

    private int status; //订单状态

    private long day;//支付完成日期

    @SortKey
    @PagerCursor
    private long ctime;

    private long utime;

}
