package com.store.system.client;

import com.store.system.model.ProductSKU;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientProductSKU extends ProductSKU {

    private int canUseNum; //可用库存

    private List<ClientInventoryDetail> details; //库存详情

    private List<ClientInventoryDetail> otherDetails; //其他分店库存详情

    private double discount; //商品的会员折扣

    private Map<Object,Object> p_properties_value;//SKU属性的值

    public ClientProductSKU(ProductSKU productSKU) {
        try {
            BeanUtils.copyProperties(this, productSKU);
        } catch (Exception e) {
            throw new IllegalStateException("ClientProductSKU construction error!");
        }
    }

}
