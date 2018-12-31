package com.store.system.client;

import com.store.system.model.ProductSPU;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

public class ClientProductSPU extends ProductSPU {

    private String providerName;

    private String categoryName;

    private String brandName;

    private String seriesName;

    private List<ClientProductSKU> skuList;

    public ClientProductSPU(ProductSPU productSPU) {
        try {
            BeanUtils.copyProperties(this, productSPU);
        } catch (Exception e) {
            throw new IllegalStateException("ClientProductSPU construction error!");
        }
    }

    public List<ClientProductSKU> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<ClientProductSKU> skuList) {
        this.skuList = skuList;
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
}
