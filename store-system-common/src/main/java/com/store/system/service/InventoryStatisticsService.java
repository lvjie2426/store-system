package com.store.system.service;

import com.store.system.client.ClientInventoryStatistics;

import java.util.List;
import java.util.Map;

/**
 * @ClassName InventoryStatisticsService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/27 16:34
 * @Version 1.0
 **/
public interface InventoryStatisticsService {

    public Map<Long,List<ClientInventoryStatistics>> getDayList(long subId, List<Long> days) throws Exception;

    public Map<Long,List<ClientInventoryStatistics>> searchSale(long startTime, long endTime, long subId) throws Exception;
}
