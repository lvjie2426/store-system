package com.store.system.model.attendance;

import com.quakoo.baseFramework.model.pagination.PagerCursor;
import com.quakoo.space.annotation.domain.CombinationKey;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.ShardingKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;


/**
 * 打卡记录
 * 考勤日志
 * 一天两次打卡
 */
@Data
@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure,
        identityType = IdentityType.human)
public class AttendanceLog implements Serializable {

    public static final int leave_no=0; //正常
    public static final int leave_yes=1; //已请假

    public static final int fixType_all=0;
    public static final int fixType_in=1;//修改早上考勤为正常
    public static final int fixType_out=2;//修改晚上考勤为正常

    /**
    * 公司ID
    */
    @ShardingKey
    @CombinationKey
    private long sid;

    /**
     * 门店ID
     */
    @CombinationKey
    private long subId;

    /**
     * 员工ID
     */
    @CombinationKey
    private long uid;

    /**
     * 日期/天
     */
    @CombinationKey
    private long day;

    /**
     * 周
     */
    private long week;
    /**
     * 月
     */
    private long month;

    /**
     * 应该打卡的开始时间
     */
    private int start=-1;
    /**
     * 应该打卡的结束时间
     */
    private int end=-1;

    /**
     * 弹性时间
     */
    private int flexTime;

    /**
     * 打卡时间
     */
    private long startTime;
    /**
     * 打卡图片
     */
    private String startImg;

    /**
     * 打卡时间
     */
    private long endTime;
    /**
     * 打卡图片
     */
    private String endImg;

    /**
     * 是否请假
     */
    private int leave;

    /**
     * 补卡原因
     */
    private String reason;

    /**
     * 原始数据，补卡，系统管理员重置打卡等JSON格式保留信息，以备扩展
     */
    private String orgInfo;

    @SortKey
    @PagerCursor
    private long ctime;

    private long utime;

}
