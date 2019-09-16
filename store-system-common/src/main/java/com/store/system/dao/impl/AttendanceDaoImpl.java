package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.AttendanceLogDao;
import com.store.system.model.attendance.AttendanceLog;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class AttendanceDaoImpl extends CacheBaseDao<AttendanceLog>
        implements AttendanceLogDao {

    private static final long serialVersionUID = -1L;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.DEFAULT_MAX_LIST_SIZE=100000;
        super.DEFAULT_INIT_LIST_SIZE=100000;
        super.afterPropertiesSet();
    }


    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public List<AttendanceLog> getAllListBySubDay(long sid, long day){
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public List<AttendanceLog> getAllListBySubMonth(long sid, long month){
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public List<AttendanceLog> getAllListByUserMonth(long sid,long month,long uid){
        return null;
    }


    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public List<AttendanceLog> getAllListByUserDay(long sid,long day,long uid){
        return null;
    }

}
