package com.store.system.dao;

import com.store.system.model.FinanceLog;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface FinanceLogDao {

     public void insert(FinanceLog financeLog) throws DataAccessException;

     public List<FinanceLog> getBetweenTimeLogs(int mode, long startTime, long endTime) throws DataAccessException;

    public List<FinanceLog> getBetweenTimeLogs(long startTime, long endTime) throws DataAccessException;

}
