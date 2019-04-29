package com.store.system.model;

import com.quakoo.baseFramework.model.pagination.PagerCursor;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

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
@Data
public class RoleInitTemplate implements Serializable {

    @PrimaryKey
    private long id;

    private String name;

    private String remark;

    private long utime;

    @PagerCursor
    @SortKey
    private long ctime;

}
