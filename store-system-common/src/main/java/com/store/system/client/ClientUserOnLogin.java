package com.store.system.client;

import com.store.system.model.Subordinate;
import com.store.system.model.User;
import com.store.system.service.SubordinateService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

import javax.annotation.Resource;

/**
 * 用户基本信息，包含登录token
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ClientUserOnLogin extends User {

    private String token;
    private String sName;//公司名称
    private String subName;//门店名称
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
