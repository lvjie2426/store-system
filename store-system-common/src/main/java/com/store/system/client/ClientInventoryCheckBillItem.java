package com.store.system.client;

import com.store.system.bean.InventoryCheckBillItem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientInventoryCheckBillItem extends InventoryCheckBillItem {

    private long spuid; //产品SPU的id

    private String spuName;

    private String spuIcon;

    private List<String> spuCovers;

    private long cid;

    private String categoryName;

    private long bid; //品牌ID

    private String brandName;

    private long sid; //系列ID

    private String seriesName;

    private String code; //产品编码

    private int eyeType;

    private Map<Long, Object> properties; //sku属性json

    private Map<Object, Object> k_properties_value; //SKU 属性

    public ClientInventoryCheckBillItem(InventoryCheckBillItem inventoryCheckBillItem) {
        try {
            BeanUtils.copyProperties(this, inventoryCheckBillItem);
        } catch (Exception e) {
            throw new IllegalStateException("ClientInventoryCheckBillItem construction error!");
        }
    }

}
