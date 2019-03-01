package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.ProductBrandPool;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProductBrandPoolDao extends HDao<ProductBrandPool> {

    public List<ProductBrandPool> getAllList(long subid) throws DataAccessException;

}
