package com.store.system.dao.impl;


import com.store.system.dao.RoleDao;
import com.store.system.model.Role;
import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.cache.CacheSort;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.quakoo.space.enums.cache.CacheSortOrder;

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
