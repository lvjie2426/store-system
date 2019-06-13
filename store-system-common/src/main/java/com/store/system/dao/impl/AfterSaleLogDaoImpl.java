package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.cache.CacheMethodParam;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.quakoo.space.enums.cache.CacheMethodParamEnum;
import com.store.system.dao.AfterSaleLogDao;
import com.store.system.model.AfterSaleLog;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * @ClassName AfterSaleLogDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/10 18:20
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class AfterSaleLogDaoImpl extends CacheBaseDao<AfterSaleLog> implements AfterSaleLogDao{
    @Override
    public Map<String, List<String>> getCacheMap() {
        return cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getCountWithoutSharding)
    public List<AfterSaleLog> getList(long subId, long oid,
                                      @CacheMethodParam(paramEnum = CacheMethodParamEnum.size) int size) throws DataAccessException {
        return null;
    }

}
