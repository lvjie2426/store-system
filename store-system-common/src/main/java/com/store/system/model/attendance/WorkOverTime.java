package com.store.system.model.attendance;

import com.quakoo.baseFramework.model.pagination.PagerCursor;
import com.quakoo.space.annotation.domain.*;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 加班
 */
@Data
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
public class WorkOverTime implements Serializable {

    public static final int status_ask=0;//申请中
    public static final int status_success=1;//审核通过
    public static final int status_fail=2;//审核不通过

    @PrimaryKey
    private long id;
    /**
     * 加班开始
     */
    private long startTime;
    /**
     * 加班结束
     */
    private long endTime;
    /**
     * 申请时间
     */
    private long applyTime;
    /**
     * 加班时长(小时)
     */
    private double workTime;
    /**
     * 请假状态
     */
    private int status;
    /**
     * 公司ID
     */
    private long sid;
    /**
     * 门店ID
     */
    private long subId;
    /**
     * 申请人UID
     */
    private long askUid;
    /**
     * 日期/天
     */
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
     * 年
     */
    private long year;
    /**
     * 审批人UID
     */
    private long checkUid;
    /**
    *  抄送UID
    */
    private long copyUid;
    /**
     * 附带图片
     */
    @HyperspaceColumn(isJson = true)
    private List<String> imgs;
    /**
     * 加班原因内容
     */
    private String content;
    /**
     * 审核理由
     */
    private String reason;

    @SortKey
    @PagerCursor
    private long ctime;

    private long utime;


}
