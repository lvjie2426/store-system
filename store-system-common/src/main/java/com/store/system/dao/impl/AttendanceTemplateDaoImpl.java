package com.store.system.dao.impl;


import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.AttendanceTemplateDao;
import com.store.system.model.attendance.AttendanceTemplate;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class AttendanceTemplateDaoImpl extends CacheBaseDao<AttendanceTemplate>
        implements AttendanceTemplateDao {

    private static final long serialVersionUID = -1L;

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }


    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<AttendanceTemplate> getAllList(long subId) {
        return null;
    }
}
