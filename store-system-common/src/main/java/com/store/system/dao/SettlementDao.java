package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.Settlement;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName SettlementDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/7/8 16:04
 * @Version 1.0
 **/
public interface SettlementDao extends HDao<Settlement> {

    public List<Settlement> getAllList(long subId) throws DataAccessException;
}
