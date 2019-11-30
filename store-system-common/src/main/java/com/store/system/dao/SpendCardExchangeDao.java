package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.SpendCardExchange;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName SpendCardExchangeDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/30 10:38
 * @Version 1.0
 **/
public interface SpendCardExchangeDao extends HDao<SpendCardExchange>{

    List<SpendCardExchange> getAllList(long psid, long spuId, int status) throws DataAccessException;

    int getCount(long psid, long spuId, int status) throws DataAccessException;
}
