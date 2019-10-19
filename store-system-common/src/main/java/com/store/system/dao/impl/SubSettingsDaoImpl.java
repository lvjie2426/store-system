package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.store.system.dao.SubSettingsDao;
import com.store.system.model.attendance.SubSettings;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SubSettingsDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/12 15:58
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class SubSettingsDaoImpl extends CacheBaseDao<SubSettings> implements SubSettingsDao {

    private static final long serialVersionUID = -1L;

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }
}
