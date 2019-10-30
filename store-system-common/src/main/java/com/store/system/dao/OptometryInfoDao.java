package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.OptometryInfo;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface OptometryInfoDao extends HDao<OptometryInfo> {

    public List<OptometryInfo> getList(long uid, int size) throws DataAccessException;

    public List<OptometryInfo> getUserList(long uid)throws DataAccessException;
}
