package com.store.system.model.attendance;

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

    @PrimaryKey
    private long id;
    /***
    * 调班日期
    */
    private long date;
    /***
    * 班次时间
    */
    private long flightTime;
    /***
    * 调班时间
    */
    private long changeTime;
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
     * 公司ID
     */
    private long sid;
    /**
     * 门店ID
     */
    private long subId;

    @SortKey
    private long ctime;

    private long utime;

}
