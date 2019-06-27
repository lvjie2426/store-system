package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.store.system.dao.OrderNotifyDao;
import com.store.system.model.OrderNotify;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class OrderNotifyDaoImpl extends CacheBaseDao<OrderNotify> implements OrderNotifyDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return null;
    }

}
