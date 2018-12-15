package com.store.system.client;

import com.store.system.model.InventoryOutBill;
import org.apache.commons.beanutils.BeanUtils;

public class ClientInventoryOutBill extends InventoryOutBill {

    private String warehouseName;

    private String outUserName;

    private String createUserName;

    private String checkUserName;

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
}
