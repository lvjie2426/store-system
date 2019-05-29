package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;

/**
 * @ProjectName: store-system
 * @Package: com.store.system.model
 * @ClassName: Salary
 * @Author: LiHaoJie
 * @Description: 工资表 员工的工资明细表
 * @Date: 2019/5/27 14:47
 * @Version: 1.0
 */
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,identityType = IdentityType.origin_indentity)
@Data
public class Salary implements Serializable{

    public static final int status_nomore=0;//正常
    public static final int status_delete=1;//删除

    @PrimaryKey
    private long id;

    private long uid;//用户ID

    private String uname;//员工姓名

    private long sid;//店铺ID

    private long psid;//公司ID

    private int basePay;//基本工资(分)

    private int royalty;//销售提成(分)

    private int bonus;//奖金(分)

    private int fine;//罚款(分)

    private int finalPay;//实发工资

    private long oid;//操作人

    private int year;//年份

    private int month;//月份

    private String desc;

    private int status;//0正常 1删除

    @SortKey
    private long ctime;

    private long utime;


}
