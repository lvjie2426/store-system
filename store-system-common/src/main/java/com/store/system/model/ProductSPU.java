package com.store.system.model;

import com.google.common.collect.Lists;
import com.quakoo.baseFramework.model.pagination.PagerCursor;
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

    public static final int type_common = 0; //常规产品
    public static final int type_devices = 1; //医疗器械

    public static final int checkStatus_no = 0;//未审核/未验收
    public static final int checkStatus_yes = 1;//审核通过/已验收

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

    private String other; //附加属性

    private String ext; //扩展字段/存放质检人

    private int saleStatus;// 销售状态

    private long integralStartTime;// 积分商品兑换开始时间

    private long integralEndTime;//积分商品兑换结束时间

    private int integralNum; // 积分商品兑换数量上限

    @HyperspaceColumn(isJson = true)
    private List<ProductCustomRange> ranges= Lists.newArrayList();//定制范围

    @HyperspaceColumn(isJson = true)
    private List<ProductCustomRange> nowRanges= Lists.newArrayList();//现货范围

    private int nowRemind; //现货库存为0的产品达到的数量

    private double totalRemind; //总库存少于备货量百分之n，进行提醒

    private int underRemind;  //当库存低于n数量，进行提醒

    //医疗器械
    private int type;//类型 非医疗器械0 医疗器械1

    private int checkStatus;//审核状态  0 未审核 1 审核通过

    private long checkStatusDate;//审核时间

    private String nirNum;//国械注准号

    private long nirNumDate;//国械注准号注册时间 

    @HyperspaceColumn(isJson = true)
    private List<String> nirImg= Lists.newArrayList();//注册图片


    @SortKey
    @PagerCursor
    private long ctime;

    private long utime;

}
