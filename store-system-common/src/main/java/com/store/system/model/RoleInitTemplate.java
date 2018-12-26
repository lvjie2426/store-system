package com.store.system.model;

import com.quakoo.baseFramework.model.pagination.PagerCursor;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;

import java.io.Serializable;


/**
 * 权限初始化模板。
 * 比如下属单位 初始化一个权限模板名字叫做小型学校，中型学校，大型学校
 * 小型学校包含：校长、主任、教师
 * 中型学校包含：校长、系主任、班主任、教师
 * 大型学校包含：校长、副校长、校区领导、年级主任、系主任、班主任、教师、宿管、保卫
 */
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
public class RoleInitTemplate implements Serializable {

    @PrimaryKey
    private long id;

    private String name;

    private String remark;

    private long utime;

    @PagerCursor
    @SortKey
    private long ctime;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public long getUtime() {
        return utime;
    }

    public void setUtime(long utime) {
        this.utime = utime;
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
}
