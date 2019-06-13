package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.bean.AfterSale;
import com.store.system.model.AfterSaleDetail;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName AfterSaleDetailDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/10 18:20
 * @Version 1.0
 **/
public interface AfterSaleDetailDao extends HDao<AfterSaleDetail> {

    public List<AfterSaleDetail> getAllList(long asId) throws DataAccessException;

    public int getCount(long oid) throws DataAccessException;

}
