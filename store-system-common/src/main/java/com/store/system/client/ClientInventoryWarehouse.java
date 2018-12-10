package com.store.system.client;

import com.store.system.model.InventoryWarehouse;
import org.apache.commons.beanutils.BeanUtils;

public class ClientInventoryWarehouse extends InventoryWarehouse {

    private String adminName;

    public ClientInventoryWarehouse(InventoryWarehouse inventoryWarehouse) {
        try {
            BeanUtils.copyProperties(this, inventoryWarehouse);
        } catch (Exception e) {
            throw new IllegalStateException("ClientInventoryWarehouse construction error!");
        }
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

}
