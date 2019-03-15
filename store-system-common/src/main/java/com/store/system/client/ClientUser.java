package com.store.system.client;

import com.store.system.model.User;
import org.apache.commons.beanutils.BeanUtils;

public class ClientUser extends User {

    private String pSubName; //公司名称

    private String subName; //分店名称

    public ClientUser(User user) {
        try {
            BeanUtils.copyProperties(this, user);
            this.setPassword(null);
            this.setRand(-1);
        } catch (Exception e) {
            throw new IllegalStateException("ClientUser construction error!");
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
