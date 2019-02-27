package com.store.system.client;

import com.store.system.model.InventoryInvokeBill;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

public class ClientInventoryInvokeBill extends InventoryInvokeBill {

    private String inSubName;

    private String inWarehouseName;

    private String outSubName;

    private String outWarehouseName;

    private String outUserName;

    private String createUserName;

    private String checkUserName;

    private String inUserName;

    private List<ClientInventoryInvokeBillItem> items;

    public ClientInventoryInvokeBill(InventoryInvokeBill inventoryInvokeBill) {
        try {
            BeanUtils.copyProperties(this, inventoryInvokeBill);
        } catch (Exception e) {
            throw new IllegalStateException("ClientInventoryInvokeBill construction error!");
        }
    }

    public String getInSubName() {
        return inSubName;
    }

    public void setInSubName(String inSubName) {
        this.inSubName = inSubName;
    }

    public String getInWarehouseName() {
        return inWarehouseName;
    }

    public void setInWarehouseName(String inWarehouseName) {
        this.inWarehouseName = inWarehouseName;
    }

    public String getOutSubName() {
        return outSubName;
    }

    public void setOutSubName(String outSubName) {
        this.outSubName = outSubName;
    }

    public String getOutWarehouseName() {
        return outWarehouseName;
    }

    public void setOutWarehouseName(String outWarehouseName) {
        this.outWarehouseName = outWarehouseName;
    }

    public String getOutUserName() {
        return outUserName;
    }

    public void setOutUserName(String outUserName) {
        this.outUserName = outUserName;
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

    public String getInUserName() {
        return inUserName;
    }

    public void setInUserName(String inUserName) {
        this.inUserName = inUserName;
    }

    public List<ClientInventoryInvokeBillItem> getItems() {
        return items;
    }

    public void setItems(List<ClientInventoryInvokeBillItem> items) {
        this.items = items;
    }
}
