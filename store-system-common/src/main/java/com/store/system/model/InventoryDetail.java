package com.store.system.model;

import com.quakoo.baseFramework.model.pagination.PagerCursor;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

/**
 * 进销存仓库明细
 * class_name: InventoryDetail
 * package: com.store.system.model
 * creat_user: lihao
 * creat_date: 2018/12/10
 * creat_time: 18:01
 **/
@HyperspaceDomain(identityType = IdentityType.origin_indentity, domainType = HyperspaceDomainType.mainDataStructure)
@Data
public class InventoryDetail implements Serializable {

    public static final int status_normal = 0; //正常
    public static final int status_past = 1; //过期

    @PrimaryKey
    private long id;

    private long subid; //店铺ID

    private long wid; //仓库id

    private long p_cid; //产品类目的id

    private long p_spuid; //产品SPU的id

    private long p_skuid; //产品SKU的id

    private int num;

    private int status;

    @SortKey
    @PagerCursor
    private long ctime;

    private long utime;

}
