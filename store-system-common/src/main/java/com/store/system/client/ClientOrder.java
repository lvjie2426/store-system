package com.store.system.client;

import com.store.system.model.OptometryInfo;
import com.store.system.model.Order;
import com.store.system.model.OrderSku;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

/**
 * @program: qianyi
 * @description:
 * @author: zhangmeng
 * @create: 2019-04-03 16:50
 **/
@Data
public class ClientOrder extends Order {

    private String subName; //门店名称
    private String machiningName; //加工师
    private String oiName; //验光师
    private String couponName; //优惠券名称
    private String uName; //顾客
    private String uPhone;
    private String personnelName; //员工姓名
    private double descSubtract;// 折扣（打折）、金额
    private double priceYuan;// 实际支付金额 元
    private double dicountPriceYuan;// 折后金额 元
    private double totalPriceYuan;// 总金额 元
    private int descSubtractType;// 类型(1-金额 2-百分比)
    private int asCount;//售后次数

    private List<OptometryInfo> optometryInfos;//顾客的验光信息记录

    public ClientOrder(){};
    public ClientOrder(Order order){
        try {
            BeanUtils.copyProperties(this, order);
        } catch (Exception e) {
            throw new IllegalStateException("ClientOrder construction error!");
        }

    };


}
