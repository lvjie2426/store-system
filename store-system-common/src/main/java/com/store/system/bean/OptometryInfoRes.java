package com.store.system.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 验光信息结果
 * class_name: OptometryInfoRes
 * package: com.store.system.bean
 * creat_user: lihao
 * creat_date: 2019/4/29
 * creat_time: 11:26
 **/
@Data
public class OptometryInfoRes implements Serializable {

    private OptometryInfoResItem rightResItem; //右眼结果

    private OptometryInfoResItem leftResItem; //左眼结果

}
