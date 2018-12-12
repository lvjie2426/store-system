package com.store.system.client;

import java.io.Serializable;
import java.util.List;

public class ClientInventoryInBillSelect implements Serializable {

    private ClientProductSPU productSPU;

    private List<ClientProductProperty> skuProperties;

    public ClientProductSPU getProductSPU() {
        return productSPU;
    }

    public void setProductSPU(ClientProductSPU productSPU) {
        this.productSPU = productSPU;
    }

    public List<ClientProductProperty> getSkuProperties() {
        return skuProperties;
    }

    public void setSkuProperties(List<ClientProductProperty> skuProperties) {
        this.skuProperties = skuProperties;
    }

}
