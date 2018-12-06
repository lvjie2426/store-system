package com.store.system.dao;

import com.store.system.model.GoodsField;
import com.s7.space.interfaces.HDao;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface GoodsFieldDao extends HDao<GoodsField> {

    public List<GoodsField> getAllList(int type, int status) throws DataAccessException;

}
