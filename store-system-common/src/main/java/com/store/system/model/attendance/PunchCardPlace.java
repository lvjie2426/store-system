package com.store.system.model.attendance;

import lombok.Data;

import java.io.Serializable;
@Data
public class PunchCardPlace implements Serializable {

    private String place; //打卡地点

    private String mapData; //经纬度数据

    private String distance; // 距离

    private int status; //启用&关闭

}
