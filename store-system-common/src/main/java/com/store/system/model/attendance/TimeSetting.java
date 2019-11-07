package com.store.system.model.attendance;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TimeSetting
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/5 17:33
 * @Version 1.0
 **/
@Data
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
public class TimeSetting implements Serializable {

    @PrimaryKey
    private long id;

    private long sid;

    private String timeStr;

    private int start;

    private int end;

    /***
    * 时间区间
    */
    private String interval;


    @SortKey
    private long ctime;

    private long utime;

}
