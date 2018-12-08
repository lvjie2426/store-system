package com.store.system.client;

import com.store.system.model.ProductSPU;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

public class ClientProductSPU extends ProductSPU {

    private List<ClientProductSKU> skuList;

    public ClientProductSPU(ProductSPU productSPU) {
        try {
            BeanUtils.copyProperties(this, productSPU);
        } catch (Exception e) {
            throw new IllegalStateException("ClientProductSPU construction error!");
        }
    }

    public List<ClientProductSKU> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<ClientProductSKU> skuList) {
        this.skuList = skuList;
    }

}
