package com.store.system.model;

import com.quakoo.baseFramework.model.pagination.PagerCursor;
import com.quakoo.space.annotation.domain.CombinationKey;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.ShardingKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;


/**
 * 下属单位-、用户
 */
@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure,
        identityType = IdentityType.human)
@Data
public class SubordinateUserPool implements Serializable {

    @CombinationKey
    @ShardingKey
    private long sid;

    @CombinationKey
    private long uid;

    private int status;

    @SortKey
    @PagerCursor
    private long ctime;

    private long utime;

}
