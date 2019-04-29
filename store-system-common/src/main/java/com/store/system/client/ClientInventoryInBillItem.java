package com.store.system.client;

import com.store.system.bean.InventoryInBillItem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientInventoryInBillItem extends InventoryInBillItem {

    private String spuName;

    private String spuIcon;

    private List<String> spuCovers;

    private long cid;

    private String categoryName;

    private long bid; //品牌ID

    private String brandName;

    private long sid; //系列ID

    private String seriesName;

    public ClientInventoryInBillItem(InventoryInBillItem inventoryInBillItem) {
        try {
            BeanUtils.copyProperties(this, inventoryInBillItem);
        } catch (Exception e) {
            throw new IllegalStateException("ClientInventoryInBillItem construction error!");
        }
    }

}
