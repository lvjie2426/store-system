package com.store.system.client;

import com.store.system.model.OptometryInfo;
import com.store.system.model.OrderSku;
import com.store.system.model.Surcharge;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ClientAfterSaleDetail
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/12 15:55
 * @Version 1.0
 **/
@Data
public class ClientAfterSaleDetail implements Serializable{

    private String userName; //顾客姓名
    private int userAge; //顾客年龄
    private String phone;//联系方式
    private String price; //订单金额
    private double discount; //折扣
    private String oiName; //验光师
    private String machiningName; //加工师
    private List<OptometryInfo> optometryInfos;//顾客的验光信息记录
    private List<Surcharge> surcharges; //附加费用
    private String totalPrice;//总金额
    private List<OrderSku> sku; //订单信息
    private String reason; //售后原因
    private String optName; //操作人姓名
    private String saleTime; //售后时间

}
