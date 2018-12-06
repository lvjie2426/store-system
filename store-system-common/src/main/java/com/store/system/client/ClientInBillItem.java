package com.store.system.client;

import java.io.Serializable;

public class ClientInBillItem implements Serializable {

    private String name; //名称

    private int num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}
