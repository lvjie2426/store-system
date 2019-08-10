package com.store.system.model;

import com.quakoo.space.annotation.domain.*;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Commission
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/13 19:10
 * @Version 1.0
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure,
        identityType = IdentityType.human)
@Data
public class Commission implements Serializable{

    @ShardingKey
    @CombinationKey
    private long subId;//门店ID

    @CombinationKey
    private long spuId;//SPUID

    @HyperspaceColumn(isJson = true)
    private int users; //个人提成

    private int price; //团队提成
    @SortKey
    private long ctime;

    private long utime;
}
