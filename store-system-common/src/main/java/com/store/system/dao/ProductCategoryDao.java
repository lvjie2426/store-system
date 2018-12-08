package com.store.system.dao;

import com.s7.space.interfaces.HDao;
import com.store.system.model.ProductCategory;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProductCategoryDao extends HDao<ProductCategory> {

    public List<ProductCategory> getAllList(int status) throws DataAccessException;

}
