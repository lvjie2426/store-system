package com.store.system.dao.impl;


import com.store.system.dao.UserPermissionPoolDao;
import com.store.system.model.UserPermissionPool;
import com.s7.space.CacheBaseDao;
import com.s7.space.annotation.cache.CacheDaoMethod;
import com.s7.space.annotation.dao.HyperspaceDao;
import com.s7.space.enums.HyperspaceType;
import com.s7.space.enums.cache.CacheMethodEnum;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;


@HyperspaceDao(type = HyperspaceType.cache)
public class UserPermissionPoolDaoImpl extends CacheBaseDao<UserPermissionPool> implements UserPermissionPoolDao {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Map<String, List<String>> getCacheMap() {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<UserPermissionPool> getAllList(long uid) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getCountWithoutSharding)
    public int getCountWithPermission(long pid, int type) throws DataAccessException {
        return 0;
    }
}
