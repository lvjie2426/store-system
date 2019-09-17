package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.SubordinateAttendanceStatisticsDao;
import com.store.system.model.Subordinate;
import com.store.system.model.attendance.SubordinateAttendanceStatistics;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class SubordinateAttendanceStatisticsDaoImpl extends CacheBaseDao<SubordinateAttendanceStatistics>
        implements SubordinateAttendanceStatisticsDao {

    private static final long serialVersionUID = -1L;

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }


    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public List<SubordinateAttendanceStatistics> getAllList(long sid, long month) throws DataAccessException {
        return null;
    }


}
