package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.cache.CacheSort;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.quakoo.space.enums.cache.CacheSortOrder;
import com.store.system.dao.AttendanceRankingDao;
import com.store.system.model.attendance.AttendanceRanking;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * @ClassName AttendanceRankingDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/12 15:51
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class AttendanceRankingDaoImpl extends CacheBaseDao<AttendanceRanking>  implements AttendanceRankingDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    @CacheSort(order = CacheSortOrder.desc)
    public List<AttendanceRanking> getSubListByDay(long sid, long subId, long day) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public List<AttendanceRanking> getSubListByMonth(long sid, long subId, long month) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public List<AttendanceRanking> getSubListByYear(long sid, long subId, long year) throws DataAccessException {
        return null;
    }
}
