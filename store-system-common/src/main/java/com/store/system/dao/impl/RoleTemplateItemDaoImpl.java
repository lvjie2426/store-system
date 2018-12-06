package com.store.system.dao.impl;

import com.store.system.dao.RoleTemplateItemDao;
import com.store.system.model.RoleTemplateItem;
import com.s7.space.CacheBaseDao;
import com.s7.space.annotation.cache.CacheDaoMethod;
import com.s7.space.annotation.dao.HyperspaceDao;
import com.s7.space.enums.HyperspaceType;
import com.s7.space.enums.cache.CacheMethodEnum;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class RoleTemplateItemDaoImpl extends CacheBaseDao<RoleTemplateItem>
                                   implements RoleTemplateItemDao {

	private static final long serialVersionUID = -1l;

	@Override
	public Map<String, List<String>> getCacheMap() {
		return super.cache_map();
	}



	@Override
	@CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
	public List<RoleTemplateItem> getAllList(long roleInitTemplateId) {
		return null;
	}


}
