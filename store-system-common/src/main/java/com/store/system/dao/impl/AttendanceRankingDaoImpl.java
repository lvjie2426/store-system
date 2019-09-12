package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.store.system.dao.AttendanceRankingDao;
import com.store.system.model.attendance.AttendanceRanking;

import java.util.List;
import java.util.Map;

/**
 * @ClassName AttendanceRankingDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/12 15:51
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class AttendanceRankingDaoImpl extends CacheBaseDao<AttendanceRanking>  implements AttendanceRankingDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return cache_map();
    }
}
