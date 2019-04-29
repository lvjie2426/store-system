package com.store.system.client;

import com.store.system.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

/**
 * 用户基本信息，包含登录token
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ClientUserOnLogin extends User {

    private String token;

    public ClientUserOnLogin() {
    }

    public ClientUserOnLogin(User user) {
        try {
            BeanUtils.copyProperties(this, user);
            this.setPassword(null);
            this.setRand(-1);
            this.token = user.createToken();
        } catch (Exception e) {
            throw new IllegalStateException("user construction error!");
        }
    }

}
