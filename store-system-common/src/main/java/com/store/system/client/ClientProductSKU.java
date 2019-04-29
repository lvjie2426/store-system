package com.store.system.client;

import com.store.system.model.ProductSKU;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientProductSKU extends ProductSKU {

    private int canUseNum; //可用库存

    private List<ClientInventoryDetail> details; //库存详情

    private List<ClientInventoryDetail> otherDetails; //其他分店库存详情

    public ClientProductSKU(ProductSKU productSKU) {
        try {
            BeanUtils.copyProperties(this, productSKU);
        } catch (Exception e) {
            throw new IllegalStateException("ClientProductSKU construction error!");
        }
    }

}
