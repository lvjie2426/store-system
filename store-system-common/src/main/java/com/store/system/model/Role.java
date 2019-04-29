package com.store.system.model;

import com.quakoo.baseFramework.model.pagination.PagerCursor;
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
public class Role implements Serializable {

    @PrimaryKey
    private long id;

    private String roleName;

    private String remark;

    @PagerCursor
    @SortKey
    private long sort = System.currentTimeMillis();

    private long sid;//下级单位ID

    private long utime;

    private long ctime;

}
