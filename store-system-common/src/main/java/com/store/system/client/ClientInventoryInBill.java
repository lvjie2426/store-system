package com.store.system.client;

import com.store.system.model.Company;
import com.store.system.model.InventoryInBill;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientInventoryInBill extends InventoryInBill {

    private String subName;

    private String warehouseName;

    private String inUserName;

    private String createUserName;

    private String checkUserName;

    private List<ClientInventoryInBillItem> clientItems;

    public ClientInventoryInBill(InventoryInBill inventoryInBill) {
        try {
            BeanUtils.copyProperties(this, inventoryInBill);
        } catch (Exception e) {
            throw new IllegalStateException("ClientInventoryInBill construction error!");
        }
    }

}
