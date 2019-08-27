package com.store.system.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.quakoo.space.annotation.domain.*;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;
import org.apache.oro.util.Cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 营销管理--销售任务--门店 任务 中间表
 * class_name: Mission
 * package: com.store.system.model
 * creat_user: lihaojie
 * creat_date: 2019/5/23
 * creat_time: 10:45
 **/

@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure, identityType = IdentityType.human)
@Data
public class SubordinateMissionPool implements Serializable{

    @CombinationKey
    @ShardingKey
    private long mid;//任务ID

    @CombinationKey
    private long sid;//门店ID

    @HyperspaceColumn(isJson = true)
    private List<Long> oids=new ArrayList<>();//订单ID

    private int number;//实际完成个数

    private int price;//实际完成金额

    private int progress;//店铺完成进度

    @SortKey
    private long ctime;

    private long utime;
}
