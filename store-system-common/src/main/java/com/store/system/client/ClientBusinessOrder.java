package com.store.system.client;

import com.store.system.model.*;
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
    private int uAge;
    private int score;//顾客可用积分
    private int money;//顾客可用储值
    private ClientUser userInfo;//顾客信息
    private String staffName; //员工姓名

    private MarketingCoupon coupon;//优惠券
    private int asCount;//售后次数
    private List<OptometryInfo> optometryInfos;//顾客的验光信息历史记录
    private List<PayInfo> payInfos;  //支付记录



    public ClientBusinessOrder(){}
    public ClientBusinessOrder(BusinessOrder businessOrder){
        try {
            BeanUtils.copyProperties(this, businessOrder);
        } catch (Exception e) {
            throw new IllegalStateException("ClientBusinessOrder construction error!");
        }

    }
}
