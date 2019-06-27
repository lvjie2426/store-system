package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.InventoryStatisticsDao;
import com.store.system.model.InventoryStatistics;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * @ClassName InventoryStatisticsDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/27 15:51
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class InventoryStatisticsDaoImpl extends CacheBaseDao<InventoryStatistics> implements InventoryStatisticsDao{

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public List<InventoryStatistics> getSubList(long subId, long day) throws DataAccessException {
        return null;
    }
}
