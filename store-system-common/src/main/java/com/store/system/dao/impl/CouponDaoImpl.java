package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.CouponDao;
import com.store.system.model.Coupon;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * @ClassName CouponDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 15:27
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class CouponDaoImpl extends CacheBaseDao<Coupon> implements CouponDao{

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<Coupon> getAllList(long psid) throws DataAccessException {
        return null;
    }
}
