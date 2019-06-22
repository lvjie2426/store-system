package com.store.system.service;

import com.store.system.client.ClientCategoryStatistics;
import com.store.system.client.ClientSaleStatistics;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SaleCategoryStatisticsService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/20 14:51
 * @Version 1.0
 **/
public interface SaleCategoryStatisticsService {

    public Map<Long,ClientCategoryStatistics> getDayList(long subId, List<Long> days) throws Exception;

    public Map<Long,ClientCategoryStatistics> searchSale(long startTime, long endTime, long subId) throws Exception;

}
