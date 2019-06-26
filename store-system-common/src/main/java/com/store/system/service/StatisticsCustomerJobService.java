package com.store.system.service;

import com.store.system.client.ClientStatisticsCustomer;

import java.util.List;
import java.util.Map;

/**
 * @ProjectName: store-system
 * @Author: LiHaoJie
 * @Description:
 * @Date: 2019/6/15 16:19
 * @Version: 1.0
 */
public interface StatisticsCustomerJobService {


    public List<ClientStatisticsCustomer> getCustomerCount(long subid, String date, int type)throws Exception;

    public List<ClientStatisticsCustomer> getCustomerByTime(long subid,long startTime,long endTime)throws Exception;

    public ClientStatisticsCustomer getCustomerBySub(List<Long> subIds,long startTime,long endTime, String date, int type)throws Exception;

}
