package com.store.system.dao.impl;


import com.store.system.dao.UserDao;
import com.store.system.model.User;
import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.quakoo.space.mapper.LongRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@HyperspaceDao(type = HyperspaceType.cache)
public class UserDaoImpl extends CacheBaseDao<User> implements UserDao {

    Logger logger= LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        DEFAULT_INIT_LIST_SIZE = 10000;
        super.afterPropertiesSet();
    }

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
        List<Long> result = this.getJdbcTemplate(0, true).getJdbcTemplate().query(sql,new LongRowMapper("id"),status);
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
    public List<User> getAllList(long sid, int userType, int status) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<User> getAllLists(int userType, int status,String contactPhone) throws DataAccessException {
        return null;
    }

    @Override
    public User getUserByPhone(int userType, int status,String contactPhone) throws Exception {
        long startTime=System.currentTimeMillis();
        String sql="SELECT id FROM "+this.getTable(0)+" where userType = ? and status = ? and contactPhone = ?";
        List<Long> result = this.getJdbcTemplate(0, true).getJdbcTemplate().query(sql,new LongRowMapper("id"),userType,status,contactPhone);
        logger.info(daoClassName + "getlist sql:{},time:{}", new Object[]{sql,(System.currentTimeMillis()-startTime)});
        if (null != result&&result.size()>0) {
            return (User) load(result);
        } else {
            return new User();
        }
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<User> getAllListByJob(int userType, int status, String job, long sid) throws Exception {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getPageListWithoutSharding)
    public List<User> getAllUserList(Long sid, int userType,double cursor,int size) throws Exception {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getPageListWithoutSharding)
    public List<User> getPagerAllList(Long sid, int userType, int status, double cursor, int size) throws Exception {
        return null;
    }
}
