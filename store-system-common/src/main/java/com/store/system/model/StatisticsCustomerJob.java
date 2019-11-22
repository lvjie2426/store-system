package com.store.system.model;

import com.quakoo.space.annotation.domain.*;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;
import org.apache.commons.collections.map.HashedMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private int total;//总数

    private int returnNum;//回头客人数

    private int oldNum;//老顾客人数

    @HyperspaceColumn(isJson = true)
    private List<Integer> age = new ArrayList<>();//年龄

    //男 年龄数组
    @HyperspaceColumn(isJson = true)
    private Map<String,Object> manAge = new HashedMap();
    //女 年龄数组
    @HyperspaceColumn(isJson = true)
    private Map<String,Object> womanAge = new HashedMap();

    @SortKey
    private long ctime;

    private long utime;
}
