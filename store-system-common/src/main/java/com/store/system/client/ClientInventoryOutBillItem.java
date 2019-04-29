package com.store.system.client;

import com.store.system.bean.InventoryOutBillItem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientInventoryOutBillItem extends InventoryOutBillItem {

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

    public ClientInventoryOutBillItem(InventoryOutBillItem inventoryOutBillItem) {
        try {
            BeanUtils.copyProperties(this, inventoryOutBillItem);
        } catch (Exception e) {
            throw new IllegalStateException("ClientInventoryOutBillItem construction error!");
        }
    }

}
