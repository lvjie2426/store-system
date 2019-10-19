package com.store.system.model.attendance;

import com.quakoo.space.annotation.domain.CombinationKey;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.ShardingKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

/**个人请假天数
 * @ClassName UserLeaveDays
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/10 14:50
 * @Version 1.0
 **/
@Data
@HyperspaceDomain(domainType = HyperspaceDomainType.listDataStructure,
        identityType = IdentityType.human)
public class UserLeaveDays implements Serializable{

    @ShardingKey
    @CombinationKey
    private long uid;

    @CombinationKey
    private int leaveType;

    /***
    * 剩余可请假时长(小时)
    */
    private double time;

    @SortKey
    private long ctime;

    private long utime;


}
