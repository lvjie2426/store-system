package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.SpendCardSet;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName SpendCardSetDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/30 10:35
 * @Version 1.0
 **/
public interface SpendCardSetDao extends HDao<SpendCardSet>{

    List<SpendCardSet> getAllList(long psid, long cid, long spuId, int status) throws DataAccessException;

    int getSpuCount(long psid, long cid) throws DataAccessException;

    int getCateCount(long psid, long cid, long spuId, int status) throws DataAccessException;
}
