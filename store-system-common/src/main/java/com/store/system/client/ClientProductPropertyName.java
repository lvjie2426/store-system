package com.store.system.client;

import com.store.system.model.PayPassport;
import com.store.system.model.ProductPropertyName;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-07-11 17:49
 **/
@Data
public class ClientProductPropertyName extends ProductPropertyName {

    private String cName;

    public ClientProductPropertyName(ProductPropertyName productPropertyName) {
        try {
            BeanUtils.copyProperties(this, productPropertyName);
        } catch (Exception e) {
            throw new IllegalStateException("ClientProductPropertyName construction error!");
        }
    }
}
