package com.store.system.client;

import com.store.system.model.InventoryInBill;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

public class ClientInventoryInBill extends InventoryInBill {

    private String warehouseName;

    private String inUserName;

    private String createUserName;

    private String checkUserName;

    private List<ClientInventoryInBillItem> items;

    public ClientInventoryInBill(InventoryInBill inventoryInBill) {
        try {
            BeanUtils.copyProperties(this, inventoryInBill);
        } catch (Exception e) {
            throw new IllegalStateException("ClientInventoryInBill construction error!");
        }
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getInUserName() {
        return inUserName;
    }

    public void setInUserName(String inUserName) {
        this.inUserName = inUserName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCheckUserName() {
        return checkUserName;
    }

    public void setCheckUserName(String checkUserName) {
        this.checkUserName = checkUserName;
    }

    public List<ClientInventoryInBillItem> getItems() {
        return items;
    }

    public void setItems(List<ClientInventoryInBillItem> items) {
        this.items = items;
    }
}
