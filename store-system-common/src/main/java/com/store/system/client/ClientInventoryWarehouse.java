package com.store.system.client;

import com.store.system.model.InventoryWarehouse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientInventoryWarehouse extends InventoryWarehouse {

    private String adminName;

    public ClientInventoryWarehouse(InventoryWarehouse inventoryWarehouse) {
        try {
            BeanUtils.copyProperties(this, inventoryWarehouse);
        } catch (Exception e) {
            throw new IllegalStateException("ClientInventoryWarehouse construction error!");
        }
    }

}
