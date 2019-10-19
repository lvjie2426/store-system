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
 * 考勤统计-下属单位的考勤统计
 */
@Data
@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure,
        identityType = IdentityType.human)
public class SubordinateAttendanceStatistics implements Serializable {

    /**
     * 单位ID
     */
    @CombinationKey
    @ShardingKey
    private long sid;

    /**
     * 天
     */
    @CombinationKey
    private long day;

    /**
     * 周
     */
    @CombinationKey
    private long week;
    /**
     * 月
     */
    @CombinationKey
    private long month;

    /**
     * 需要考勤人/次数
     */
    private int totalNum;
    /**
     * 迟到人/次数
     */
    private int lateNum;

    /**
     * 早退人/次数
     */
    private int leaveEarlyNum;

    /**
     * 出勤人/次数
     */
    private int attendanceNum;

    /**
     * 缺卡人/次数
     */
    private int noCardNum;

    /**
     * 旷工人/次数
     */
    private int absentNum;

    /**
     * 未排班人/次数
     */
    private int weiPaibanNum;

    /**
     * 请假次数
     */
    private int leaveNum;

    /**
     * 未设置人/次数
     */
    private int unsetNum;

    /**
     * 出勤率
     */
    private double attendanceRate;


    @SortKey
    @PagerCursor
    private long ctime;

    private long utime;


}
