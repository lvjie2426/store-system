package com.store.system.client;

import com.store.system.model.InventoryCheckBill;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

public class ClientInventoryCheckBill extends InventoryCheckBill {

    private String warehouseName;

    private String initUserName;

    private String createUserName;

    private String checkUserName;

    private List<ClientInventoryCheckBillItem> items;

    public ClientInventoryCheckBill(InventoryCheckBill inventoryCheckBill) {
        try {
            BeanUtils.copyProperties(this, inventoryCheckBill);
        } catch (Exception e) {
            throw new IllegalStateException("ClientInventoryCheckBill construction error!");
        }
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getInitUserName() {
        return initUserName;
    }

    public void setInitUserName(String initUserName) {
        this.initUserName = initUserName;
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

    public List<ClientInventoryCheckBillItem> getItems() {
        return items;
    }

    public void setItems(List<ClientInventoryCheckBillItem> items) {
        this.items = items;
    }
}
