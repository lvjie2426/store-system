package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceColumn;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import com.store.system.bean.OptometryInfoRes;
import lombok.Data;

import java.io.Serializable;

/**
 * 验光信息
 * class_name: OptometryInfo
 * package: com.store.system.model
 * creat_user: lihao
 * creat_date: 2019/3/4
 * creat_time: 17:23
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure, identityType = IdentityType.origin_indentity)
@Data
public class OptometryInfo implements Serializable {

    public static final int wear_prop_jin_shi = 1; //近视眼镜
    public static final int wear_prop_yuan_shi = 2; //远视眼镜
    public static final int wear_prop_yin_xing = 3; //隐形眼镜

    @PrimaryKey
    private long id;

    private long pSubid; //公司ID

    private long subid; //分店ID

    private long uid; //顾客ID

    private long optUid; //验光师

    @SortKey
    private long optTime; //验光时间

    private String desc; //验光备注

    @HyperspaceColumn(isJson = true)
    private OptometryInfoRes yuanYongRes; //远用

    @HyperspaceColumn(isJson = true)
    private OptometryInfoRes yinXingRes; //隐形

    @HyperspaceColumn(isJson = true)
    private OptometryInfoRes jinYongRes; //近用

    @HyperspaceColumn(isJson = true)
    private OptometryInfoRes jianJinDuoJiaoDianRes; //渐进多焦点

    private int farPd; //远用瞳距

    private int wearProp; //佩戴属性

    private String res; //结果

    private String support; //建议

    private long ctime;

    private long utime;


}
