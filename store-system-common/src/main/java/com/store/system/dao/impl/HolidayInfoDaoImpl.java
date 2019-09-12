package com.store.system.dao.impl;


import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.cache.CacheSort;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.quakoo.space.enums.cache.CacheSortOrder;
import com.store.system.dao.HolidayInfoDao;
import com.store.system.model.attendance.HolidayInfo;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class HolidayInfoDaoImpl extends CacheBaseDao<HolidayInfo>
        implements HolidayInfoDao {

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

    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    @CacheSort(order = CacheSortOrder.asc)
    public List<HolidayInfo> getAll(){
        return null;
    }

}
