package com.store.system.client;

import com.store.system.bean.InventoryInBillItem;
import com.store.system.model.Company;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;
import java.util.Map;

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

    private Map<Object,Object> p_properties_value;//SKU属性的值

    private Company company;

    //国械著准号
    private String nirNum;

    public ClientInventoryInBillItem(InventoryInBillItem inventoryInBillItem) {
        try {
            BeanUtils.copyProperties(this, inventoryInBillItem);
        } catch (Exception e) {
            throw new IllegalStateException("ClientInventoryInBillItem construction error!");
        }
    }

}
