package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.ProductCategoryPool;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProductCategoryPoolDao extends HDao<ProductCategoryPool> {

    public List<ProductCategoryPool> getAllList(long subid) throws DataAccessException;

}
