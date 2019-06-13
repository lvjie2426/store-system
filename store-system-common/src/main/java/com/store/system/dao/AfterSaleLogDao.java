package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.AfterSaleLog;
import org.springframework.dao.DataAccessException;

import java.util.List;


/**
 * @ClassName AfterSaleLogDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/10 18:19
 * @Version 1.0
 **/
public interface AfterSaleLogDao extends HDao<AfterSaleLog>{

    public List<AfterSaleLog> getList(long subId, long oid) throws DataAccessException;
}
