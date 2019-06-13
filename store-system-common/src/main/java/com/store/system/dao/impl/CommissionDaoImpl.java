package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.store.system.dao.CommissionDao;
import com.store.system.model.Commission;

import java.util.List;
import java.util.Map;

/**
 * @ClassName CommissionDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/13 19:24
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class CommissionDaoImpl extends CacheBaseDao<Commission> implements CommissionDao{
    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }
}
