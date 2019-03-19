package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.ProductCustom;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProductCustomDao extends HDao<ProductCustom> {

    public List<ProductCustom> getAllList(long soid) throws DataAccessException;

}
