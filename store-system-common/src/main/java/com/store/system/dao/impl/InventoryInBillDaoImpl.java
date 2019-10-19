package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.cache.CacheMethodParam;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.quakoo.space.enums.cache.CacheMethodParamEnum;
import com.store.system.dao.InventoryInBillDao;
import com.store.system.model.InventoryInBill;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class InventoryInBillDaoImpl extends CacheBaseDao<InventoryInBill> implements InventoryInBillDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }


    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getPageListWithoutSharding)
    public List<InventoryInBill> getCheckPageList(long subid,
                                             @CacheMethodParam(paramEnum = CacheMethodParamEnum.cursor) double cursor,
                                             @CacheMethodParam(paramEnum = CacheMethodParamEnum.size) int size) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getPageListWithoutSharding)
    public List<InventoryInBill> getCreatePageList(long createUid,
                                             @CacheMethodParam(paramEnum = CacheMethodParamEnum.cursor) double cursor,
                                             @CacheMethodParam(paramEnum = CacheMethodParamEnum.size) int size) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getCountWithoutSharding)
    public int getCheckCount(long subid) throws DataAccessException {
        return 0;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getCountWithoutSharding)
    public int getCreateCount(long subid) throws DataAccessException {
        return 0;
    }
}
