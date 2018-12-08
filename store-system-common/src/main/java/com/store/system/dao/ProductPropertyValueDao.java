package com.store.system.dao;

import com.s7.space.interfaces.HDao;
import com.store.system.model.ProductPropertyValue;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProductPropertyValueDao extends HDao<ProductPropertyValue> {

    public List<ProductPropertyValue> getAllList(long pnid, int status) throws DataAccessException;

}
