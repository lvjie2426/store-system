package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

/**支付明细
 * @ClassName PayInfo
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/7/23 16:39
 * @Version 1.0
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
@Data
public class PayInfo implements Serializable{

    public static final int pay_type_ali = 1; //支付宝
    public static final int pay_type_wx = 2; //微信
    public static final int pay_type_cash = 3; //现金
    public static final int pay_type_stored = 4; //储值

    public static final int status_pay = 1; //已缴费
    public static final int status_refund = 2; //已退款


    @PrimaryKey
    private long id;

    private long subId;

    private long uid;

    private long boId;  //业务订单ID

    private int payType; //支付类型

    private int price;  //支付金额

    private int status;

    @SortKey
    private long ctime;

    private long utime;



}
