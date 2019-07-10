package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.SettlementDao;
import com.store.system.model.Settlement;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SettlementDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/7/8 16:05
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class SettlementDaoImpl extends CacheBaseDao<Settlement> implements SettlementDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<Settlement> getAllList(long subId) throws DataAccessException {
        return null;
    }
}
