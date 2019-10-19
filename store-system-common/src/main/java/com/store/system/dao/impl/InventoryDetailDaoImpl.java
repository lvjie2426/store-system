package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.cache.CacheMethodParam;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.quakoo.space.enums.cache.CacheMethodParamEnum;
import com.store.system.dao.InventoryDetailDao;
import com.store.system.model.InventoryDetail;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class InventoryDetailDaoImpl extends CacheBaseDao<InventoryDetail> implements InventoryDetailDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InventoryDetail> getAllListByWidAndSKU(long wid, long p_skuid) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InventoryDetail> getAllListByWidAndSPU(long wid, long p_spuid) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InventoryDetail> getAllListByWid(long wid) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InventoryDetail> getAllListBySubId(long subid) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InventoryDetail> getAllListBySubAndSPU(long subid, long p_spuid) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InventoryDetail> getAllListBySubAndSKU(long subid, long p_skuid) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InventoryDetail> getAllListBySKU(long p_skuid) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InventoryDetail> getAllListBySPU(long p_spuid) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getPageListWithoutSharding)
    public List<InventoryDetail> getPageList(long wid, long p_cid,
                                           @CacheMethodParam(paramEnum = CacheMethodParamEnum.cursor) double cursor,
                                           @CacheMethodParam(paramEnum = CacheMethodParamEnum.size) int size) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getCountWithoutSharding)
    public int getCount(long wid, long p_cid) throws DataAccessException {
        return 0;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InventoryDetail> getAllListByWidAndCid(long wid, long p_cid) throws DataAccessException {
        return null;
    }

}
