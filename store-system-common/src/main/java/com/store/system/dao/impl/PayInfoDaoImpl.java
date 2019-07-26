package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.store.system.dao.PayInfoDao;
import com.store.system.model.PayInfo;

import java.util.List;
import java.util.Map;

/**
 * @ClassName PayInfoDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/7/23 17:02
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class PayInfoDaoImpl extends CacheBaseDao<PayInfo> implements PayInfoDao{

    @Override
    public Map<String, List<String>> getCacheMap() {
        return cache_map();
    }
}
