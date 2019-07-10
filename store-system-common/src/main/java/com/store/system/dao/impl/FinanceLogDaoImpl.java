package com.store.system.dao.impl;

import com.google.common.collect.Lists;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.dao.FinanceLogDao;
import com.store.system.model.FinanceLog;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FinanceLogDaoImpl implements FinanceLogDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    private RowMapperHelp<FinanceLog> rowMapper = new RowMapperHelp<>(FinanceLog.class);

    @Override
    public void insert(FinanceLog financeLog) throws DataAccessException {
        String sql = "insert into finance_log (ownType, ownId, `mode`, `type`, subType, money, `desc`, `day`, `time`) values (?, ?, ?, ?, ?, ?, ?, ?. ?)";
        jdbcTemplate.update(sql, financeLog.getOwnType(), financeLog.getOwnId(), financeLog.getMode(), financeLog.getType(),
                financeLog.getSubType(), financeLog.getMoney(), financeLog.getDesc(), financeLog.getTime());
    }

    @Override
    public List<FinanceLog> getDay(long subId, long day) throws DataAccessException {
        String sql = "select * from finance_log where `day` = ? and `subId` = ? order by `time` asc";
        List<Object> params = Lists.newArrayList();
        params.add(day);
        params.add(subId);
        List<FinanceLog> logs = this.jdbcTemplate.query(sql, params.toArray(), rowMapper);
        return logs;
    }


    @Override
    public List<FinanceLog> getBetweenTimeLogs(int mode, long startTime, long endTime) throws DataAccessException {
        String sql = "select * from finance_log where mode = ? and `time` >= ? and `time` < ? order by `time` asc";
        List<Object> params = Lists.newArrayList();
        params.add(mode);
        params.add(startTime);
        params.add(endTime);
        List<FinanceLog> logs = jdbcTemplate.query(sql, params.toArray(), rowMapper);
        return logs;
    }

    @Override
    public List<FinanceLog> getBetweenTimeLogs(long startTime, long endTime) throws DataAccessException {
        String sql = "select * from finance_log where `time` >= ? and `time` < ? order by `time` asc";
        List<Object> params = Lists.newArrayList();
        params.add(startTime);
        params.add(endTime);
        List<FinanceLog> logs = jdbcTemplate.query(sql, params.toArray(), rowMapper);
        return logs;
    }

}
