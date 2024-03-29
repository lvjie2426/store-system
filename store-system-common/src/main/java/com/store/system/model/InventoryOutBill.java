package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceColumn;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import com.store.system.bean.InventoryOutBillItem;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 进销存出库单
 * class_name: InventoryOutBill
 * package: com.store.system.model
 * creat_user: lihao
 * creat_date: 2019/1/1
 * creat_time: 16:15
 **/
@HyperspaceDomain(identityType = IdentityType.origin_indentity, domainType = HyperspaceDomainType.mainDataStructure)
@Data
public class InventoryOutBill implements Serializable {

    public static final int status_edit = 0; //编辑状态
    public static final int status_wait_check = 1; //等待审核
    public static final int status_end = 2; //完结状态

    public static final int check_pass = 1; //审核通过
    public static final int check_no_pass = 2; //审核未通过

    @PrimaryKey
    private long id;

    private long subid; //店铺ID

    private long wid; //仓库ID

    private long outUid; //出库人

    private long createUid; //创建人

    private long checkUid; //审核人

    private int type; //出库类型，0 正常 3 退货；2 销毁

    @HyperspaceColumn(isJson = true)
    private List<InventoryOutBillItem> items; //子条目JSON

    private int status;

    private int check; //审核状态

    @SortKey
    private long ctime;

    private long utime;

}
