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


    public ClientStatisticsCustomer getCustomerCount(long subid, String date, int type)throws Exception;

    public ClientStatisticsCustomer getCustomerByTime(long subid,long startTime,long endTime)throws Exception;

    public ClientStatisticsCustomer getWebCustomerByTime(long subid,long startTime,long endTime)throws Exception;

}
