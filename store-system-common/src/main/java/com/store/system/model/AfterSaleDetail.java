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
 * @ClassName AfterSaleDetail
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/10 17:39
 * @Version 1.0
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
@Data
public class AfterSaleDetail implements Serializable{

    @PrimaryKey
    private long id;

    private long oid;

    private long asId; //售后记录ID

    private long optId; //操作人ID

    private String reason; //售后原因

    @HyperspaceColumn(isJson = true)
    private List<OrderSku> sku=new ArrayList<>();

    @SortKey
    private long ctime;

    private long utime;
}
