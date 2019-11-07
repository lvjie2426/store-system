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
 * 请假
 *
 *
 */
@Data
@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure,
        identityType = IdentityType.human)
public class UserLeavePool implements Serializable {
    /**
     */
    @ShardingKey
    @CombinationKey
    private long uid;

    /**
     * 请假ID
     */
    @CombinationKey
    private long lid;

    private int status;


    @SortKey
    @PagerCursor
    private long ctime;

    private long utime;

    public UserLeavePool(){}

    public UserLeavePool(Leave leave){
        this.lid=leave.getId();
        this.uid=leave.getAskUid();

    }

}
