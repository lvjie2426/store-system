package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.SubordinateMissionPoolDao;
import com.store.system.model.SubordinateMissionPool;

import java.util.List;
import java.util.Map;

public class SubordinateMissionPoolDaoImpl extends CacheBaseDao<SubordinateMissionPool> implements SubordinateMissionPoolDao{

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public SubordinateMissionPool load(long mid, long sid) throws Exception {
        return null;
    }
}
