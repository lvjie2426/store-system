package com.store.system.model;

import com.quakoo.space.annotation.domain.HyperspaceColumn;
import com.quakoo.space.annotation.domain.HyperspaceDomain;
import com.quakoo.space.annotation.domain.PrimaryKey;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceDomainType;
import com.quakoo.space.enums.IdentityType;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: store-system
 * @Package: com.store.system.model
 * @ClassName: SalaryRecord
 * @Author: LiHaoJie
 * @Description: 工资单导入记录表 记录每一次工资单导入记录
 * @Date: 2019/5/27 15:13
 * @Version: 1.0
 */
@HyperspaceDomain(domainType = HyperspaceDomainType.mainDataStructure,identityType = IdentityType.origin_indentity)
@Data
public class SalaryRecord implements Serializable {

    public static final int status_nomore=0;//正常
    public static final int status_delete=1;//删除

    public static final int type_success=0;//成功
    public static final int type_fail=1;//失败

    @PrimaryKey
    private long id;

    private long sid;//门店ID

    private long psid;//公司ID

    private int allMoney;//总金额

    private int allNumber;//总人数

    private long oid;//操作人员

    @HyperspaceColumn(isJson = true)
    private List<Long> sids = new ArrayList<>();//工资单ID

    private int year;//年份

    private int month;//月份

    private int status;

    private int type;

    private String errLogs;//错误日志

    @SortKey
    private long ctime;

    private long utime;


}
