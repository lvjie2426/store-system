package com.store.system.model;

import com.quakoo.space.annotation.domain.*;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: store-system
 * @Author: LiHaoJie
 * @Description: 顾客注册人数日统计
 * @Date: 2019/6/15 15:42
 * @Version: 1.0
 */
@Data
@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure,
        identityType = IdentityType.human)
public class StatisticsCustomerJob implements Serializable{

    @ShardingKey
    @CombinationKey
    private long subid;//门店ID

    @CombinationKey
    private int day;

    @CombinationKey
    private int week;//某年的第几周

    @CombinationKey
    private int month;

    private int man;//男

    private int woman;//女

    @HyperspaceColumn(isJson = true)
    private List<Integer> age = new ArrayList<>();//年龄

    @SortKey
    private long ctime;

    private long utime;
}
