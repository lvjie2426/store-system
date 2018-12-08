package com.store.system.client;

import com.store.system.model.ProductSKU;
import org.apache.commons.beanutils.BeanUtils;

public class ClientProductSKU extends ProductSKU {

    public ClientProductSKU(ProductSKU productSKU) {
        try {
            BeanUtils.copyProperties(this, productSKU);
        } catch (Exception e) {
            throw new IllegalStateException("ClientProductSKU construction error!");
        }
    }

}
