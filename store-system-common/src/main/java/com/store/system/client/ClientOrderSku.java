package com.store.system.client;

import com.store.system.model.OrderSku;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

/**
 * @ClassName ClientOrderSku
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/10/30 16:44
 * @Version 1.0
 **/
@Data
public class ClientOrderSku extends OrderSku{

    private Map<Object,Object> p_properties_value;//SPU属性的值

    private Map<Object,Object> k_properties_value;//SKU属性的值

    private String providerName;//供应商

    private String createName;// 生产商

    private String categoryName;

    private String brandName;

    private String seriesName;

    private double rate;

    public ClientOrderSku(){}
    public ClientOrderSku(OrderSku orderSku){
        try {
            BeanUtils.copyProperties(this, orderSku);
        } catch (Exception e) {
            throw new IllegalStateException("ClientOrderSku construction error!");
        }

    }
}
