package com.store.system.dao;

import com.s7.space.interfaces.HDao;
import com.store.system.model.ProductBrand;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProductBrandDao extends HDao<ProductBrand> {

    public List<ProductBrand> getAllList(int status) throws DataAccessException;

}
