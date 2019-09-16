package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.UserLeaveDaysDao;
import com.store.system.dao.WorkOverTimeDao;
import com.store.system.model.attendance.UserLeaveDays;
import com.store.system.model.attendance.WorkOverTime;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * @ClassName WorkOverTimeDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/12 16:04
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class WorkOverTimeDaoImpl extends CacheBaseDao<WorkOverTime> implements WorkOverTimeDao {

    private static final long serialVersionUID = -1L;

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<WorkOverTime> getListByUid(long askUid) throws DataAccessException {
        return null;
    }
}
