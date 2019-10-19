package com.store.system.dao.impl;

import com.baidubce.services.media.model.Job;
import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.cache.CacheMethodParam;
import com.quakoo.space.annotation.cache.CacheSort;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.quakoo.space.enums.cache.CacheMethodParamEnum;
import com.quakoo.space.enums.cache.CacheSortOrder;
import com.store.system.dao.BusinessOrderDao;
import com.store.system.model.BusinessOrder;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * @ClassName BusinessDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/7/23 17:02
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class BusinessDaoImpl extends CacheBaseDao<BusinessOrder> implements BusinessOrderDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<BusinessOrder> getAllList(long subId, int status, int makeStatus) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<BusinessOrder> getAllList(long subId, int makeStatus) throws DataAccessException{
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<BusinessOrder> getUserList(long uid, int status, int makeStatus) throws DataAccessException{
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getPageListWithoutSharding)
    @CacheSort(order = CacheSortOrder.desc)
    public List<BusinessOrder> getUserList(long staffId, int status, int makeStatus,
                                           @CacheMethodParam(paramEnum = CacheMethodParamEnum.cursor) double cursor,
                                           @CacheMethodParam(paramEnum = CacheMethodParamEnum.size) int size) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getPageListWithoutSharding)
    public List<BusinessOrder> getPageList(long subId, int status, int makeStatus, long day,
                                           @CacheMethodParam(paramEnum = CacheMethodParamEnum.cursor) double cursor,
                                           @CacheMethodParam(paramEnum = CacheMethodParamEnum.size) int size) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getPageListWithoutSharding)
    public List<BusinessOrder> getPageList(long subId, int status, int makeStatus,
                                           @CacheMethodParam(paramEnum = CacheMethodParamEnum.cursor) double cursor,
                                           @CacheMethodParam(paramEnum = CacheMethodParamEnum.size) int size) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getCountWithoutSharding)
    public int getUserCount(long staffId, int status, int makeStatus) throws DataAccessException {
        return 0;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getCountWithoutSharding)
    public int getCount(long subId, int status, int makeStatus) throws DataAccessException {
        return 0;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getCountWithoutSharding)
    public int getCount(long subId, int status, int makeStatus, long day) throws DataAccessException {
        return 0;
    }
}
