package com.store.system.dao.impl;

import com.store.system.dao.GoodsBrandDao;
import com.store.system.model.GoodsBrand;
import com.s7.space.CacheBaseDao;
import com.s7.space.annotation.cache.CacheDaoMethod;
import com.s7.space.annotation.dao.HyperspaceDao;
import com.s7.space.enums.HyperspaceType;
import com.s7.space.enums.cache.CacheMethodEnum;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class GoodsBrandDaoImpl extends CacheBaseDao<GoodsBrand> implements GoodsBrandDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<GoodsBrand> getAllList(int status) throws DataAccessException {
        return null;
    }

}
