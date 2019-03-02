package com.store.system.client;

import com.store.system.model.ProductSKU;
import org.apache.commons.beanutils.BeanUtils;

public class ClientProductSKU extends ProductSKU {

    private int num; //可用库存

    public ClientProductSKU(ProductSKU productSKU) {
        try {
            BeanUtils.copyProperties(this, productSKU);
        } catch (Exception e) {
            throw new IllegalStateException("ClientProductSKU construction error!");
        }
    }

    @Override
    public int getNum() {
        return num;
    }

    @Override
    public void setNum(int num) {
        this.num = num;
    }
}
