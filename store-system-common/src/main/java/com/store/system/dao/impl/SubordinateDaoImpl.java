package com.store.system.dao.impl;

import com.store.system.dao.SubordinateDao;
import com.store.system.model.Subordinate;
import com.s7.space.CacheBaseDao;
import com.s7.space.annotation.cache.CacheDaoMethod;
import com.s7.space.annotation.dao.HyperspaceDao;
import com.s7.space.enums.HyperspaceType;
import com.s7.space.enums.cache.CacheMethodEnum;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class SubordinateDaoImpl extends CacheBaseDao<Subordinate> implements SubordinateDao {

	private static final long serialVersionUID = 5423831415281643567L;

	@Override
	public Map<String, List<String>> getCacheMap() {
		return super.cache_map();
	}

	@Override
	@CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
	public List<Subordinate> getAllList(int status) throws DataAccessException {
		return null;
	}

	@Override
	@CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
	public List<Subordinate> getAllSubordinateByProvince(long province, long city, int status) throws DataAccessException {
		return null;
	}

	@Override
	@CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
	public List<Subordinate> getIdByName(String name) throws DataAccessException {
		return null;
	}


}
