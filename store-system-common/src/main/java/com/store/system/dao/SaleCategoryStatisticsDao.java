package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.SaleCategoryStatistics;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName SaleCategoryStatisticsDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/18 14:35
 * @Version 1.0
 **/
public interface SaleCategoryStatisticsDao extends HDao<SaleCategoryStatistics>{

    public List<SaleCategoryStatistics> getSubList(long subId, long day) throws DataAccessException;

    public List<SaleCategoryStatistics> getSubList(long subId, long day, long cid) throws DataAccessException;
}
