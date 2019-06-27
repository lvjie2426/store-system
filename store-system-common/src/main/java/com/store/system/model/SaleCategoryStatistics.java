package com.store.system.model;

import com.quakoo.space.annotation.domain.*;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName SaleCategoryStatistics
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/18 11:32
 * @Version 1.0
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure,
        identityType = IdentityType.human)
@Data
public class SaleCategoryStatistics implements Serializable{

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

    private double sale; //产品分类销售额

    private int num; //产品分类销售单数

    private double perPrice; //平均客单价

    @HyperspaceColumn(isJson = true)
    private List<OrderSku> salesLog; //记录

    @SortKey
    private long ctime;

    private long utime;
}
