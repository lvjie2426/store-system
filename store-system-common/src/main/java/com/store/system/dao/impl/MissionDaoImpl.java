package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.MissionDao;
import com.store.system.model.Mission;

import java.util.List;
import java.util.Map;

public class MissionDaoImpl extends CacheBaseDao<Mission> implements MissionDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return null;
    }

}
