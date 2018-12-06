package com.store.system.service;




import com.store.system.model.Permission;

import java.util.List;


public interface PermissionService {

    public Permission add(Permission permission) throws Exception;

    public boolean update(Permission permission) throws Exception;

    public boolean del(long id) throws Exception;

    /**
     * 获取所有的权限
     * @param subordinate 传入true将只获取学校的权限
     * @return
     * @throws Exception
     */
    public List<Permission> getAllList(boolean subordinate) throws Exception;

}
