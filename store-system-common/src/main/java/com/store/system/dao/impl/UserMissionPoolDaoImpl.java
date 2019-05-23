package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.SubordinateMissionPoolDao;
import com.store.system.dao.UserMissionPoolDao;
import com.store.system.model.UserMissionPool;

import java.util.List;
import java.util.Map;

public class UserMissionPoolDaoImpl extends CacheBaseDao<UserMissionPool> implements UserMissionPoolDao{

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public UserMissionPool load(long mid, long uid) throws Exception {
        return null;
    }
}
