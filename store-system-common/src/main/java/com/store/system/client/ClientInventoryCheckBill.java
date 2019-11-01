package com.store.system.client;

import com.store.system.model.InventoryCheckBill;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientInventoryCheckBill extends InventoryCheckBill {

    private String subName;

    private String warehouseName;

    private String initUserName;  //发起人

    private String createUserName; //创建人

    private String checkUserName; //盘点人

    private List<ClientInventoryCheckBillItem> clientItems;

    public ClientInventoryCheckBill(InventoryCheckBill inventoryCheckBill) {
        try {
            BeanUtils.copyProperties(this, inventoryCheckBill);
        } catch (Exception e) {
            throw new IllegalStateException("ClientInventoryCheckBill construction error!");
        }
    }

}
