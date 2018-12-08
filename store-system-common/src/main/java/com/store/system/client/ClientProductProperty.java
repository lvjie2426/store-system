package com.store.system.client;

import com.store.system.model.ProductPropertyName;

import java.io.Serializable;
import java.util.List;

public class ClientProductProperty implements Serializable {

    private long id;

    private String content;


    private List<ClientProductPropertyValue> values;


    private int input; //是否输入属性 (输入的值非选项)

    private int defaul; //是否默认 (默认的不需要在页面上显示)

    private int multiple; //是否多值 (多值的是SKU要确定的属性, 单值的是SPU的属性)


    public ClientProductProperty() {
    }

    public ClientProductProperty(ProductPropertyName productPropertyName) {
        this.id = productPropertyName.getId();
        this.content = productPropertyName.getContent();
        this.input = productPropertyName.getInput();
        this.defaul = productPropertyName.getDefaul();
        this.multiple = productPropertyName.getMultiple();
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

    public List<ClientProductPropertyValue> getValues() {
        return values;
    }

    public void setValues(List<ClientProductPropertyValue> values) {
        this.values = values;
    }

    public int getInput() {
        return input;
    }

    public void setInput(int input) {
        this.input = input;
    }

    public int getDefaul() {
        return defaul;
    }

    public void setDefaul(int defaul) {
        this.defaul = defaul;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }
}
