package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;


/**
 * 下属单位
 */
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
@Data
public class Subordinate implements Serializable {

    public static final int status_online = 0;//线上
    public static final int status_delete = 1;//删除

    //@Excel(name = "id", height = 20, width = 30, isImportField = "true_st")
    @PrimaryKey
    private long id;

    private long pid; //父类ID(0：公司，非0：分店)

    //@Excel(name = "名称", height = 20, width = 30, isImportField = "true_st")
    private String name;

    private String cover;
    //@Excel(name = "介绍", height = 20, width = 30, isImportField = "true_st")
    private String content;//介绍

    private String desc;
    //@Excel(name = "手机号", height = 20, width = 30, isImportField = "true_st")
    private long phone;
    //@Excel(name = "logo", height = 20, width = 30, isImportField = "true_st")
    private String icon;

    private String address;

    private long adminId;//管理员ID

    private String adminPhone;//管理员手机号，手机号+验证码登录时候用

    private String adminUserName;//管理员账号名 账号名+密码登录时候用

    private String adminPassword;//管理员密码 账号名+密码登录时候用

    private int status;

    private long province;//省

    private long city;//城市

    private long roleInitTemplateId;//角色模板ID

    @SortKey
    private long ctime;

    private long utime;

}
