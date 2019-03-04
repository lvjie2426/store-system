package com.store.system.client;

import com.store.system.model.OptometryInfo;
import org.apache.commons.beanutils.BeanUtils;

public class ClientOptometryInfo extends OptometryInfo {

    private String pSubName; //公司名称

    private String subName; //分店名称

    private String customerName;

    private String optUserName;

    public ClientOptometryInfo(OptometryInfo optometryInfo) {
        try {
            BeanUtils.copyProperties(this, optometryInfo);
        } catch (Exception e) {
            throw new IllegalStateException("ClientOptometryInfo construction error!");
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOptUserName() {
        return optUserName;
    }

    public void setOptUserName(String optUserName) {
        this.optUserName = optUserName;
    }
}
