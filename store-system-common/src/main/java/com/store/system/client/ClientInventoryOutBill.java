package com.store.system.client;

import com.store.system.model.InventoryOutBill;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

public class ClientInventoryOutBill extends InventoryOutBill {

    private String subName;

    private String warehouseName;

    private String outUserName;

    private String createUserName;

    private String checkUserName;

    private List<ClientInventoryOutBillItem> items;

    public ClientInventoryOutBill(InventoryOutBill inventoryOutBill) {
        try {
            BeanUtils.copyProperties(this, inventoryOutBill);
        } catch (Exception e) {
            throw new IllegalStateException("ClientInventoryOutBill construction error!");
        }
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
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

    public List<ClientInventoryOutBillItem> getItems() {
        return items;
    }

    public void setItems(List<ClientInventoryOutBillItem> items) {
        this.items = items;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }
}

