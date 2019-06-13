package com.store.system.model;

import com.quakoo.space.annotation.domain.CombinationKey;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.ShardingKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

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

    public static final int type_personal = 1; //个人
    public static final int type_team = 2; //团体

    @ShardingKey
    @CombinationKey
    private long subId;//门店ID

    @CombinationKey
    private long spuId;//SPUID

    @CombinationKey
    private int type;//个人或团体

    private int price; //(分) 每件商品10元提成
    @SortKey
    private long ctime;

    private long utime;
}
