package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceColumn;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 营销管理--销售任务
 * class_name: Mission
 * package: com.store.system.model
 * creat_user: lihaojie
 * creat_date: 2019/5/22
 * creat_time: 16:23
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,identityType = IdentityType.origin_indentity)
@Data
public class Mission implements Serializable{

    public static final int status_yes = 0;//正常
    public static final int status_no = 1;//删除

    public static final int missionStatus_nofinish = 1;//进行中
    public static final int missionStatus_finish = 2;//完成

    public static final int type_tem=1;//任务类型--团队
    public static final int type_user=2;//任务类型--个人

    public static final int amountType_number = 1;//完成--销售量-个
    public static final int amountType_money = 2;//完成--销售额-元
    public static final int amountType_allMoney = 3;//全店销售额

    @PrimaryKey
    private long id;

    private String name;

    @HyperspaceColumn(isJson = true)
    private List<Long> executor = new ArrayList<>();//执行任务对象

    private long sid;//公司ID

    private int status;//任务状态0正常  1删除

    private int amount;//奖励金额(分)

    private int fine;//处罚金额(分)

    private int amountType;//奖励类型

    private long startTime;

    private long endTime;

    private int type;//团队 or 个人

    private int missionStatus;//任务状态

    private int target;//完成金额 或者数量 或者总金额

    @HyperspaceColumn(isJson = true)
    private List<Long> skuIds = new ArrayList<>();//任务产品

    @SortKey
    private long ctime;

    private long utime;

}
