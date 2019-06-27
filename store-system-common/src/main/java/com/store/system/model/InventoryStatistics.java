package com.store.system.model;

import com.quakoo.space.annotation.domain.CombinationKey;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.ShardingKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName InventoryStatistics
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/27 15:46
 * @Version 1.0
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure,
        identityType = IdentityType.human)
@Data
public class InventoryStatistics implements Serializable{

    @CombinationKey
    @ShardingKey
    private long subId;

    @CombinationKey
    private long cid;

    @CombinationKey
    private long day;

    @CombinationKey
    private long week;

    @CombinationKey
    private long month;

    private int num; //库存总数

    private double price; //金额

    @SortKey
    private long ctime;

    private long utime;
}
