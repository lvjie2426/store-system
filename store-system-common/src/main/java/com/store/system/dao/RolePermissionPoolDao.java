package com.store.system.dao;


import com.store.system.model.RolePermissionPool;
import com.quakoo.space.interfaces.HDao;
import org.springframework.dao.DataAccessException;

import java.util.List;


public interface RolePermissionPoolDao extends HDao<RolePermissionPool> {

    public List<RolePermissionPool> getAllList(long rid) throws DataAccessException;

    public int getCountWithPermission(long pid) throws DataAccessException;

}
