package com.store.system.client;

import com.store.system.model.ProductPropertyValue;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;

@Data
public class ClientProductPropertyValue extends ProductPropertyValue implements Serializable {

    private long id;
    private String pnName;

    private String content; //显示内容

    public ClientProductPropertyValue() {
    }

    public ClientProductPropertyValue(ProductPropertyValue productPropertyValue) {
        try {
            BeanUtils.copyProperties(this, productPropertyValue);
        } catch (Exception e) {
            throw new IllegalStateException("ClientProductPropertyValue construction error!");
        }
    }

}
