package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.ProductPropertyValuePoolDao;
import com.store.system.model.ProductPropertyValuePool;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class ProductPropertyValuePoolDaoImpl extends CacheBaseDao<ProductPropertyValuePool> implements ProductPropertyValuePoolDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<ProductPropertyValuePool> getAllList(long subid, long pnid) throws DataAccessException {
        return null;
    }

}
