package com.store.system.client;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ClientAfterSaleLog
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/10 18:53
 * @Version 1.0
 **/
@Data
public class ClientAfterSaleLog implements Serializable{

    private long id;

    private String orderNo;//订单编号

    private String lastTime;//最后一次售后时间

    private String userName;//顾客姓名

    private String userPhone;//联系方式

    private String resaon;//售后原因

    private String salesName;//销售员姓名

    private int status;//订单状态

    private int times;//售后次数

}
