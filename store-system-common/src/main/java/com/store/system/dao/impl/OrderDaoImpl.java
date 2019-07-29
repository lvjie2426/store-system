package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.cache.CacheSort;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.quakoo.space.enums.cache.CacheSortOrder;
import com.store.system.dao.OrderDao;
import com.store.system.model.Order;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class OrderDaoImpl extends CacheBaseDao<Order> implements OrderDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

//    @Override
//    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
//    public List<Order> getAllBySubid(long subid, int status, int makeStatus) {
//        return null;
//    }
//
//    @Override
//    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
//    public List<Order> getTemporaryOrder(long subid, int makeStatus) {
//        return null;
//    }
//
//    @Override
//    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
//    @CacheSort(order = CacheSortOrder.desc)
//    public List<Order> getUserFinishOrders(long uid, int makeStatus) throws Exception {
//        return null;
//    }
}
