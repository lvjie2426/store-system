package com.store.system.dao.impl;

import com.google.common.collect.Lists;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.dao.StatisticsFinanceLogDao;
import com.store.system.model.StatisticsFinanceLog;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Service
public class StatisticsFinanceLogDaoImpl implements StatisticsFinanceLogDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    private RowMapperHelp<StatisticsFinanceLog> rowMapper = new RowMapperHelp<>(StatisticsFinanceLog.class);

    @Override
    public void insert(StatisticsFinanceLog statisticsFinanceLog) throws DataAccessException {
        String sql = "insert into statistics_finance_log (`mode`, subType, totalIn, totalOut, `date`) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, statisticsFinanceLog.getMode(), statisticsFinanceLog.getSubType(), statisticsFinanceLog.getTotalIn(), statisticsFinanceLog.getTotalOut(),
                statisticsFinanceLog.getDate());
    }

    @Override
    public void delete(int date) throws DataAccessException {
        String sql = "delete from statistics_finance_log where `date` = ?";
        jdbcTemplate.update(sql, date);
    }

    @Override
    public List<StatisticsFinanceLog> getBetweenDateLogs(int mode, int startDate, int endDate) throws DataAccessException {
        String sql = "select * from statistics_finance_log where mode = ? and `date` >= ? and `date` < ? order by `date` asc";
        List<Object> params = Lists.newArrayList();
        params.add(mode);
        params.add(startDate);
        params.add(endDate);
        List<StatisticsFinanceLog> logs = jdbcTemplate.query(sql, params.toArray(), rowMapper);
        return logs;
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(calendar.getTime()));

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        System.out.println(sdf.format(calendar.getTime()));

    }

}
