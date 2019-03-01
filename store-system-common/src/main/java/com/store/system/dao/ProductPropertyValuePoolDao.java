package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.ProductPropertyValuePool;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProductPropertyValuePoolDao extends HDao<ProductPropertyValuePool> {

    public List<ProductPropertyValuePool> getAllList(long subid, long pnid) throws DataAccessException;

}
