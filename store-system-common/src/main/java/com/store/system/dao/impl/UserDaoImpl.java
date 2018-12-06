package com.store.system.dao.impl;


import com.store.system.dao.UserDao;
import com.store.system.model.User;
import com.s7.space.CacheBaseDao;
import com.s7.space.annotation.cache.CacheDaoMethod;
import com.s7.space.annotation.dao.HyperspaceDao;
import com.s7.space.enums.HyperspaceType;
import com.s7.space.enums.cache.CacheMethodEnum;
import com.s7.space.mapper.LongRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@HyperspaceDao(type = HyperspaceType.cache)
public class UserDaoImpl extends CacheBaseDao<User> implements UserDao {

    Logger logger= LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<User> getAllList() {
        return null;
    }

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }


    @Override
    public List<User> getAllList(long rid, int status) throws Exception {
        long startTime=System.currentTimeMillis();
        String sql="SELECT id FROM "+this.getTable(0)+" where status = ?";
        sql = sql + " and (`ridsJson` like '[" + rid+"]'  or `ridsJson` like '%," + rid+",%'  or `ridsJson` like '[" + rid+",%'   or `ridsJson` like '%," + rid+"]')";
        List<Long> result = this.getJdbcTemplate(0, true).query(sql,new LongRowMapper("id"),status);
        logger.info(daoClassName + "getlist sql:{},time:{}", new Object[]{sql,(System.currentTimeMillis()-startTime)});
        if (null != result&&result.size()>0) {
            return load(result);
        } else {
            return new ArrayList<>();
        }
    }

    //

    @Override
    public int getCount(long rid) throws DataAccessException {
        long startTime=System.currentTimeMillis();
        String sql="SELECT id FROM "+this.getTable(0)+" where 1=1 ";
        sql = sql + " and (`ridsJson` like '[" + rid+"]'  or `ridsJson` like '%," + rid+",%'  or `ridsJson` like '[" + rid+",%'   or `ridsJson` like '%," + rid+"]')";
        List<Long> result = this.getJdbcTemplate(0, true).query(sql,new LongRowMapper("id"));
        logger.info(daoClassName + "getlist sql:{},time:{}", new Object[]{sql,(System.currentTimeMillis()-startTime)});
        if (null != result&&result.size()>0) {
            return result.size();
        } else {
            return 0;
        }
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<User> getAllListByPhone(String phone) {
        return null;
    }


}
