package com.store.system.dao.impl;

import com.store.system.dao.LoginUserPoolDao;
import com.store.system.model.LoginUserPool;
import com.s7.space.CacheBaseDao;
import com.s7.space.annotation.dao.HyperspaceDao;
import com.s7.space.enums.HyperspaceType;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class LoginUserPoolDaoImpl extends CacheBaseDao<LoginUserPool> implements LoginUserPoolDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return null;
    }
}
