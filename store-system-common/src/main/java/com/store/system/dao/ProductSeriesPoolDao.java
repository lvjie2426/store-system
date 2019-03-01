package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.ProductSeriesPool;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProductSeriesPoolDao extends HDao<ProductSeriesPool> {

    public List<ProductSeriesPool> getAllList(long subid, long bid) throws DataAccessException;

}
