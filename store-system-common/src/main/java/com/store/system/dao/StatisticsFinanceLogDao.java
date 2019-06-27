package com.store.system.dao;

import com.store.system.model.StatisticsFinanceLog;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface StatisticsFinanceLogDao {

    public void insert(StatisticsFinanceLog statisticsFinanceLog) throws DataAccessException;

    public void delete(int date) throws DataAccessException;

    public List<StatisticsFinanceLog> getBetweenDateLogs(int mode, int startDate, int endDate) throws DataAccessException;

}
