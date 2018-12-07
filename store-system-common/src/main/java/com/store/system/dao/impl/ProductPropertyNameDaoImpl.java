package com.store.system.dao.impl;

import com.s7.space.CacheBaseDao;
import com.s7.space.annotation.dao.HyperspaceDao;
import com.s7.space.enums.HyperspaceType;
import com.store.system.dao.ProductPropertyNameDao;
import com.store.system.model.ProductPropertyName;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class ProductPropertyNameDaoImpl extends CacheBaseDao<ProductPropertyName> implements ProductPropertyNameDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

}
