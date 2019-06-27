package com.store.system.client;

import lombok.Data;

/**
 * @ClassName ClientInventoryStatistics
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/27 17:28
 * @Version 1.0
 **/
@Data
public class ClientInventoryStatistics{

    private String cName; //分类名称

    private int num;//库存总数

    private double price;//资产总额

}
