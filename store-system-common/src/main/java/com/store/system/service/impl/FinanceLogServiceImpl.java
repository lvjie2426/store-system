package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.store.system.client.ClientFinanceLog;
import com.store.system.client.ClientFinanceLogDetail;
import com.store.system.dao.FinanceLogDao;
import com.store.system.dao.StatisticsFinanceLogDao;
import com.store.system.model.FinanceLog;
import com.store.system.model.StatisticsFinanceLog;
import com.store.system.service.FinanceLogService;
import com.store.system.util.Constant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FinanceLogServiceImpl implements FinanceLogService {

    @Resource
    private FinanceLogDao financeLogDao;

    @Resource
    private StatisticsFinanceLogDao statisticsFinanceLogDao;


    private void _insertLog(int ownType, long ownId, int mode, int type, int subType, double money, String desc) {
        FinanceLog log = new FinanceLog();
        log.setOwnType(ownType);
        log.setOwnId(ownId);
        log.setMode(mode);
        log.setType(type);
        log.setSubType(subType);
        log.setMoney(money);
        log.setDesc(desc);
        log.setTime(System.currentTimeMillis());
        financeLogDao.insert(log);
    }

    @Override
    public void insertLog(final int ownType, final long ownId, final int mode, final int type,
                          final int subType, final double money, final String desc, boolean async) throws Exception {
        if(async) {
            Constant.sync_executor.submit(new Runnable() {
                @Override
                public void run() {
                    _insertLog(ownType, ownId, mode, type, subType, money, desc);
                }
            });
        } else {
            _insertLog(ownType, ownId, mode, type, subType, money, desc);
        }
    }

    @Override
    public ClientFinanceLogDetail getDetail(int mode, long startTime, long endTime) throws Exception {
        List<FinanceLog> financeLogs = financeLogDao.getBetweenTimeLogs(mode, startTime, endTime);
        double totalIn = 0;
        double totalOut = 0;
        List<ClientFinanceLog> logs = Lists.newArrayList();
        for(FinanceLog one : financeLogs) {
            ClientFinanceLog client = new ClientFinanceLog(one);
            logs.add(client);
            if(one.getType() == FinanceLog.type_in) totalIn += one.getMoney();
            else if(one.getType() == FinanceLog.type_out) totalOut += one.getMoney();
        }
        ClientFinanceLogDetail res = new ClientFinanceLogDetail();
        res.setLogs(logs);
        res.setTotalIn(totalIn);
        res.setTotalOut(totalOut);
        return res;
    }

    @Override
    public List<StatisticsFinanceLog> getStatistics(int mode, int startDate, int endDate) throws Exception {
        return statisticsFinanceLogDao.getBetweenDateLogs(mode, startDate, endDate);
    }

}
