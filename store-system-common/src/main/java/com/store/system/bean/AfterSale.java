package com.store.system.bean;

import com.store.system.model.OrderSku;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AfterSale
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/10 19:15
 * @Version 1.0
 **/
@Data
public class AfterSale implements Serializable{

    private long subId;//门店ID

    private long oid;//订单ID

    private String reason;

    private List<OrderSku> sku=new ArrayList<>();

    private long optId;//操作人ID

    private int refund;//退款金额(分)

    private int extra;//额外支付(分)

    public AfterSale(){}
}
