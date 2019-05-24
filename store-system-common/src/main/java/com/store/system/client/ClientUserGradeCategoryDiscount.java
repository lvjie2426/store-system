package com.store.system.client;

import com.store.system.model.UserGradeCategoryDiscount;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-05-23 18:49
 **/
@Data
public class ClientUserGradeCategoryDiscount extends UserGradeCategoryDiscount {
    private  String ugName;

    public ClientUserGradeCategoryDiscount(){};
    public ClientUserGradeCategoryDiscount(UserGradeCategoryDiscount userGradeCategoryDiscount){
        try {
            BeanUtils.copyProperties(this, userGradeCategoryDiscount);
        } catch (Exception e) {
            throw new IllegalStateException("ClientUserGradeCategoryDiscount construction error!");
        }


    }

}
