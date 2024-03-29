package com.store.system.dao;


import com.quakoo.space.interfaces.HDao;
import com.store.system.model.ProductSPU;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProductSPUDao extends HDao<ProductSPU> {

    public int getCount(long subid, long pid, long cid, long bid, long sid) throws DataAccessException;

    public List<ProductSPU> getAllList(long subid, long pid, long cid, long bid, long sid) throws DataAccessException;

    public List<ProductSPU> getAllList(long subid, long cid, long bid) throws DataAccessException;

    public List<ProductSPU> getAllList(long subid, long cid) throws DataAccessException;

    public List<ProductSPU> getAllList(long subid, long cid, long bid, long sid) throws DataAccessException;

}
