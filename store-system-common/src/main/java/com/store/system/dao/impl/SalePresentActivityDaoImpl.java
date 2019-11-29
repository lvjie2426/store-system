package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.SalePresentActivityDao;
import com.store.system.model.SalePresentActivity;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SalePresentActivityDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 11:11
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class SalePresentActivityDaoImpl extends CacheBaseDao<SalePresentActivity> implements SalePresentActivityDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<SalePresentActivity> getAllList(long psid, int status, int open) throws DataAccessException {
        return null;
    }
}
