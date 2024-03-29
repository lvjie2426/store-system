package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.UserGradeCategoryDiscountDao;
import com.store.system.model.User;
import com.store.system.model.UserGradeCategoryDiscount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-05-15 15:15
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class UserGradeCategoryDiscountDaoImpl extends CacheBaseDao<UserGradeCategoryDiscount> implements UserGradeCategoryDiscountDao {
    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }


    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<UserGradeCategoryDiscount> getAllBySPUId(long spuid) throws Exception {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
    public UserGradeCategoryDiscount getDiscount(long spuid, long ugid) throws Exception {
        return null;
    }
}
