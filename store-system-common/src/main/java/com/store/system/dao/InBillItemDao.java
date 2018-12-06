package com.store.system.dao;

import com.store.system.model.InBillItem;
import com.s7.space.interfaces.HDao;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface InBillItemDao extends HDao<InBillItem> {

    public List<InBillItem> getAllList(long ibid) throws DataAccessException;

    public int getCount(long ibid) throws DataAccessException;

}
