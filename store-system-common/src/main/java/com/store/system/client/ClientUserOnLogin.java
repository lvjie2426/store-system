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

    @Resource
    private SubordinateService subordinateService;
    private String token;

    private String subName;//门店名称

    private String  sName;//公司名称
    public ClientUserOnLogin() {
    }

    public ClientUserOnLogin(User user) {
        try {
            BeanUtils.copyProperties(this, user);
            this.setPassword(null);
            this.setRand(-1);
            this.token = user.createToken();
            if(user.getPsid()>0){
               Subordinate subordinate =  subordinateService.load(user.getPsid());
               if(subordinate!=null){
                   this.setSName(subordinate.getName());
               }
            }
            if(user.getSid()>0){
                Subordinate subordinate = subordinateService.load(user.getSid());
                if(subordinate!=null){
                    this.setSubName(subordinate.getName());
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("user construction error!");
        }
    }

}
