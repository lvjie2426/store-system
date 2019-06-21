package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.SaleStatistics;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName SaleStatisticsDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/15 11:01
 * @Version 1.0
 **/
public interface SaleStatisticsDao extends HDao<SaleStatistics> {

    public List<SaleStatistics> getDayList(long day, long subId) throws DataAccessException;

    public List<SaleStatistics> getWeekList(long week, long subId) throws DataAccessException;

    public List<SaleStatistics> getMonthList(long month, long subId) throws DataAccessException;
}
