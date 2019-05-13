package com.store.system.client;

import com.store.system.model.InventoryDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientInventoryDetail extends InventoryDetail {

    private String subName; //店铺名称

    private String warehouseName; //仓库名称

    private int p_type;

    private long p_pid;

    private String providerName;

    private String categoryName;

    private long p_bid;

    private String brandName;

    private long p_sid;

    private String seriesName;

    private String p_name;

    private String p_code;

    private Map<Long, Object> p_properties; //SKU 属性

    private int p_retailPrice;

    private int p_costPrice;

    private int p_integralPrice;

    private int num;

    private long ctime;

    public ClientInventoryDetail(InventoryDetail inventoryDetail) {
        try {
            BeanUtils.copyProperties(this, inventoryDetail);
        } catch (Exception e) {
            throw new IllegalStateException("ClientInventoryDetail construction error!");
        }
    }

}
