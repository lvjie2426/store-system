package com.store.system.client;

import com.store.system.bean.InventoryInvokeBillItem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientInventoryInvokeBillItem extends InventoryInvokeBillItem {

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

    private Map<Long, Object> properties; //sku属性json

    public ClientInventoryInvokeBillItem(InventoryInvokeBillItem inventoryInvokeBillItem) {
        try {
            BeanUtils.copyProperties(this, inventoryInvokeBillItem);
        } catch (Exception e) {
            throw new IllegalStateException("ClientInventoryInvokeBillItem construction error!");
        }
    }

}
