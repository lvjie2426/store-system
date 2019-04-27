package com.store.system.client;

import com.store.system.bean.InventoryCheckBillItem;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;
import java.util.Map;

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

    private Map<Long, Object> properties; //sku属性json

    public ClientInventoryCheckBillItem(InventoryCheckBillItem inventoryCheckBillItem) {
        try {
            BeanUtils.copyProperties(this, inventoryCheckBillItem);
        } catch (Exception e) {
            throw new IllegalStateException("ClientInventoryCheckBillItem construction error!");
        }
    }

    public long getSpuid() {
        return spuid;
    }

    public void setSpuid(long spuid) {
        this.spuid = spuid;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getSpuIcon() {
        return spuIcon;
    }

    public void setSpuIcon(String spuIcon) {
        this.spuIcon = spuIcon;
    }

    public List<String> getSpuCovers() {
        return spuCovers;
    }

    public void setSpuCovers(List<String> spuCovers) {
        this.spuCovers = spuCovers;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getBid() {
        return bid;
    }

    public void setBid(long bid) {
        this.bid = bid;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<Long, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<Long, Object> properties) {
        this.properties = properties;
    }
}
