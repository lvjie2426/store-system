package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.CompanyDao;
import com.store.system.dao.SubordinateDao;
import com.store.system.model.Company;
import com.store.system.model.Subordinate;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class CompanyDaoImpl extends CacheBaseDao<Company> implements CompanyDao {

	private static final long serialVersionUID = 5423831415281643567L;

	@Override
	public Map<String, List<String>> getCacheMap() {
		return super.cache_map();
	}


	@Override
	@CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
	public List<Company> getAll() throws Exception {
		return null;
	}

	@Override
	@CacheDaoMethod(methodEnum = CacheMethodEnum.getCountWithoutSharding)
	public int getCount() throws Exception {
		return 0;
	}

}
