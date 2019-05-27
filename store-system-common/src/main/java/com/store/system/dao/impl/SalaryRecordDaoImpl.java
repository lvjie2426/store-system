package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.store.system.dao.SalaryDao;
import com.store.system.dao.SalaryRecordDao;
import com.store.system.model.Salary;
import com.store.system.model.SalaryRecord;

import java.util.List;
import java.util.Map;

/**
 * @ProjectName: store-system
 * @Package: com.store.system.dao.impl
 * @ClassName: SalaryRecordDaoImpl
 * @Author: LiHaoJie
 * @Description: 工资表
 * @Date: 2019/5/27 15:48
 * @Version: 1.0
 */
@HyperspaceDao(type = HyperspaceType.cache)
public class SalaryRecordDaoImpl extends CacheBaseDao<SalaryRecord> implements SalaryRecordDao{

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }
}
