package com.store.system.dao;

import com.store.system.model.GoodsBrand;
import com.s7.space.interfaces.HDao;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface GoodsBrandDao extends HDao<GoodsBrand> {

    public List<GoodsBrand> getAllList(int status) throws DataAccessException;


}
