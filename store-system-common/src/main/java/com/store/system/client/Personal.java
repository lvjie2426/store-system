package com.store.system.client;

import lombok.Data;

import java.util.List;

/**
 * @program: store-system
 * @description: 用于返回数据
 * @author: zhangmeng
 * @create: 2019-10-22 11:11
 **/
@Data
public class Personal {

    private long sid;

    private String uName;//员工name

    private String sName; //企业name

    private int number;//实际完成个数

    private int price;//实际完成金额

}
