package com.store.system.model;

import com.quakoo.baseFramework.model.pagination.PagerCursor;
import com.quakoo.space.annotation.domain.CombinationKey;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.ShardingKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import lombok.Data;

import java.io.Serializable;


@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure)
@Data
public class RolePermissionPool implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @CombinationKey
    @ShardingKey
    private long rid;
    @CombinationKey
    private long pid;


    private long ctime;

    private long utime;

    @PagerCursor
    @SortKey
    private long sort;

}
