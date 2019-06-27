package com.store.system.client;

import com.google.common.collect.Lists;
import com.store.system.model.SaleCategoryStatistics;
import lombok.Data;

import java.util.List;

/**
 * @ClassName ClientCategoryStatistics
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/20 18:11
 * @Version 1.0
 **/
@Data
public class ClientCategoryStatistics {

    private String cName; //分类名称

    private double sale;//产品分类销售额

    private double perPrice; //平均客单价

    private int num;//产品分类销售单数

    private double rate;//占比

    private double rate_0to100;

    private double rate_100to500;

    private double rate_500to1000;

    private double rate_1000to2000;

    private double rate_2000;

    private List<SaleCategoryStatistics> statistics = Lists.newArrayList();

}
