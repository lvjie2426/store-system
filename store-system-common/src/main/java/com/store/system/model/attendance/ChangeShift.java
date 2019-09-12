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
     * 调班时间
     */
    private long replaceUid;
    /**
     * 请假人UID
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

    @SortKey
    private long ctime;

    private long utime;

}
