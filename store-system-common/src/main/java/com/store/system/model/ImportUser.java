package com.store.system.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
@Data
public class ImportUser implements Serializable {
    @Excel(name = "序号" )
    private String number;

    @Excel(name = "顾客姓名")
    private String name;

    @Excel(name = "性别")
    private String sex;

    @Excel(name = "年龄")
    private String age;

    @Excel(name = "出生日期")
    private String birthday;

    @Excel(name = "职业")
    private String job;

    @Excel(name = "手机号")
    private String phone;

    @Excel(name = "用户名")
    private String userName;

    @Excel(name = "微信号")
    private String weChat;

    @Excel(name = "身份证")
    private String idCard;

    @Excel(name = "邮箱")
    private String email;

    @Excel(name = "居住地")
    private String address;

    @Excel(name = "积分")
    private String score;

    @Excel(name = "储蓄金额")
    private String money;

    @Excel(name = "备注")
    private String desc;

    private String reason;
}
