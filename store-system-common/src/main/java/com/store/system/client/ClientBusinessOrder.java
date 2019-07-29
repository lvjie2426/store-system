package com.store.system.client;

import com.store.system.model.BusinessOrder;
import com.store.system.model.OptometryInfo;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

/**
 * @ClassName ClientBusinessOrder
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/7/24 11:40
 * @Version 1.0
 **/
@Data
public class ClientBusinessOrder extends BusinessOrder{

    private String subName; //门店名称
    private String threePolicy;//三包
    private String address;//门店地址
    private String phone;//门店联系电话
    private String machiningName; //加工师
    private String oiName; //验光师
    private OptometryInfo info;//该订单的验光信息
    private String couponName; //优惠券名称
    private String uName; //顾客
    private String uPhone;
    private String personnelName; //员工姓名

    private int asCount;//售后次数
    private List<OptometryInfo> optometryInfos;//顾客的验光信息历史记录













    public ClientBusinessOrder(){}
    public ClientBusinessOrder(BusinessOrder businessOrder){
        try {
            BeanUtils.copyProperties(this, businessOrder);
        } catch (Exception e) {
            throw new IllegalStateException("ClientBusinessOrder construction error!");
        }

    }
}
