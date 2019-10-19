package com.store.system.model.attendance;

import com.quakoo.baseFramework.model.pagination.PagerCursor;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

/**审批记录
 * @ClassName ApprovalLog
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/10 15:04
 * @Version 1.0
 **/
@Data
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
public class ApprovalLog implements Serializable{

    public static final int type_leave=1; //请假审批
    public static final int type_card=2; //补卡审批
    public static final int type_change=3;//调班审批
    public static final int type_work=4;//加班审批

    @PrimaryKey
    private long id;

    private long sid;

    private long subId;

    private long typeId;

    private int type;

    private long checkUid;

    @SortKey
    @PagerCursor
    private long ctime;

    private long utime;

}
