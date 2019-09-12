package com.store.system.model.attendance;


import com.quakoo.baseFramework.model.pagination.PagerCursor;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;
@Data
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
public class PunchCardLog implements Serializable{

    public static final int punchCardType_device=1;//闸机打卡
    public static final int punchCardType_gps=2;//定位打卡
    public static final int punchCardType_wifi=3;//无线网络打卡
    public static final int punchCardType_callName=4;//点名打卡
    public static final int punchCardType_face=5;//人脸识别打卡

    public static final int status_in=1;//入园打卡
    public static final int status_out=2;//离园打卡

    @PrimaryKey
    private long id;

    private long uid;
    /**
     * 打卡时间
     */
    private long punchCardTime;

    private long day;
    /**
     * 打卡卡号
     */
    private long card;
    /**
     * 打卡类型
     */
    private int punchCardType;
    /**
     * 打卡图片
     */
    private String punchCardImg;
    /**
     * 打卡状态
     */
    private int status;
    /**
     * 闸机编号
     */
    private String snCode;
    /**
     * 闸机读头
     */
    private int no;
    /**
     * 无线网络编号
     */
    private String wifeNumber;
    /**
     * 无线网络名称
     */
    private String wifeName;
    /**
     * 定位打卡地点
     */
    private String punchCardPlace;
    /**
     * 经纬度数据
     */
    private String mapData;
    /**
     * 现场点名备注
     */
    private String callName;

    @SortKey
    @PagerCursor
    private long ctime;

    private long utime;

}
