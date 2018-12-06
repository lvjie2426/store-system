package com.store.system.dao.impl;

import com.store.system.dao.GoodsDao;
import com.store.system.model.Goods;
import com.s7.space.CacheBaseDao;
import com.s7.space.annotation.dao.HyperspaceDao;
import com.s7.space.enums.HyperspaceType;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class GoodsDaoImpl extends CacheBaseDao<Goods> implements GoodsDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

}
