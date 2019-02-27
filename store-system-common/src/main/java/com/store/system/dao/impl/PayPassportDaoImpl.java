package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.store.system.dao.PayPassportDao;
import com.store.system.model.PayPassport;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class PayPassportDaoImpl extends CacheBaseDao<PayPassport> implements PayPassportDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return null;
    }

}
