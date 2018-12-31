package com.store.system.service.impl;


import com.store.system.dao.PermissionDao;
import com.store.system.dao.RolePermissionPoolDao;
import com.store.system.dao.UserPermissionPoolDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Permission;
import com.store.system.model.UserPermissionPool;
import com.store.system.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private PermissionDao permissionDao;

    @Resource
    private RolePermissionPoolDao rolePermissionPoolDao;

    @Resource
    private UserPermissionPoolDao userPermissionPoolDao;

    @Override
    public Permission add(Permission permission) throws Exception {
        permission = permissionDao.insert(permission);
        return permission;
    }

    @Override
    public boolean update(Permission permission) throws Exception {
        long id = permission.getId();
        Permission currentPermission = permissionDao.load(id);
        if(null == currentPermission) throw new StoreSystemException("权限不存在");
        currentPermission.setCssName(permission.getCssName());
        currentPermission.setHref(permission.getHref());
        currentPermission.setText(permission.getText());
        currentPermission.setSort(permission.getSort());
        currentPermission.setSubordinate(permission.getSubordinate());
        boolean sign = permissionDao.update(currentPermission);
        return sign;
    }

    @Override
    public boolean del(long id) throws Exception {
        Permission currentPermission = permissionDao.load(id);
        if(null == currentPermission) throw new StoreSystemException("许可不存在");
        int count = permissionDao.getCount(id);
        if(count > 0) throw new StoreSystemException("有子节点，不能删除");
        count = rolePermissionPoolDao.getCountWithPermission(id);
        if(count > 0) throw new StoreSystemException("该权限分配到角色，不能删除");
        count = userPermissionPoolDao.getCountWithPermission(id, UserPermissionPool.type_on);
        if(count > 0) throw new StoreSystemException("该权限分配到用户，不能删除");
        boolean sign = permissionDao.delete(id);
        return sign;
    }

    @Override
    public List<Permission> getAllList(boolean subordinate) throws Exception {
        if(subordinate){return permissionDao.getAllList(Permission.subordinate_on);}
        return permissionDao.getAllList();
    }




}
