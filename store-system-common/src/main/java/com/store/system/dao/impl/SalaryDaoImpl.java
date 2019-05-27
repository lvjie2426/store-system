package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.store.system.dao.SalaryDao;
import com.store.system.model.Salary;

import java.util.List;
import java.util.Map;

/**
 * @ProjectName: store-system
 * @Package: com.store.system.dao.impl
 * @ClassName: SalaryDaoImpl
 * @Author: LiHaoJie
 * @Description: 工资表
 * @Date: 2019/5/27 15:48
 * @Version: 1.0
 */
@HyperspaceDao(type = HyperspaceType.cache)
public class SalaryDaoImpl extends CacheBaseDao<Salary> implements SalaryDao{

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<Salary> getAll(long uid) throws Exception {
        return null;
    }
}
