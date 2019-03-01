package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.ProductProviderPool;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProductProviderPoolDao extends HDao<ProductProviderPool> {

    public List<ProductProviderPool> getAllList(long subid) throws DataAccessException;

}
