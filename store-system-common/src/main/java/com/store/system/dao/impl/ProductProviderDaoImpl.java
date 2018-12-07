package com.store.system.dao.impl;

import com.s7.space.CacheBaseDao;
import com.s7.space.annotation.dao.HyperspaceDao;
import com.s7.space.enums.HyperspaceType;
import com.store.system.dao.ProductProviderDao;
import com.store.system.model.ProductProvider;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class ProductProviderDaoImpl extends CacheBaseDao<ProductProvider> implements ProductProviderDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

}