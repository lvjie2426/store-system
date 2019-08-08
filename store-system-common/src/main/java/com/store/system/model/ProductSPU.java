package com.store.system.model;

import com.google.common.collect.Lists;
import com.quakoo.space.annotation.domain.HyperspaceColumn;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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

    public static final int sale_status_open=0;//开启
    public static final int sale_status_close=1;//关闭

    @PrimaryKey
    private long id;

    private long subid; //店铺ID (一级：公司)

    private long pid; //供应商ID

    private long cid; //类目ID

    private long bid; //品牌ID

    private long sid; //系列ID

    private String icon;

    @HyperspaceColumn(isJson = true)
    private List<String> covers= Lists.newArrayList();

    private String name; //产品名称

    private String priceRange; //价格区间

    @HyperspaceColumn(isJson = true)
    private Map<Long, Object> properties=new HashMap<Long, Object>(); //属性json

    private int status;

    private long sort;

    private String other; //附加属性

    private String ext; //扩展字段

    private int saleStatus;// 销售状态

    private long integralStartTime;// 积分商品兑换开始时间

    private long integralEndTime;//积分商品兑换结束时间

    private int integralNum; // 积分商品兑换数量上限

    @HyperspaceColumn(isJson = true)
    private List<ProductCustomRange> ranges= Lists.newArrayList();//定制范围

    @HyperspaceColumn(isJson = true)
    private List<ProductCustomRange> nowRanges= Lists.newArrayList();//现货范围

    private int nowRemind; //现货库存提醒

    private int totalRemind; //总库存提醒


    @SortKey
    private long ctime;

    private long utime;

}
