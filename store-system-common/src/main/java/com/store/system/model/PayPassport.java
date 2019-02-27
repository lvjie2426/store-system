package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;

import java.io.Serializable;

@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.human)
public class PayPassport implements Serializable  {

    @PrimaryKey
    private long id;

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

    public String getAliRedPayNotifyUrl() {
        return aliRedPayNotifyUrl;
    }

    public void setAliRedPayNotifyUrl(String aliRedPayNotifyUrl) {
        this.aliRedPayNotifyUrl = aliRedPayNotifyUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAliPid() {
        return aliPid;
    }

    public void setAliPid(String aliPid) {
        this.aliPid = aliPid;
    }

    public String getAliAppid() {
        return aliAppid;
    }

    public void setAliAppid(String aliAppid) {
        this.aliAppid = aliAppid;
    }

    public String getAliPrivateKey() {
        return aliPrivateKey;
    }

    public void setAliPrivateKey(String aliPrivateKey) {
        this.aliPrivateKey = aliPrivateKey;
    }

    public String getAliPublicKey() {
        return aliPublicKey;
    }

    public void setAliPublicKey(String aliPublicKey) {
        this.aliPublicKey = aliPublicKey;
    }

    public String getAliOrderNotifyUrl() {
        return aliOrderNotifyUrl;
    }

    public void setAliOrderNotifyUrl(String aliOrderNotifyUrl) {
        this.aliOrderNotifyUrl = aliOrderNotifyUrl;
    }

    public String getWxAppid() {
        return wxAppid;
    }

    public void setWxAppid(String wxAppid) {
        this.wxAppid = wxAppid;
    }

    public String getWxMerchantId() {
        return wxMerchantId;
    }

    public void setWxMerchantId(String wxMerchantId) {
        this.wxMerchantId = wxMerchantId;
    }

    public String getWxApiKey() {
        return wxApiKey;
    }

    public void setWxApiKey(String wxApiKey) {
        this.wxApiKey = wxApiKey;
    }

    public String getWxPkcs12CertificateName() {
        return wxPkcs12CertificateName;
    }

    public void setWxPkcs12CertificateName(String wxPkcs12CertificateName) {
        this.wxPkcs12CertificateName = wxPkcs12CertificateName;
    }

    public String getWxOrderNotifyUrl() {
        return wxOrderNotifyUrl;
    }

    public void setWxOrderNotifyUrl(String wxOrderNotifyUrl) {
        this.wxOrderNotifyUrl = wxOrderNotifyUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public long getUtime() {
        return utime;
    }

    public void setUtime(long utime) {
        this.utime = utime;
    }

    public String getAliWapOrderReturnUrl() {
        return aliWapOrderReturnUrl;
    }

    public void setAliWapOrderReturnUrl(String aliWapOrderReturnUrl) {
        this.aliWapOrderReturnUrl = aliWapOrderReturnUrl;
    }

    public String getWxWapUrl() {
        return wxWapUrl;
    }

    public void setWxWapUrl(String wxWapUrl) {
        this.wxWapUrl = wxWapUrl;
    }

    public String getWxWapName() {
        return wxWapName;
    }

    public void setWxWapName(String wxWapName) {
        this.wxWapName = wxWapName;
    }
}
