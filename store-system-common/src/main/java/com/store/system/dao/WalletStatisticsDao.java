package com.store.system.dao;

import com.store.system.model.WalletStatistics;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface WalletStatisticsDao {

    public WalletStatistics load(int date) throws DataAccessException;

    public void insert(WalletStatistics walletStatistics) throws DataAccessException;

    public List<WalletStatistics> getBetweenDateList(int startDate, int endDate) throws DataAccessException;

}
