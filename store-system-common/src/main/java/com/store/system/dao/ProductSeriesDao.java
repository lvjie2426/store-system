package com.store.system.dao;

import com.s7.space.interfaces.HDao;
import com.store.system.model.ProductSeries;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProductSeriesDao extends HDao<ProductSeries> {

    public List<ProductSeries> getAllList(long bid, int status) throws DataAccessException;

}
