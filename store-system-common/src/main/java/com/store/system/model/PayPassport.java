package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.human)
@Data
public class PayPassport implements Serializable  {

    @PrimaryKey
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

    private String wxAppid;

    private String wxMerchantId;

    private String wxApiKey;

    private String wxPkcs12CertificateName;

    private String wxOrderNotifyUrl;

    private String wxWapUrl;

    private String wxWapName;


    private String desc;

    private long ctime;

    private long utime;


}
