package com.store.system.dao;


import com.store.system.model.UserPermissionPool;
import com.quakoo.space.interfaces.HDao;
import org.springframework.dao.DataAccessException;

import java.util.List;


public interface UserPermissionPoolDao extends HDao<UserPermissionPool> {

    public List<UserPermissionPool> getAllList(long uid) throws DataAccessException;

    public int getCountWithPermission(long pid, int type) throws DataAccessException;

}
