package com.store.system.dao.impl;

import com.store.system.dao.GoodsProviderDao;
import com.store.system.model.GoodsProvider;
import com.s7.space.CacheBaseDao;
import com.s7.space.annotation.cache.CacheDaoMethod;
import com.s7.space.annotation.dao.HyperspaceDao;
import com.s7.space.enums.HyperspaceType;
import com.s7.space.enums.cache.CacheMethodEnum;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class GoodsProviderDaoImpl extends CacheBaseDao<GoodsProvider> implements GoodsProviderDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<GoodsProvider> getAllList(int status) throws DataAccessException {
        return null;
    }

}
