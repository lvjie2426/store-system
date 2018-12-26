package com.store.system.dao.impl;

import com.store.system.dao.RoleTemplateItemDao;
import com.store.system.model.RoleTemplateItem;
import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;

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
