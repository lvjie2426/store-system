package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.AfterSaleDetailDao;
import com.store.system.model.AfterSaleDetail;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * @ClassName AfterSaleDetailDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/10 18:20
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class AfterSaleDetailDaoImpl extends CacheBaseDao<AfterSaleDetail> implements AfterSaleDetailDao{
    @Override
    public Map<String, List<String>> getCacheMap() {
        return cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<AfterSaleDetail> getAllList(long asId) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getCountWithoutSharding)
    public int getCount(long oid) throws DataAccessException {
        return 0;
    }
}
