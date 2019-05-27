package com.store.system.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

//工资单
@Data
public class ImportSalary implements Serializable {
    @Excel(name = "序号" )
    private String number;

    @Excel(name = "员工编号")
    private String id;

    @Excel(name = "基本工资")
    private String basePay;

    @Excel(name = "销售提成")
    private String royalty;

    @Excel(name = "奖金")
    private String bonus;

    @Excel(name = "罚款")
    private String fine;

    @Excel(name = "实发工资")
    private String finalPay;

    @Excel(name = "备注")
    private String desc;

}
