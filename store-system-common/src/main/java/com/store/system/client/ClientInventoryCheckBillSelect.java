package com.store.system.client;

import com.store.system.model.InventoryDetail;

import java.io.Serializable;
import java.util.List;

public class ClientInventoryCheckBillSelect implements Serializable {

    private ClientProductSPU productSPU;

    private int currentNum; //当前数量

    private List<InventoryDetail> details;

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }

    public List<InventoryDetail> getDetails() {
        return details;
    }

    public void setDetails(List<InventoryDetail> details) {
        this.details = details;
    }

    public ClientProductSPU getProductSPU() {
        return productSPU;
    }

    public void setProductSPU(ClientProductSPU productSPU) {
        this.productSPU = productSPU;
    }
}
