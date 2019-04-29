package com.store.system.model;

import com.quakoo.space.annotation.domain.CombinationKey;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.ShardingKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import lombok.Data;

import java.io.Serializable;

@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure)
@Data
public class ProductPropertyValuePool implements Serializable {

    @ShardingKey
    @CombinationKey
    private long subid;

    @CombinationKey
    private long pnid;

    @CombinationKey
    private long pvid;

    @SortKey
    private long sort;

    private long ctime;

    private long utime;

}
