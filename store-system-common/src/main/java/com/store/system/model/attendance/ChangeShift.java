package com.store.system.model.attendance;

import com.quakoo.baseFramework.model.pagination.PagerCursor;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

/**调班
 * @ClassName ChangeShift
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/10 16:17
 * @Version 1.0
 **/
@Data
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
public class ChangeShift implements Serializable{

    public static final int status_ask=0;//申请中
    public static final int status_success=1;//审核通过
    public static final int status_fail=2;//审核不通过

    public static final int replace_yes=1;//被调班人接受
    public static final int replace_no=2;//被调班人拒绝
    public static final int replace_wait=0;//等待中

    @PrimaryKey
    private long id;
    /***
    * 调班日期
    */
    private long date;
    /***
    * 班次时间
    */
    private int flightStart;

    private int flightEnd;
    /***
    * 调班时间
    */
    private int changeStart;

    private int changeEnd;
    /***
     * 被调班人
     */
    private long replaceUid;
    /**
     * 申请调班人UID
     */
    private long askUid;
    /**
     * 审批人UID
     */
    private long checkUid;
    /**
     *  抄送UID
     */
    private long copyUid;

    /**
     * 请假状态
     */
    private int status;

    /**
     * 被调班人态度
     */
    private int replaceStatus;
    /**
     * 公司ID
     */
    private long sid;
    /**
     * 门店ID
     */
    private long subId;

    @SortKey
    @PagerCursor
    private long ctime;

    private long utime;

}
