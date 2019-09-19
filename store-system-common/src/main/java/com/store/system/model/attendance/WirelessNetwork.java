package com.store.system.model.attendance;

import lombok.Data;

import java.io.Serializable;
@Data
public class WirelessNetwork implements Serializable {

    private String name; //wife名称

    private String number; //路由器编号

    private int status; //启用&关闭
}
