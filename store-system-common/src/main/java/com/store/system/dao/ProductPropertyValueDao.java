package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.ProductPropertyValue;
import org.springframework.dao.DataAccessException;

import javax.xml.crypto.Data;
import java.util.List;

public interface ProductPropertyValueDao extends HDao<ProductPropertyValue> {

    public List<ProductPropertyValue> getAllList(long pnid, int status) throws DataAccessException;

    public int getCount(long pnid, int status) throws DataAccessException;

}
