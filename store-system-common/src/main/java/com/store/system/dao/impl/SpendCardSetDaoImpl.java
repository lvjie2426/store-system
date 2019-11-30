package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.quakoo.space.mapper.LongRowMapper;
import com.store.system.dao.SpendCardSetDao;
import com.store.system.model.IntegralActivity;
import com.store.system.model.SpendCardSet;
import com.store.system.model.Subordinate;
import com.store.system.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SpendCardSetDaoImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/30 10:39
 * @Version 1.0
 **/
@HyperspaceDao(type = HyperspaceType.cache)
public class SpendCardSetDaoImpl extends CacheBaseDao<SpendCardSet> implements SpendCardSetDao{


    Logger logger= LoggerFactory.getLogger(SpendCardSetDaoImpl.class);


    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<SpendCardSet> getAllList(long psid, long cid, long spuId, int status) throws DataAccessException {
        return null;
    }

    @Override
    public int getSpuCount(long psid, long cid) throws DataAccessException {
        long startTime = System.currentTimeMillis();
        String sql = "SELECT COUNT(*) FROM " + this.getTable(0) + " where 1=1 ";
        if (psid > 0) {
            sql = sql + " AND `psid` = " + psid;
        }
        if (cid > 0) {
            sql = sql + " AND `cid` = " + cid;
        }
        sql = sql + " AND `spuId` > " + 0;
        sql = sql + " AND `type` = " + SpendCardSet.TYPE_SPU;
        sql = sql + " AND `status` = " + Constant.STATUS_NORMAL;
        System.err.println(sql);
        int count = this.getJdbcTemplate(0, true).getJdbcTemplate().queryForObject(sql, Integer.class);
        logger.info(daoClassName + "getlist sql:{},time:{}", new Object[]{sql, (System.currentTimeMillis() - startTime)});
        return count;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getCountWithoutSharding)
    public int getCateCount(long psid, long cid, long spuId, int status) throws DataAccessException {
        return 0;
    }
}
