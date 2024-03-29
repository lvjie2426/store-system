package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.SpendCardExchangeDao;
import com.store.system.model.SpendCardExchange;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SpendCardExchangeDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/30 10:42
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class SpendCardExchangeDaoImpl extends CacheBaseDao<SpendCardExchange> implements SpendCardExchangeDao{

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<SpendCardExchange> getAllList(long psid, long spuId, int status) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<SpendCardExchange> getAllList(long psid, int status) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getCountWithoutSharding)
    public int getCount(long psid, long spuId, int status) throws DataAccessException {
        return 0;
    }
}
