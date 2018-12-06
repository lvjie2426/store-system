package com.store.system.dao.impl;


import com.store.system.dao.RoleDao;
import com.store.system.model.Role;
import com.s7.space.CacheBaseDao;
import com.s7.space.annotation.cache.CacheDaoMethod;
import com.s7.space.annotation.cache.CacheSort;
import com.s7.space.annotation.dao.HyperspaceDao;
import com.s7.space.enums.HyperspaceType;
import com.s7.space.enums.cache.CacheMethodEnum;
import com.s7.space.enums.cache.CacheSortOrder;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class RoleDaoImpl extends CacheBaseDao<Role> implements RoleDao {
    @Override
    public Map<String, List<String>> getCacheMap() {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    @CacheSort(order = CacheSortOrder.asc)
    public List<Role> getAllList(long sid) {
        return null;
    }
}
