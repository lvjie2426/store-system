package com.store.system.dao.impl;

import com.store.system.dao.InBillItemDao;
import com.store.system.model.InBillItem;
import com.s7.space.CacheBaseDao;
import com.s7.space.annotation.cache.CacheDaoMethod;
import com.s7.space.annotation.dao.HyperspaceDao;
import com.s7.space.enums.HyperspaceType;
import com.s7.space.enums.cache.CacheMethodEnum;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class InBillItemDaoImpl extends CacheBaseDao<InBillItem> implements InBillItemDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InBillItem> getAllList(long ibid) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getCountWithoutSharding)
    public int getCount(long ibid) throws DataAccessException {
        return 0;
    }
}
