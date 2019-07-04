package com.store.system.model;

import com.google.common.collect.Lists;
import com.quakoo.baseFramework.model.pagination.PagerCursor;
import com.quakoo.space.annotation.domain.HyperspaceColumn;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 权限初始化模板(子项)。
 */
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
@Data
public class RoleTemplateItem implements Serializable {

    @PrimaryKey
    private long id;

    private String roleName;

    private String remark;

    @HyperspaceColumn(isJson = true)
    private List<Long> pids= Lists.newArrayList();//权限

    private long roleInitTemplateId;

    private long utime;

    @PagerCursor
    @SortKey
    private long ctime;


}
