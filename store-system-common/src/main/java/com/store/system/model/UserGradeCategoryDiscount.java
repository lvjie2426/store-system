package com.store.system.model;

import com.quakoo.space.annotation.domain.CombinationKey;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.ShardingKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import lombok.Data;

/**
 * @program: store-system
 * @description: 类目折扣表 现在记录的是单类目的折扣信息。
 * @author: zhangmeng
 * @create: 2019-05-15 15:07
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure)
@Data
public class UserGradeCategoryDiscount {

    @CombinationKey
    private long spuId;//类目
    private int discount; //折扣
    @CombinationKey
    @ShardingKey
    private long ugId;//会员等级id
    @SortKey
    private long sort;
    private long ctime;
    private long utime;
}
