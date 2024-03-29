package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.cache.CacheMethodParam;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.UserSettingsDao;
import com.store.system.model.attendance.UserSettings;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-09-20 18:25
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class UserSettingsDaoImpl extends CacheBaseDao<UserSettings> implements UserSettingsDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

}
