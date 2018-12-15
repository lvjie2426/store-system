package com.store.system.client;

import com.store.system.model.InventoryDetail;
import org.apache.commons.beanutils.BeanUtils;


public class ClientInventoryDetail extends InventoryDetail {

    private String warehouseName;

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

    private String p_propertyJson; //SKU 属性

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

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public int getP_type() {
        return p_type;
    }

    public void setP_type(int p_type) {
        this.p_type = p_type;
    }

    public long getP_pid() {
        return p_pid;
    }

    public void setP_pid(long p_pid) {
        this.p_pid = p_pid;
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

    public long getP_bid() {
        return p_bid;
    }

    public void setP_bid(long p_bid) {
        this.p_bid = p_bid;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public long getP_sid() {
        return p_sid;
    }

    public void setP_sid(long p_sid) {
        this.p_sid = p_sid;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_code() {
        return p_code;
    }

    public void setP_code(String p_code) {
        this.p_code = p_code;
    }

    public int getP_retailPrice() {
        return p_retailPrice;
    }

    public void setP_retailPrice(int p_retailPrice) {
        this.p_retailPrice = p_retailPrice;
    }

    public int getP_costPrice() {
        return p_costPrice;
    }

    public void setP_costPrice(int p_costPrice) {
        this.p_costPrice = p_costPrice;
    }

    public int getP_integralPrice() {
        return p_integralPrice;
    }

    public void setP_integralPrice(int p_integralPrice) {
        this.p_integralPrice = p_integralPrice;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public String getP_propertyJson() {
        return p_propertyJson;
    }

    public void setP_propertyJson(String p_propertyJson) {
        this.p_propertyJson = p_propertyJson;
    }
}
