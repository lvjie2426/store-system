package com.store.system.dao;

import com.store.system.model.GoodsProvider;
import com.s7.space.interfaces.HDao;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface GoodsProviderDao extends HDao<GoodsProvider> {

    public List<GoodsProvider> getAllList(int status) throws DataAccessException;

}
