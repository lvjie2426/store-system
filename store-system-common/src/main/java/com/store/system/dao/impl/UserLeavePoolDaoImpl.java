package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.cache.CacheMethodParam;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.quakoo.space.enums.cache.CacheMethodParamEnum;
import com.store.system.dao.UserLeavePoolDao;
import com.store.system.model.attendance.UserLeavePool;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class UserLeavePoolDaoImpl extends CacheBaseDao<UserLeavePool>
        implements UserLeavePoolDao {

    private static final long serialVersionUID = -1L;

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }


    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getPageListWithoutSharding)
    public List<UserLeavePool> getAllList(long uid,
                                          @CacheMethodParam(paramEnum = CacheMethodParamEnum.cursor) Double cursor,
                                          @CacheMethodParam(paramEnum = CacheMethodParamEnum.size) int size) {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getCount)
    public int getCountByUser(long uid, int status) throws DataAccessException {
        return 0;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getPageList)
    public List<UserLeavePool> getPageListByUser(long uid, int status,
                                                   @CacheMethodParam(paramEnum = CacheMethodParamEnum.cursor) double cursor,
                                                   @CacheMethodParam(paramEnum = CacheMethodParamEnum.size) int size) throws DataAccessException {
        return null;
    }
}
