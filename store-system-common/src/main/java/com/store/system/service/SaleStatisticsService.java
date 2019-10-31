package com.store.system.service;

import com.store.system.client.ClientSaleStatistics;

import java.util.List;

/**
 * @ClassName SaleStatisticsService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/15 11:05
 * @Version 1.0
 **/
public interface SaleStatisticsService {

    public ClientSaleStatistics getDayList(List<Long> days, List<Long> subIds) throws Exception;

    public ClientSaleStatistics getDay(long dayTime, List<Long> subIds) throws Exception;

    public ClientSaleStatistics getWeek(long week, List<Long> subIds) throws Exception;

    public ClientSaleStatistics getMonth(long month, List<Long> subIds) throws Exception;

    public ClientSaleStatistics searchSale(long startTime, long endTime, List<Long> subIds) throws Exception;

    public ClientSaleStatistics getDate(long day, List<Long> subIds) throws Exception;

}
