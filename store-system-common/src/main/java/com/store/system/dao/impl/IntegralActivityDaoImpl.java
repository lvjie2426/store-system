package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.IntegralActivityDao;
import com.store.system.model.IntegralActivity;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IntegralActivityDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 14:05
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class IntegralActivityDaoImpl extends CacheBaseDao<IntegralActivity> implements IntegralActivityDao{

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }


    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<IntegralActivity> getAllList(long sid) throws DataAccessException {
        return null;
    }
}
