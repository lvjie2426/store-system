package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;

import java.io.Serializable;


/**
 * 下属单位
 */
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
public class Subordinate implements Serializable {

    public static final int status_online = 0;//线上
    public static final int status_delete = 1;//删除

    //@Excel(name = "id", height = 20, width = 30, isImportField = "true_st")
    @PrimaryKey
    private long id;

    private long pid; //父类ID

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


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getUtime() {
        return utime;
    }

    public void setUtime(long utime) {
        this.utime = utime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getProvince() {
        return province;
    }

    public void setProvince(long province) {
        this.province = province;
    }

    public long getCity() {
        return city;
    }

    public void setCity(long city) {
        this.city = city;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    public String getAdminPhone() {
        return adminPhone;
    }

    public void setAdminPhone(String adminPhone) {
        this.adminPhone = adminPhone;
    }

    public String getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getRoleInitTemplateId() {
        return roleInitTemplateId;
    }

    public void setRoleInitTemplateId(long roleInitTemplateId) {
        this.roleInitTemplateId = roleInitTemplateId;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }
}
