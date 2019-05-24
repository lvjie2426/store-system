package com.store.system.model;

import com.quakoo.space.annotation.domain.*;
import com.quakoo.space.enums.HyperspaceDomainType;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 营销管理--销售任务--个人 任务 中间表
 * class_name: Mission
 * package: com.store.system.model
 * creat_user: lihaojie
 * creat_date: 2019/5/23
 * creat_time: 10:45
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure)
@Data
public class UserMissionPool implements Serializable {
    @ShardingKey
    @CombinationKey
    private long mid;//任务ID

    @CombinationKey
    private long uid;//员工ID

    @HyperspaceColumn(isDbColumn = true)
    private List<Long> oids=new ArrayList<>();//订单ID

    private int number;//实际完成个数

    private int price;//实际完成金额

    private int progress;//个人完成进度

    @SortKey
    private long ctime;

    private long utime;
}
