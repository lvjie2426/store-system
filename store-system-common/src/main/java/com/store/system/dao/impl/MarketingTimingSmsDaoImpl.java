package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.cache.CacheMethodParam;
import com.quakoo.space.annotation.cache.CacheSort;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.annotation.domain.SortKey;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.quakoo.space.enums.cache.CacheMethodParamEnum;
import com.quakoo.space.enums.cache.CacheSortOrder;
import com.store.system.dao.MarketingTimingSmsDao;
import com.store.system.model.MarketingTimingSms;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class MarketingTimingSmsDaoImpl extends CacheBaseDao<MarketingTimingSms> implements MarketingTimingSmsDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getListWithoutSharding)
    @CacheSort(order = CacheSortOrder.asc)
    public List<MarketingTimingSms> getList(int status, int send,
                                            @CacheMethodParam(paramEnum = CacheMethodParamEnum.size) int size) throws DataAccessException {
        return null;
    }

}
