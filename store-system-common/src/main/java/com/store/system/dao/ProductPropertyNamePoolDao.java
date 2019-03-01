package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.ProductPropertyNamePool;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProductPropertyNamePoolDao extends HDao<ProductPropertyNamePool> {

    public List<ProductPropertyNamePool> getAllList(long subid, long cid) throws DataAccessException;

}
