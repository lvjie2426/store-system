package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.InventoryInBill;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface InventoryInBillDao extends HDao<InventoryInBill> {

    public List<InventoryInBill> getCheckPageList(long subid, double cursor, int size) throws DataAccessException;

    public List<InventoryInBill> getCreatePageList(long createUid, double cursor, int size) throws DataAccessException;

    public int getCheckCount(long subid) throws DataAccessException;

    public int getCreateCount(long subid) throws DataAccessException;
}
