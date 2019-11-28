package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.ComboActivityDao;
import com.store.system.model.ComboActivity;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ComboActivityDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 14:29
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class ComboActivityDaoImpl extends CacheBaseDao<ComboActivity> implements ComboActivityDao{

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<ComboActivity> getAllList(long sid) throws DataAccessException {
        return null;
    }
}
