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
public class ProductCategoryPool implements Serializable {

    @ShardingKey
    @CombinationKey
    private long subid;

    @CombinationKey
    private long cid;

    @SortKey
    private long sort;

    private long ctime;

    private long utime;


}
