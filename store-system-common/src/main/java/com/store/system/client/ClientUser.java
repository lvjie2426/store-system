package com.store.system.client;

import com.store.system.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

@EqualsAndHashCode(callSuper = true)
@Data
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

}
