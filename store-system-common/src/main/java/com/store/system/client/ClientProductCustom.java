package com.store.system.client;

import com.store.system.model.ProductCustom;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientProductCustom extends ProductCustom {

    private String pSubName; //公司名称

    private String subName; //分店名称

    private String providerName;

    private String categoryName;

    private String brandName;

    private String seriesName;

    private String name;

    private String code;

    private Map<Long, Object> properties; //SKU 属性

    public ClientProductCustom(ProductCustom productCustom) {
        try {
            BeanUtils.copyProperties(this, productCustom);
        } catch (Exception e) {
            throw new IllegalStateException("ClientProductCustom construction error!");
        }
    }

}
