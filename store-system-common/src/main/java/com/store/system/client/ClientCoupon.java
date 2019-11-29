package com.store.system.client;

import com.store.system.exception.StoreSystemException;
import com.store.system.model.Coupon;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

/**
 * @ClassName ClientCoupon
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/29 11:12
 * @Version 1.0
 **/
@Data
public class ClientCoupon extends Coupon{

    private List<ClientProductSKU> skuList;

    public  ClientCoupon(Coupon coupon){
        try {
            BeanUtils.copyProperties(this,coupon);
        } catch (Exception e) {
            throw  new StoreSystemException("ClientCoupon copybean error");
        }
    }

}
