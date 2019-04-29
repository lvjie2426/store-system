package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceColumn;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 产品SPU
 * class_name: ProductSPU
 * package: com.store.system.model
 * creat_user: lihao
 * creat_date: 2018/12/6
 * creat_time: 11:38
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure, identityType = IdentityType.origin_indentity)
@Data
public class ProductSPU implements Serializable {

    public static final int status_nomore=0;//正常
    public static final int status_delete=1;//删除

    public static final int type_common = 0; //常规产品
    public static final int type_integral = 1; //积分产品

    @PrimaryKey
    private long id;

    private int type; //SPU类型 0-常规 1-积分

    private long subid; //店铺ID (一级：公司)

    private long pid; //供应商ID

    private long cid; //类目ID

    private long bid; //品牌ID

    private long sid; //系列ID

    private String icon;

    @HyperspaceColumn(isJson = true)
    private List<String> covers;

    private String name; //产品名称

    private String priceRange; //价格区间

    @HyperspaceColumn(isJson = true)
    private Map<Long, Object> properties; //属性json

    private int status;

    @SortKey
    private long sort;

    private String other; //附加属性

    private String ext; //扩展字段

    private long ctime;

    private long utime;

}
