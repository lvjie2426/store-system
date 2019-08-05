package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

/** 结算
 * @ClassName Settlement
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/7/8 15:10
 * @Version 1.0
 **/
@Data
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
public class Settlement implements Serializable {
    public static final int status_normal=0;//正常
    public static final int status_delete=1;//删除

    @PrimaryKey
    private long id;
    //门店ID
    private long subId;
    //结算人
    private long optId;
    //销售额
    private double sale;
    //现金
    private double cash;
    //阿里支付
    private double ali;
    //微信支付
    private double wx;
    //其他
    private double other;
    //总金额
    private double total;
    //预留金
    private double obligate;
    //上缴现金
    private double payMoney;
    //差额
    private double balanceMoney;
    //现金单数
    private int num;
    //结算开始时间
    private long startTime;
    //结算截至时间
    private long endTime;
    //0正常 1删除
    private int status;

    @SortKey
    private long ctime;

    private long utime;

}
