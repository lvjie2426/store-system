package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.annotation.cache.CacheMethodParam;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.quakoo.space.enums.cache.CacheMethodEnum;
import com.quakoo.space.enums.cache.CacheMethodParamEnum;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.quakoo.space.mapper.LongRowMapper;
import com.store.system.dao.InventoryDetailDao;
import com.store.system.model.InventoryDetail;
import com.store.system.model.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class InventoryDetailDaoImpl extends CacheBaseDao<InventoryDetail> implements InventoryDetailDao {

    Logger logger= LoggerFactory.getLogger(InventoryDetailDaoImpl.class);

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InventoryDetail> getAllListByWidAndSKU(long wid, long p_skuid) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InventoryDetail> getAllListByWidAndSPU(long wid, long p_spuid) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InventoryDetail> getAllListByWid(long wid) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InventoryDetail> getAllListBySubId(long subid) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InventoryDetail> getAllListBySubAndSPU(long subid, long p_spuid) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InventoryDetail> getAllListBySubAndSKU(long subid, long p_skuid) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InventoryDetail> getAllListBySKU(long p_skuid) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InventoryDetail> getAllListBySPU(long p_spuid) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getPageListWithoutSharding)
    public List<InventoryDetail> getPageList(long wid, long p_cid,
                                           @CacheMethodParam(paramEnum = CacheMethodParamEnum.cursor) double cursor,
                                           @CacheMethodParam(paramEnum = CacheMethodParamEnum.size) int size) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getCountWithoutSharding)
    public int getCount(long wid, long p_cid) throws DataAccessException {
        return 0;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InventoryDetail> getAllListByWidAndCid(long wid, long p_cid) throws DataAccessException {
        return null;
    }

    @Override
    public List<InventoryDetail> selectDetails(long wid, String search) throws Exception {
        long startTime=System.currentTimeMillis();
        String sql=" SELECT d.* FROM `inventory_detail` AS d JOIN `product_spu` AS p ON d.p_spuid = p.id " +
                "JOIN `product_sku` AS pk ON d.p_skuid = pk.id" +
                " JOIN `product_brand` AS pb ON p.bid = pb.id " +
                "JOIN `product_series` AS ps ON p.sid = ps.id WHERE 1 = 1";
        if (StringUtils.isNotBlank(search)) {
            sql = sql + " AND pb.`name` LIKE '%" + search + "%'" + " OR  ps.`name` LIKE '%" + search + "%'" + " OR  pk.`code` LIKE '%" + search + "%'";
        }
        List<InventoryDetail> result = this.getJdbcTemplate(0, true).getJdbcTemplate().query(sql,new HyperspaceBeanPropertyRowMapper<>(InventoryDetail.class));
        logger.info(daoClassName + "getlist sql:{},time:{}", new Object[]{sql,(System.currentTimeMillis()-startTime)});
        if (null != result&&result.size()>0) {
            return load(result);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InventoryDetail> getAllListBySubId(long subid,  int status) throws DataAccessException {
        return null;
    }

    @Override
    @CacheDaoMethod(methodEnum = CacheMethodEnum.getAllListWithoutSharding)
    public List<InventoryDetail> getAllListByWidAndCid(long wid, long p_cid, int status) throws DataAccessException {
        return null;
    }

}
