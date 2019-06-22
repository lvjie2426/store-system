package com.store.system.model;

import com.quakoo.space.annotation.domain.CombinationKey;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.ShardingKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**销售额统计
 * @ClassName SaleStatistics
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/15 10:46
 * @Version 1.0
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure,
        identityType = IdentityType.human)
@Data
public class SaleStatistics implements Serializable{

    @CombinationKey
    @ShardingKey
    private long subId;

    @CombinationKey
    private long day;

    @CombinationKey
    private long week;

    @CombinationKey
    private long month;


    private double sale; //销售额

    private int num; //销售单数

    private double perPrice; //平均客单价

    private double profits;//毛利润

    @SortKey
    private long ctime;

    private long utime;

}
