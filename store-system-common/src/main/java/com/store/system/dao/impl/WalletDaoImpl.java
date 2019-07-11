package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.store.system.dao.WalletDao;
import com.store.system.model.Wallet;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

@HyperspaceDao(type = HyperspaceType.cache)
public class WalletDaoImpl extends CacheBaseDao<Wallet> implements WalletDao {

    @Override
    public Map<String, List<String>> getCacheMap() {
        return super.cache_map();
    }

    @Override
    public double getTotalMoney() throws DataAccessException {
        String sql = "select sum(money) from wallet";
        Double res = this.getJdbcTemplate(0, false).getJdbcTemplate().queryForObject(sql, Double.class);
        return res;
    }

}
