package com.store.system.dao;

import com.s7.space.interfaces.HDao;
import com.store.system.model.InventoryWarehouse;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface InventoryWarehouseDao extends HDao<InventoryWarehouse> {

    public List<InventoryWarehouse> getAllList(long subid, int status) throws DataAccessException;

}
