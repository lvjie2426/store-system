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
 * @program: store-system
 * @description: 类目折扣表 现在记录的是单类目的折扣信息。
 * @author: zhangmeng
 * @create: 2019-05-15 15:07
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure, identityType = IdentityType.human)
@Data
public class UserGradeCategoryDiscount implements Serializable{
    @CombinationKey
    @ShardingKey
    private long ugid;//会员等级id
    @CombinationKey
    private long spuid;//spuid
    private double discount; //折扣

    @SortKey
    private long ctime;
    private long utime;
}
