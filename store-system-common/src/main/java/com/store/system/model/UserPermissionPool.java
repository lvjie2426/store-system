package com.store.system.model;

import com.quakoo.baseFramework.model.pagination.PagerCursor;
import com.quakoo.space.annotation.domain.CombinationKey;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.ShardingKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import lombok.Data;

import java.io.Serializable;


@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure)
@Data
public class UserPermissionPool implements Serializable {

    //权限=（角色权限）+（个人打开的权限）-（个人关闭的权限）
    public static final int type_on=0;//打开此权限
    public static final int type_off=1;//关闭此权限
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @CombinationKey
    @ShardingKey
    private long uid;
    @CombinationKey
    private long pid;


    private int type;


    private long ctime;

    private long utime;

    @PagerCursor
    @SortKey
    private long sort;

}
