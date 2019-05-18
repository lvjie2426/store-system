package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.PaymentDao;
import com.store.system.model.Payment;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * @ClassName PaymentDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/5/17 14:22
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class PaymentDaoImpl extends CacheBaseDao<Payment> implements PaymentDao{
    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<Payment> getUsedList(long psid, int payType, int type) throws DataAccessException {
        return null;
    }
}
