package com.store.system.dao;


import com.store.system.model.Permission;
import com.s7.space.interfaces.HDao;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface PermissionDao extends HDao<Permission> {

    public List<Permission> getAllList();

    //是否是下级单位的权限
    public List<Permission> getAllList(int subordinate);

    public int getCount(long pid) throws DataAccessException;


}
