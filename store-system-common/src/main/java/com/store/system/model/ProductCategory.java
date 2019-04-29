package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

/**
 * 产品类目
 * class_name: ProductCategory
 * package: com.store.system.model
 * creat_user: lihao
 * creat_date: 2018/12/6
 * creat_time: 10:55
 **/
@HyperspaceDomain(identityType = IdentityType.origin_indentity, domainType = HyperspaceDomainType.mainDataStructure)
@Data
public class ProductCategory implements Serializable {

    public static final int status_nomore=0;//正常
    public static final int status_delete=1;//删除

    @PrimaryKey
    private long id;

    private long pid; //父类目id

    private String name; //类目名称

    private int status;

    @SortKey
    private long ctime;

    private long utime;

}
