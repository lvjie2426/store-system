package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

/**
 * 进销存仓库
 * class_name: InventoryWarehouse
 * package: com.store.system.model
 * creat_user: lihao
 * creat_date: 2018/12/10
 * creat_time: 17:59
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure, identityType = IdentityType.origin_indentity)
@Data
public class InventoryWarehouse implements Serializable {

    public static final int status_nomore=0;//正常
    public static final int status_delete=1;//删除

    public static final int type_common = 0; //普通仓库
    public static final int type_give = 1; //赠品仓库

    @PrimaryKey
    private long id;

    private long subid;

    private int type;

    private String name;

    private long adminid; //管理员id

    private int status;

    @SortKey
    private long ctime;

    private long utime;

}
