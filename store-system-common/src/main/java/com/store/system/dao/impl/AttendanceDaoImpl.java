package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.AttendanceLogDao;
import com.store.system.model.attendance.AttendanceLog;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class AttendanceDaoImpl extends CacheBaseDao<AttendanceLog>
        implements AttendanceLogDao {

    private static final long serialVersionUID = -1L;

    @Resource
    private JdbcTemplate jdbcTemplate;

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

/*
    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public List<AttendanceLog> getAllListBySchoolDay(long sid,long day,int type){
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public List<AttendanceLog> getAllListBySchoolMonth(long sid,long month,int type){
        return null;
    }


    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public List<AttendanceLog> getAllListByClazzDay(long sid,long clazzId,long day,int type){
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public List<AttendanceLog> getAllListByClazzMonth(long sid,long clazzId,long month,int type){
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public List<AttendanceLog> getAllListByUserMonth(long sid,long month,long uid,int type){
        return null;
    }

    @Override
    public int getAllListByClazzId(long clazzId, long day,int type) {
        String sql = "select count(sid) from attendance_log where day ="+day+" and clazzId = "+clazzId+" and type="+type;
        int count = jdbcTemplate.queryForObject(sql, Integer.class);;
        return count;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public List<AttendanceLog> getAllListByUserDay(long sid,long day,long uid,int type){
        return null;
    }
*/

}
