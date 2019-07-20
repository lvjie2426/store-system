package com.store.system.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: store-system
 * @description: 定制范围
 * @author: zhangmeng
 * @create: 2019-05-15 11:11
 **/

@Data
public class ProductCustomRange implements Serializable{

    private long ballId; //球

    private long columnId;//柱

}

