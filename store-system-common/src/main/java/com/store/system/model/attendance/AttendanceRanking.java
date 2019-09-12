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

/** 排行榜
 * @ClassName AttendanceRanking
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/10 14:14
 * @Version 1.0
 **/
@Data
@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure,
        identityType = IdentityType.human)
public class AttendanceRanking implements Serializable{

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
     * 应该上班打卡的开始时间
     */
    private int start=-1;

    /**
     * 打卡时间
     */
    private long startTime;

    /**
     * 提前时间
     */
    private long leadTime;

    /**
     * 迟到时间
     */
    private long lateTime;

    @SortKey
    @PagerCursor
    private long ctime;

    private long utime;
}
