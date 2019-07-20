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
 * @ProjectName: store-system
 * @Author: LiHaoJie
 * @Description: 任务提成奖励记录
 * @Date: 2019/7/18 10:37
 * @Version: 1.0
 */
@Data
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,identityType = IdentityType.origin_indentity)
public class CommissionReward implements Serializable{

    /**
     * 任务奖励类型
     */
    public static final int type_mission = 1;
    /**
     * 商品提成奖励
     */
    public static final int type_reward = 2;

    @PrimaryKey
    private long id;

    /**
     * 用户ID
     */
    private long uid;

    /**
     * 门店ID
     */
    private long sid;

    /**
     * 任务ID type为1时有值
     */
    private long mid;

    /**
     * 商品详情 type为2时有值
     */
    @HyperspaceColumn(isJson = true)
    private List<OrderSku> skuList = new ArrayList<>();

    /**
     * 类型
     */
    private int type;

    /**
     * 提成或任务奖励
     */
    private int price;

    @SortKey
    private long ctime;

    private long utime;

}
