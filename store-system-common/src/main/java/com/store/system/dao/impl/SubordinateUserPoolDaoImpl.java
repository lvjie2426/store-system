package com.store.system.dao.impl;

import com.store.system.dao.SubordinateUserPoolDao;
import com.store.system.model.SubordinateUserPool;
import com.s7.space.CacheBaseDao;
import com.s7.space.annotation.cache.CacheDaoMethod;
import com.s7.space.annotation.cache.CacheMethodParam;
import com.s7.space.annotation.dao.HyperspaceDao;
import com.s7.space.enums.HyperspaceType;
import com.s7.space.enums.cache.CacheMethodEnum;
import com.s7.space.enums.cache.CacheMethodParamEnum;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class SubordinateUserPoolDaoImpl extends CacheBaseDao<SubordinateUserPool>
                                   implements SubordinateUserPoolDao {

	@Override
	public Map<String, List<String>> getCacheMap() {
		return super.cache_map();
	}

	@Override
	@CacheDaoMethod(methodEnum = CacheMethodEnum.getAllList)
	public List<SubordinateUserPool> getAllList(long sid,int status) throws DataAccessException {
		return null;
	}


	@Override
	@CacheDaoMethod(methodEnum = CacheMethodEnum.getCount)
	public int getCount(long sid,int status) throws DataAccessException {
		return 0;
	}

	@Override
	@CacheDaoMethod(methodEnum = CacheMethodEnum.getPageList)
	public List<SubordinateUserPool> getPageList(long sid, int status,
										   @CacheMethodParam(paramEnum = CacheMethodParamEnum.cursor) double cursor,
										   @CacheMethodParam(paramEnum = CacheMethodParamEnum.size) int size) throws DataAccessException {
		return null;
	}


}
