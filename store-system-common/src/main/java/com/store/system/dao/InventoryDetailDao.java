package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.InventoryDetail;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface InventoryDetailDao extends HDao<InventoryDetail> {

    public List<InventoryDetail> getAllListByWidAndSKU(long wid, long p_skuid) throws DataAccessException;

    public List<InventoryDetail> getAllListByWidAndSPU(long wid, long p_spuid) throws DataAccessException;

    public List<InventoryDetail> getAllListByWid(long wid) throws DataAccessException;

    public List<InventoryDetail> getAllListBySubId(long subid) throws DataAccessException;

    public List<InventoryDetail> getAllListBySubAndSPU(long subid, long p_spuid) throws DataAccessException;

    public List<InventoryDetail> getAllListBySubAndSKU(long subid, long p_skuid) throws DataAccessException;

    public List<InventoryDetail> getAllListBySKU(long p_skuid) throws DataAccessException;

    public List<InventoryDetail> getAllListBySPU(long p_spuid) throws DataAccessException;

    public List<InventoryDetail> getPageList(long wid, long p_cid, double cursor, int size) throws DataAccessException;

    public int getCount(long wid, long p_cid) throws DataAccessException;

    public List<InventoryDetail> getAllListByWidAndCid(long wid, long p_cid) throws DataAccessException;

    public List<InventoryDetail> getAllListByWidAndCid(long wid) throws DataAccessException;

    public List<InventoryDetail> selectDetails(long wid, String search) throws Exception;

    public List<InventoryDetail> getAllListBySubId(long subid, int status) throws DataAccessException;

    public List<InventoryDetail> getAllListByWidAndCid(long wid, long p_cid, int status) throws DataAccessException;
}
