package com.store.system.model;

import com.quakoo.space.annotation.domain.*;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**售后记录
 * @ClassName AfterSaleLog
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/10 15:24
 * @Version 1.0
 **/
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,
        identityType = IdentityType.origin_indentity)
@Data
public class AfterSaleLog implements Serializable {

    @PrimaryKey
    private long id;

    private long subId;//门店ID

    private long oid;//订单ID

    private String orderNo;//订单编号

    private long uid;//顾客ID

    private long salesUid;//销售员

    @SortKey
    private long ctime;

    private long utime;

}
