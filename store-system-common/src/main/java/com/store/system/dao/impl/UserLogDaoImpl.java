package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.UserLogDao;
import com.store.system.dao.UserMissionPoolDao;
import com.store.system.model.UserLog;
import com.store.system.model.UserMissionPool;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class UserLogDaoImpl extends CacheBaseDao<UserLog> implements UserLogDao{

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }


    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<UserLog> getAllByUid(long uid,int type) throws Exception {
        return null;
    }
}
