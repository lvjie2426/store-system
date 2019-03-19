package com.store.system.client;

import com.store.system.model.ProductCustom;
import org.apache.commons.beanutils.BeanUtils;

public class ClientProductCustom extends ProductCustom {

    private String pSubName; //公司名称

    private String subName; //分店名称

    private String providerName;

    private String categoryName;

    private String brandName;

    private String seriesName;

    private String name;

    private String code;

    private String propertyJson; //SKU 属性

    public ClientProductCustom(ProductCustom productCustom) {
        try {
            BeanUtils.copyProperties(this, productCustom);
        } catch (Exception e) {
            throw new IllegalStateException("ClientProductCustom construction error!");
        }
    }

    public String getpSubName() {
        return pSubName;
    }

    public void setpSubName(String pSubName) {
        this.pSubName = pSubName;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPropertyJson() {
        return propertyJson;
    }

    public void setPropertyJson(String propertyJson) {
        this.propertyJson = propertyJson;
    }
}
