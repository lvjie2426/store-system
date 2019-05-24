package com.store.system.client;

import com.store.system.model.ProductSPU;
import com.store.system.model.UserGradeCategoryDiscount;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientProductSPU extends ProductSPU {

    private String providerName;

    private String categoryName;

    private String brandName;

    private String seriesName;

    private List<ClientProductSKU> skuList;
    private List<ClientUserGradeCategoryDiscount> userGradeCategoryDiscountList;

    private int canUseNum; //可用库存

    public ClientProductSPU(ProductSPU productSPU) {
        try {
            BeanUtils.copyProperties(this, productSPU);
        } catch (Exception e) {
            throw new IllegalStateException("ClientProductSPU construction error!");
        }
    }

}
