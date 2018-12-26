package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.InventoryDetail;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface InventoryDetailDao extends HDao<InventoryDetail> {

    public List<InventoryDetail> getAllList(long wid, long p_skuid) throws DataAccessException;

    public List<InventoryDetail> getAllListBySPU(long wid, long p_spuid) throws DataAccessException;

    public List<InventoryDetail> getAllList(long wid) throws DataAccessException;

}
