package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.InventoryCheckBillDao;
import com.store.system.model.InventoryCheckBill;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class InventoryCheckBillDaoImpl extends CacheBaseDao<InventoryCheckBill> implements InventoryCheckBillDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getPageListWithoutSharding)
    public List<InventoryCheckBill> getAll(long subid, double cursor, int size) {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getPageListWithoutSharding)
    public List<InventoryCheckBill> getPageList(long subid, double cursor, int size) {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getPageListWithoutSharding)
    public List<InventoryCheckBill> getAllList(long subid, int status) {
        return null;
    }
}
