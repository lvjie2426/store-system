package com.store.system.client;

import com.store.system.model.InventoryInvokeBill;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientInventoryInvokeBill extends InventoryInvokeBill {

    private String inSubName;

    private String inWarehouseName;

    private String outSubName;

    private String outWarehouseName;

    private String outUserName;

    private String createUserName;

    private String checkUserName;

    private String inUserName;

    private List<ClientInventoryInvokeBillItem> clientItems;

    public ClientInventoryInvokeBill(InventoryInvokeBill inventoryInvokeBill) {
        try {
            BeanUtils.copyProperties(this, inventoryInvokeBill);
        } catch (Exception e) {
            throw new IllegalStateException("ClientInventoryInvokeBill construction error!");
        }
    }

}
