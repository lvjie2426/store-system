package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.ProductSKU;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProductSKUDao extends HDao<ProductSKU> {

    public List<ProductSKU> getAllList(long spuid, int status) throws DataAccessException;

    public int getCount(long spuid, String code) throws DataAccessException;

    public List<ProductSKU> getAllList(long spuid, String code) throws DataAccessException;

}
