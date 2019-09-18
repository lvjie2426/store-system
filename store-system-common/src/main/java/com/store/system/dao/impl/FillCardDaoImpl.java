package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.cache.CacheMethodParam;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.quakoo.space.enums.cache.CacheMethodParamEnum;
import com.store.system.dao.FillCardDao;
import com.store.system.model.attendance.FillCard;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * @ClassName FillCardDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/17 18:57
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class FillCardDaoImpl extends CacheBaseDao<FillCard> implements FillCardDao{

    private static final long serialVersionUID = 1L;

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getPageListWithoutSharding)
    public List<FillCard> getPager(long id,
                                   @CacheMethodParam(paramEnum = CacheMethodParamEnum.cursor) double cursor,
                                   @CacheMethodParam(paramEnum = CacheMethodParamEnum.size) int size) throws DataAccessException {
        return null;
    }
}
