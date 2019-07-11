package com.store.system.model;

import com.quakoo.baseFramework.model.pagination.PagerCursor;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

@Data
@HyperspaceDomain(identityType = IdentityType.origin_indentity,
        domainType = HyperspaceDomainType.mainDataStructure)
public class WalletDetail implements Serializable {

    public static final int type_in = 1;
    public static final int type_out = 2;

    @PrimaryKey
    private long id;

    private long uid;

    private int type;

    private double money;

    private String detail;

    private String desc;

    @SortKey
    @PagerCursor
    private long ctime;

    private long utime;

}
