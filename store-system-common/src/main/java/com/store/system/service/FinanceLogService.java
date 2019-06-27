package com.store.system.service;

import com.store.system.client.ClientFinanceLogDetail;
import com.store.system.model.StatisticsFinanceLog;

import java.util.List;


public interface FinanceLogService {

    public void insertLog(int ownType, long ownId, int mode, int type, int subType,
                          double money, String desc, boolean async) throws Exception;

    public ClientFinanceLogDetail getDetail(int mode, long startTime, long endTime) throws Exception;

    public List<StatisticsFinanceLog> getStatistics(int mode, int startDate, int endDate) throws Exception;

}
