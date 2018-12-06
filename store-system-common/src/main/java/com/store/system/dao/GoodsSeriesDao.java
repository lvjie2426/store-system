package com.store.system.dao;

import com.store.system.model.GoodsSeries;
import com.s7.space.interfaces.HDao;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface GoodsSeriesDao extends HDao<GoodsSeries> {

    public List<GoodsSeries> getAllList(long gbid, int status) throws DataAccessException;

}
