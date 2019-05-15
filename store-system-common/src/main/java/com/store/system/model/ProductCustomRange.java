package com.store.system.model;

import lombok.Data;

/**
 * @program: store-system
 * @description: 定制范围
 * @author: zhangmeng
 * @create: 2019-05-15 11:11
 **/

@Data
public class ProductCustomRange {

    private long ballId; //球
    private long columnId;
    private long ballValue;//柱
    private long columnValue;



}

