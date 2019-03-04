package com.store.system.client;

import com.store.system.model.Customer;
import org.apache.commons.beanutils.BeanUtils;

public class ClientCustomer extends Customer {

    private String pSubName; //公司名称

    private String subName; //分店名称

    public ClientCustomer(Customer customer) {
        try {
            BeanUtils.copyProperties(this, customer);
        } catch (Exception e) {
            throw new IllegalStateException("ClientCustomer construction error!");
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
}
