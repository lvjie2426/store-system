package com.store.system.client;

import com.store.system.model.User;
import org.apache.commons.beanutils.BeanUtils;

public class ClientUser extends User {

    private int couponNum;

    public ClientUser(User user) {
        try {
            BeanUtils.copyProperties(this, user);
            this.setPassword(null);
            this.setRand(-1);
        } catch (Exception e) {
            throw new IllegalStateException("user construction error!");
        }
    }

    public int getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(int couponNum) {
        this.couponNum = couponNum;
    }

}
