package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.SaleStatisticsDao;
import com.store.system.model.SaleStatistics;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SaleStatisticsDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/15 11:02
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class SaleStatisticsDaoImpl extends CacheBaseDao<SaleStatistics> implements SaleStatisticsDao {
    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public List<SaleStatistics> getDayList(long day, long subId) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public List<SaleStatistics> getWeekList(long week, long subId) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public List<SaleStatistics> getMonthList(long month, long subId) throws DataAccessException {
        return null;
    }
}
