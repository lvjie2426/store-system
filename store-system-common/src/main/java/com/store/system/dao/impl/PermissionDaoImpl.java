package com.store.system.dao.impl;


import com.store.system.dao.PermissionDao;
import com.store.system.model.Permission;
import com.s7.space.CacheBaseDao;
import com.s7.space.annotation.cache.CacheDaoMethod;
import com.s7.space.annotation.dao.HyperspaceDao;
import com.s7.space.enums.HyperspaceType;
import com.s7.space.enums.cache.CacheMethodEnum;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class PermissionDaoImpl extends CacheBaseDao<Permission> implements PermissionDao {
    /**
     *
     */
    private static final long serialVersionUID = 1L;


    @Override
    public Map<String, List<String>> getCacheMap() {
        return null;
    }


    /**
     * 获取所有权限信息
     *
     * @return
     */
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    @Override
    public List<Permission> getAllList() {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<Permission> getAllList(int subordinate){
        return null;
    }


    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getCountWithoutSharding)
    public int getCount(long pid) throws DataAccessException {
        return 0;
    }

}
