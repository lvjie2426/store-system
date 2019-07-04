package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
@Data
public class PayPassport implements Serializable  {

    public static final int status_on = 0; //启用
    public static final int status_off = 1; //关闭

    @PrimaryKey
    private long id;

    private long subId;//门店ID


    ///////支付宝///////

    private String aliPid; //支付宝合作伙伴身份ID

    private String aliAppid; //应用ID

    private String aliPrivateKey; //私钥

    private String aliPublicKey; //阿里公钥

    private String aliOrderNotifyUrl; //回调地址

    private String aliWapOrderReturnUrl; //wap支付完成返回地址

    private String aliRedPayNotifyUrl; //红包支付回调

    ///////微信///////

    private String wxAppid; //微信合作伙伴帐号

    private String wxMerchantId; //微信合作伙伴应用ID

    private String wxApiKey; //微信私钥

    private String wxPkcs12CertificateName; //微信公钥证书名

    private String wxOrderNotifyUrl; //微信订单回调地址

    private String wxWapUrl;

    private String wxWapName;


    private String desc;

    private int status;

    @SortKey
    private long ctime;

    private long utime;


}
