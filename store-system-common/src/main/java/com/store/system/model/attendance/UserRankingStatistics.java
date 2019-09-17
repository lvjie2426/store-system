package com.store.system.model.attendance;

import com.quakoo.space.annotation.domain.CombinationKey;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.ShardingKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName UserRankingStatistics
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/16 15:41
 * @Version 1.0
 **/
@Data
@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure,
        identityType = IdentityType.human)
public class UserRankingStatistics implements Serializable{

    /**
     * ID
     */
    @CombinationKey
    @ShardingKey
    private long uid;
    /**
     * 月
     */
    @CombinationKey
    private long month;
    /**
     * 年
     */
    @CombinationKey
    private long year;
    /***
     * 累计获得早起鸟次数
     */
    private int times;

    @SortKey
    private long ctime;

    private long utime;


}
