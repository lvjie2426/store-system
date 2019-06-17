package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.StatisticsCustomerJobDao;
import com.store.system.model.StatisticsCustomerJob;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * @ProjectName: store-system
 * @Author: LiHaoJie
 * @Description:
 * @Date: 2019/6/15 16:18
 * @Version: 1.0
 */
@HyperspaceDao(type = HyperspaceType.cache)
public class StatisticsCustomerJobDaoImpl extends CacheBaseDao<StatisticsCustomerJob> implements StatisticsCustomerJobDao {
    @Override
    public Map<String, List<String>> getCacheMap() {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public List<StatisticsCustomerJob> getDayList(long subid, int day) throws DataAccessException {
        return null;
    }
    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public List<StatisticsCustomerJob> getWeekList(long subid, int week) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public List<StatisticsCustomerJob> getList(long subid) throws DataAccessException {
        return null;
    }
}
