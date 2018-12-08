package com.store.system.dao;

import com.s7.space.interfaces.HDao;
import com.store.system.model.ProductProvider;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProductProviderDao extends HDao<ProductProvider> {

    public List<ProductProvider> getAllList(int status) throws DataAccessException;

}
