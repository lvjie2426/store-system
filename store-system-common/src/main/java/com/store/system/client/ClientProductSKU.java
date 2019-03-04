package com.store.system.client;

import com.store.system.model.ProductSKU;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

public class ClientProductSKU extends ProductSKU {

    private int num; //可用库存

    private List<ClientInventoryDetail> details; //库存详情

    private List<ClientInventoryDetail> otherDetails; //其他分店库存详情

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

    public List<ClientInventoryDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ClientInventoryDetail> details) {
        this.details = details;
    }

    public List<ClientInventoryDetail> getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(List<ClientInventoryDetail> otherDetails) {
        this.otherDetails = otherDetails;
    }
}
