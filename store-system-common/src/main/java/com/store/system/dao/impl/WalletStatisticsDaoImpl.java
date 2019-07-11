package com.store.system.dao.impl;

import com.quakoo.ext.ResultSetExtractorHelp;
import com.quakoo.ext.RowMapperHelp;

import com.store.system.dao.WalletStatisticsDao;
import com.store.system.model.WalletStatistics;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WalletStatisticsDaoImpl implements WalletStatisticsDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    private RowMapperHelp<WalletStatistics> rowMapper = new RowMapperHelp<>(WalletStatistics.class);

    private ResultSetExtractorHelp<WalletStatistics> resultSetExtractor = new ResultSetExtractorHelp<>(WalletStatistics.class);

    @Override
    public WalletStatistics load(int date) throws DataAccessException {
        String sql = "select * from wallet_statistics where `date` = ?";
        WalletStatistics walletStatistics = jdbcTemplate.query(sql, resultSetExtractor, date);
        return walletStatistics;
    }

    @Override
    public void insert(WalletStatistics walletStatistics) throws DataAccessException {
        String sql = "insert into wallet_statistics (`date`, totalMoney, totalIn, totalOut) values (?, ?, ?, ?)";
        this.jdbcTemplate.update(sql, walletStatistics.getDate(), walletStatistics.getTotalMoney(), walletStatistics.getTotalIn(), walletStatistics.getTotalOut());
    }

    @Override
    public List<WalletStatistics> getBetweenDateList(int startDate, int endDate) throws DataAccessException {
        String sql = "select * from wallet_statistics where `date` >= ? and `date` < ? order by `date` asc";
        List<WalletStatistics> res = jdbcTemplate.query(sql, rowMapper, startDate, endDate);
        return res;
    }

}
