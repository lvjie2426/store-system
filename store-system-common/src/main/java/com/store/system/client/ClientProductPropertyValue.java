package com.store.system.client;

import com.store.system.model.ProductPropertyValue;
import lombok.Data;

import java.io.Serializable;

@Data
public class ClientProductPropertyValue implements Serializable {

    private long id;
    private String pnName;

    private String content; //显示内容

    public ClientProductPropertyValue() {
    }

    public ClientProductPropertyValue(ProductPropertyValue productPropertyValue) {
        this.id = productPropertyValue.getId();
        this.content = productPropertyValue.getContent();
    }

}
