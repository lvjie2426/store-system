package com.store.system.dao.impl;

import com.quakoo.ext.RowMapperHelp;
import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.cache.CacheMethodParam;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.quakoo.space.enums.cache.CacheMethodParamEnum;
import com.store.system.dao.WalletDetailDao;
import com.store.system.model.WalletDetail;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class WalletDetailDaoImpl extends CacheBaseDao<WalletDetail> implements WalletDetailDao {

    private RowMapperHelp<WalletDetail> rowMapper = new RowMapperHelp<>(WalletDetail.class);

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }


    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getPageListWithoutSharding)
    public List<WalletDetail> getPageList(long uid, int type,
                                          @CacheMethodParam(paramEnum = CacheMethodParamEnum.cursor) double cursor,
                                          @CacheMethodParam(paramEnum = CacheMethodParamEnum.size) int size) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getPageListWithoutSharding)
    public List<WalletDetail> getPageList(long uid,
                                          @CacheMethodParam(paramEnum = CacheMethodParamEnum.cursor) double cursor,
                                          @CacheMethodParam(paramEnum = CacheMethodParamEnum.size) int size) throws DataAccessException {
        return null;
    }

    @Override
    public List<WalletDetail> getBetweenTimeList(long startTime, long endTime) throws DataAccessException {
        String sql = "select * from wallet_detail where ctime >= ? and ctime < ? order by ctime desc";
        List<WalletDetail> walletDetails = super.getJdbcTemplate(0, false).getJdbcTemplate().query(sql, rowMapper, startTime, endTime);
        return walletDetails;
    }

}
