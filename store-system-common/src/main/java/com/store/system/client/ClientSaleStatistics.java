package com.store.system.client;

import lombok.Data;

/**
 * @ClassName ClientSaleStatistics
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/19 16:04
 * @Version 1.0
 **/
@Data
public class ClientSaleStatistics {

    public static final int saleStatus_increase = 1; //增长
    public static final int saleStatus_decrement = 2; //减少

    public static final int numStatus_increase = 1; //增长
    public static final int numStatus_decrement = 2; //减少


    public static final int type_today = 1; //今日
    public static final int type_yesterday = 2; //昨日
    public static final int type_week = 3; //本周
    public static final int type_month = 4; //本月
    public static final int type_halfYear = 5; //近半年

    private String subName;//门店名称

    private double sale; //销售额(元)

    private int num; //销售单数

    private double perPrice; //平均客单价(元)

    private double profits;//毛利润(元)

    private int saleStatus;  //销售额状态

    private double saleRate; //销售额增长/减少率

    private int numStatus; //销售单数状态

    private double numRate; //销售单数增长/减少率

    //去年同期
    private double saleOld; //销售额(元)

    private int numOld; //销售单数

    private double perPriceOld; //平均客单价(元)

    private double profitsOld;//毛利润(元)

}
