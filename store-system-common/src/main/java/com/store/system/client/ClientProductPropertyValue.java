package com.store.system.client;

import com.store.system.model.ProductPropertyValue;

import java.io.Serializable;

public class ClientProductPropertyValue implements Serializable {

    private long id;

    private String content; //显示内容

    public ClientProductPropertyValue() {
    }

    public ClientProductPropertyValue(ProductPropertyValue productPropertyValue) {
        this.id = productPropertyValue.getId();
        this.content = productPropertyValue.getContent();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
