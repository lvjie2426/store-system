package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

/**
 * 产品属性值
 * class_name: ProductPropertyValue
 * package: com.store.system.model
 * creat_user: lihao
 * creat_date: 2018/12/6
 * creat_time: 11:11
 **/
@HyperspaceDomain(identityType = IdentityType.origin_indentity, domainType = HyperspaceDomainType.mainDataStructure)
@Data
public class ProductPropertyValue implements Serializable {

    public static final int status_nomore=0;//正常
    public static final int status_delete=1;//删除

    @PrimaryKey
    private long id;

    private long pnid; //属性名ID

    private String content; //显示内容

    @SortKey
    private long sort;

    private int status;

    private long ctime;

    private long utime;

}
