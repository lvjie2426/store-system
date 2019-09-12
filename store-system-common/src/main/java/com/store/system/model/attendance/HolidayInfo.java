package com.store.system.model.attendance;


import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;

import java.io.Serializable;


/**
 * 节假日
 * 里面存了 所有的节假日，靠手动拉取数据
 *
 * https://www.jisuapi.com
 * 节假日在这个网站的接口进行查询
 */
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.human)
public class HolidayInfo implements Serializable {


    @PrimaryKey
    @SortKey
    private long day;



    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

}
