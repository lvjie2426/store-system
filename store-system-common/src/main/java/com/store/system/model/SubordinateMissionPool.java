package com.store.system.model;

import com.quakoo.space.annotation.domain.CombinationKey;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.ShardingKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 营销管理--销售任务--门店 任务 中间表
 * class_name: Mission
 * package: com.store.system.model
 * creat_user: lihaojie
 * creat_date: 2019/5/23
 * creat_time: 10:45
 **/

@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure)
@Data
public class SubordinateMissionPool implements Serializable{

    @ShardingKey
    @CombinationKey
    private int mid;//任务ID

    @CombinationKey
    private int sid;//企业ID

    private List<Long> oids;//订单ID

    private int number;//实际完成个数

    private int price;//实际完成金额

    private int progress;//店铺完成进度

    @SortKey
    private long ctime;

    private long utime;
}