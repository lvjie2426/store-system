package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.store.system.dao.ChangeShiftDao;
import com.store.system.model.attendance.ChangeShift;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ChangeShiftDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/12 15:52
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class ChangeShiftDaoImpl extends CacheBaseDao<ChangeShift> implements ChangeShiftDao {

    private static final long serialVersionUID = -1L;

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }
}
