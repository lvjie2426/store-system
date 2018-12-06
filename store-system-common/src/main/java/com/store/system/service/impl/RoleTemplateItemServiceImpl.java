package com.store.system.service.impl;

import com.store.system.dao.PermissionDao;
import com.store.system.dao.RoleTemplateItemDao;
import com.store.system.model.Permission;
import com.store.system.model.RoleTemplateItem;
import com.store.system.service.RoleTemplateItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class RoleTemplateItemServiceImpl implements RoleTemplateItemService {

    private  static Logger logger = LoggerFactory.getLogger(RoleTemplateItemServiceImpl.class);


    @Resource
    private RoleTemplateItemDao roleTemplateItemDao;

    @Resource
    private PermissionDao permissionDao;

    @Override
    public RoleTemplateItem add(RoleTemplateItem roleTemplateItem) throws Exception {
        return roleTemplateItemDao.insert(roleTemplateItem);
    }

    @Override
    public boolean update(RoleTemplateItem roleTemplateItem) throws Exception {
        return roleTemplateItemDao.update(roleTemplateItem);
    }

    @Override
    public boolean del(long id) throws Exception {
        return roleTemplateItemDao.delete(id);
    }

    @Override
    public List<RoleTemplateItem> getAll(long roleInitTemplateId) throws Exception{
        return roleTemplateItemDao.getAllList(roleInitTemplateId);

    }

    @Override
    public List<Permission> getRolePermissions(long id) throws Exception {
        List<Long> pids=roleTemplateItemDao.load(id).getPids();
        if (pids==null||pids.size() == 0) {
            return new ArrayList<>();
        }
        return permissionDao.load(pids);
    }


}
