package com.store.system.model;

import com.quakoo.space.annotation.domain.CombinationKey;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.ShardingKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

/**支付方式
 * @ClassName Payment
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/5/16 16:00
 * @Version 1.0
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure,
        identityType = IdentityType.human)
@Data
public class Payment implements Serializable{

    public static final int pay_type_ali = 1; //支付宝
    public static final int pay_type_wx = 2; //微信

    public static final int type_public = 1; //公共
    public static final int tyoe_personal = 2; //个人

    public static final int status_on = 0; //启用
    public static final int status_off = 1; //关闭

    @CombinationKey
    @ShardingKey
    private long subid; //门店ID
    @CombinationKey
    private long psid; //所属企业ID
    @CombinationKey
    private int payType; //支付类型
    @CombinationKey
    private int type;

    private String merchantsWx; //商户微信号
    private int merchantsWxStatus; //商户微信号启用状态

    private String subMerchantsWx; //子商户微信号
    private int subMerchantsWxStatus; //子商户微信号启用状态

    private String personalWx; //个人收款微信号
    private int personalWxStatus; //个人收款微信号启用状态

    private String merchantsAli; //商户支付宝账号
    private int merchantsAliStatus; //商户支付宝账号启用状态

    private String personalAli; //个人收款支付宝账号
    private int personalAliStatus; //个人收款支付宝账号启用状态

    @SortKey
    private long ctime;

    private long utime;


}
