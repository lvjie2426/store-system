package com.store.system.dao;

import com.s7.space.interfaces.HDao;
import com.store.system.model.ProductPropertyName;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProductPropertyNameDao extends HDao<ProductPropertyName> {

    public List<ProductPropertyName> getAllList(long cid, int status) throws DataAccessException;

}
