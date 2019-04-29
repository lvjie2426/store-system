package com.store.system.client;

import com.store.system.model.InventoryOutBill;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientInventoryOutBill extends InventoryOutBill {

    private String subName;

    private String warehouseName;

    private String outUserName;

    private String createUserName;

    private String checkUserName;

    private List<ClientInventoryOutBillItem> clientItems;

    public ClientInventoryOutBill(InventoryOutBill inventoryOutBill) {
        try {
            BeanUtils.copyProperties(this, inventoryOutBill);
        } catch (Exception e) {
            throw new IllegalStateException("ClientInventoryOutBill construction error!");
        }
    }

}

