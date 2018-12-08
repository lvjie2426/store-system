package com.store.system.dao;

import com.s7.space.interfaces.HDao;
import com.store.system.model.ProductSKU;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProductSKUDao extends HDao<ProductSKU> {

    public List<ProductSKU> getAllList(long spuid, int status) throws DataAccessException;

}
