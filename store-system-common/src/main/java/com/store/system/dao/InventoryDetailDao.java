package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.InventoryDetail;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface InventoryDetailDao extends HDao<InventoryDetail> {

    public List<InventoryDetail> getAllListByWidAndSKU(long wid, long p_skuid) throws DataAccessException;

    public List<InventoryDetail> getAllListByWidAndSPU(long wid, long p_spuid) throws DataAccessException;

    public List<InventoryDetail> getAllListByWid(long wid) throws DataAccessException;

    public List<InventoryDetail> getAllListBySubAndSPU(long subid, long p_spuid) throws DataAccessException;

    public List<InventoryDetail> getAllListBySubAndSKU(long subid, long p_skuid) throws DataAccessException;

    public List<InventoryDetail> getAllListBySKU(long p_skuid) throws DataAccessException;

    public List<InventoryDetail> getAllListBySPU(long p_spuid) throws DataAccessException;

}
