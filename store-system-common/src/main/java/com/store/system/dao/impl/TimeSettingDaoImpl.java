package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.TimeSettingDao;
import com.store.system.model.attendance.TimeSetting;

import java.util.List;
import java.util.Map;

/**
 * @ClassName TimeSettingDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/5 17:39
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class TimeSettingDaoImpl extends CacheBaseDao<TimeSetting> implements TimeSettingDao {


    private static final long serialVersionUID = -1L;

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<TimeSetting> getAllList(long sid){
        return null;
    }

}
