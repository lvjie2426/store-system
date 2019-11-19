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
import com.store.system.util.TimeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FinanceLogServiceImpl implements FinanceLogService {

    @Resource
    private FinanceLogDao financeLogDao;

    @Resource
    private StatisticsFinanceLogDao statisticsFinanceLogDao;


    private void _insertLog(int ownType, long ownId, long subId, int mode, int type, int subType, double money, String desc) {
        FinanceLog log = new FinanceLog();
        log.setOwnType(ownType);
        log.setOwnId(ownId);
        log.setSubId(subId);
        log.setMode(mode);
        log.setType(type);
        log.setSubType(subType);
        log.setMoney(money);
        log.setDesc(desc);
        log.setDay(TimeUtils.getDayFormTime(System.currentTimeMillis()));
        log.setTime(System.currentTimeMillis());
        financeLogDao.insert(log);
    }

    @Override
    public void insertLog(final int ownType, final long subId, final long ownId, final int mode, final int type,
                          final int subType, final double money, final String desc, boolean async) throws Exception {
        if(async) {
            Constant.sync_executor.submit(new Runnable() {
                @Override
                public void run() {
                    _insertLog(ownType, ownId, subId, mode, type, subType, money, desc);
                }
            });
        } else {
            _insertLog(ownType, ownId, subId, mode, type, subType, money, desc);
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
    public ClientFinanceLogDetail getDay(long subId, long day) throws Exception {
        List<FinanceLog> financeLogs = financeLogDao.getDay(subId, day);
        double totalIn = 0;
        double totalOut = 0;
        double aliOut = 0;
        double aliIn = 0;
        double wxOut = 0;
        double wxIn = 0;
        double cashOut = 0;
        double cashIn = 0;
        double storedOut = 0;
        double storedIn = 0;
        List<ClientFinanceLog> logs = Lists.newArrayList();
        for(FinanceLog one : financeLogs) {
            ClientFinanceLog client = new ClientFinanceLog(one);
            logs.add(client);
            if(one.getType() == FinanceLog.type_in) totalIn += one.getMoney();
            else if(one.getType() == FinanceLog.type_out) totalOut += one.getMoney();
            if(one.getMode() == FinanceLog.mode_ali && one.getType() == FinanceLog.type_in){
                aliIn += one.getMoney();
            }
            if(one.getMode() == FinanceLog.mode_ali && one.getType() == FinanceLog.type_out){
                aliOut += one.getMoney();
            }
            if(one.getMode() == FinanceLog.mode_wx && one.getType() == FinanceLog.type_in){
                wxIn += one.getMoney();
            }
            if(one.getMode() == FinanceLog.mode_wx && one.getType() == FinanceLog.type_out){
                wxOut += one.getMoney();
            }
            if(one.getMode() == FinanceLog.mode_cash && one.getType() == FinanceLog.type_in){
                cashIn += one.getMoney();
            }
            if(one.getMode() == FinanceLog.mode_cash && one.getType() == FinanceLog.type_out){
                cashOut += one.getMoney();
            }
            if(one.getMode() == FinanceLog.mode_stored && one.getType() == FinanceLog.type_in){
                storedIn += one.getMoney();
            }
            if(one.getMode() == FinanceLog.mode_stored && one.getType() == FinanceLog.type_out){
                storedOut += one.getMoney();
            }
        }
        ClientFinanceLogDetail res = new ClientFinanceLogDetail();
        res.setLogs(logs);
        res.setTotalIn(totalIn);
        res.setTotalOut(totalOut);
        res.setAliIn(aliIn);
        res.setAliOut(aliOut);
        res.setWxIn(wxIn);
        res.setWxOut(wxOut);
        res.setCashIn(cashIn);
        res.setCashOut(cashOut);
        res.setStoredIn(storedIn);
        res.setStoredOut(storedOut);
        return res;
    }

    @Override
    public List<StatisticsFinanceLog> getStatistics(int mode, int startDate, int endDate) throws Exception {
        return statisticsFinanceLogDao.getBetweenDateLogs(mode, startDate, endDate);
    }

}
