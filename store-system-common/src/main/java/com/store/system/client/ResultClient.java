package com.store.system.client;

import java.io.Serializable;
import java.util.List;

public class ResultClient implements Serializable{

    private boolean success = true;

    private Object data;

    private  String msg;

    private int code;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ResultClient(Object data) {
        if(null != data) {
            this.success = true;
            this.data = data;
        } else
            this.success = false;
    }

    public ResultClient(List data) {
        if(null != data) {
            this.success = true;
            this.data = data;
        } else
            this.success = false;
    }

    public ResultClient(String msg) {
        this.success = false;
        this.msg = msg;
    }

    public ResultClient(Boolean b) {
        this.success = b;
    }

    public ResultClient(Boolean b, Object data) {
        this.success = b;
        this.data = data;
    }

    public ResultClient(boolean success, Object data, String msg) {
        this.success = success;
        this.data = data;
        this.msg = msg;
    }

    public ResultClient() {
    }
}
