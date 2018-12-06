package com.store.system.dao;

import com.store.system.model.GoodsFieldItem;
import com.s7.space.interfaces.HDao;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface GoodsFieldItemDao extends HDao<GoodsFieldItem>{

    public List<GoodsFieldItem> getAllList(long gfid, int status) throws DataAccessException;

}
