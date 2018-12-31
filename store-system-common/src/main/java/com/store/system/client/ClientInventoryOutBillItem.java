package com.store.system.client;

import com.store.system.bean.InventoryOutBillItem;
import org.apache.commons.beanutils.BeanUtils;

public class ClientInventoryOutBillItem extends InventoryOutBillItem {

    private long spuid; //产品SPU的id

    private String spuName;

    private String spuIcon;

    private String spuCover;

    private long cid;

    private String categoryName;

    private long bid; //品牌ID

    private String brandName;

    private long sid; //系列ID

    private String seriesName;

    private String code; //产品编码

    private String propertyJson; //sku属性json

    public ClientInventoryOutBillItem(InventoryOutBillItem inventoryOutBillItem) {
        try {
            BeanUtils.copyProperties(this, inventoryOutBillItem);
        } catch (Exception e) {
            throw new IllegalStateException("ClientInventoryOutBillItem construction error!");
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

    public String getSpuCover() {
        return spuCover;
    }

    public void setSpuCover(String spuCover) {
        this.spuCover = spuCover;
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

    public String getPropertyJson() {
        return propertyJson;
    }

    public void setPropertyJson(String propertyJson) {
        this.propertyJson = propertyJson;
    }
}
