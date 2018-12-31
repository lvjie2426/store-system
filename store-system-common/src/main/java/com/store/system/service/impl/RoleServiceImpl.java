package com.store.system.service.impl;

import com.store.system.dao.PermissionDao;
import com.store.system.dao.RoleDao;
import com.store.system.dao.RolePermissionPoolDao;
import com.store.system.dao.UserDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Permission;
import com.store.system.model.Role;
import com.store.system.model.RolePermissionPool;
import com.store.system.service.RoleService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service("roleService")
public class RoleServiceImpl implements RoleService {

    private  static Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Resource
    private RoleDao roleDao;

    @Resource
    private RolePermissionPoolDao rolePermissionPoolDao;

    @Resource
    private PermissionDao permissionDao;

    @Resource
    private UserDao userDao;

    @Override
    public Role add(Role role) throws Exception {
        role = roleDao.insert(role);
        return role;
    }

    @Override
    public boolean update(Role role) throws Exception {
        long rid = role.getId();
        Role currentRole = roleDao.load(rid);
        if(null == currentRole) throw new StoreSystemException("角色不存在");
        currentRole.setRoleName(role.getRoleName());
        currentRole.setRemark(role.getRemark());
        currentRole.setSort(role.getSort());
        boolean sign = roleDao.update(currentRole);
        return sign;
    }

    @Override
    public boolean del(long id) throws Exception {
        Role currentRole = roleDao.load(id);
        if(null == currentRole) throw new StoreSystemException("角色不存在");
        int count = userDao.getCount(id);
        if(count > 0) throw new StoreSystemException("用户正在使用此角色");
        List<RolePermissionPool> pools = rolePermissionPoolDao.getAllList(id);
        for(RolePermissionPool pool : pools) {
            rolePermissionPoolDao.delete(pool);
        }
        roleDao.delete(id);
        return true;
    }

    private void loopTreePids(Set<Long> treePids, List<Long> pids) throws Exception {
        List<Permission> permissions = permissionDao.load(pids);
        List<Long> parentPids = Lists.newArrayList();
        for(Permission permission : permissions) {
            if(null != permission) {
                treePids.add(permission.getId());
                if(permission.getPid() > 0) parentPids.add(permission.getPid());
            }
        }
        if(parentPids.size() > 0) loopTreePids(treePids, parentPids);
    }

    private List<Long> getTreePids(List<Long> pids) throws Exception {
        Set<Long> treePids = Sets.newHashSet();
        loopTreePids(treePids, pids);
        return Lists.newArrayList(treePids);
    }

    @Override
    public boolean updateRolePermission(long rid, List<Long> pids) throws Exception {
        List<RolePermissionPool> pools = rolePermissionPoolDao.getAllList(rid);
        List<Long> nowPids = Lists.newArrayList();
        for(RolePermissionPool pool : pools) {
            nowPids.add(pool.getPid());
        }
        List<Long> newPids = getTreePids(pids);

        List<Long> addPids = Lists.newArrayList();
        List<Long> deletePids = Lists.newArrayList();

        for (long pid : newPids){
            if(!nowPids.contains(pid)){
                addPids.add(pid);
            }
        }

        for(long pid : nowPids) {
            if (!newPids.contains(pid)) {
                deletePids.add(pid);
            }
        }

        Map<Long, Permission> addPermMap = Maps.newHashMap();
        if(addPids.size() > 0) {
            List<Permission> addPermissions = permissionDao.load(addPids);
            for(Permission permission : addPermissions) {
                addPermMap.put(permission.getId(), permission);
            }
        }
        //增加
        for(long pid : addPids){
            RolePermissionPool pool = new RolePermissionPool();
            pool.setRid(rid);
            pool.setPid(pid);
            pool.setSort(addPermMap.get(pid).getSort());
            rolePermissionPoolDao.insert(pool);
        }

        //减少
        for(long pid : deletePids){
            RolePermissionPool pool = new RolePermissionPool();
            pool.setRid(rid);
            pool.setPid(pid);
            rolePermissionPoolDao.delete(pool);
        }

        return true;
    }

    @Override
    public List<Role> getAllList(long sid) throws Exception {
        return roleDao.getAllList(sid);
    }

    @Override
    public List<Permission> getRolePermissions(long rid) throws Exception {
        List<RolePermissionPool> pools = rolePermissionPoolDao.getAllList(rid);
        List<Long> pids = Lists.newArrayList();
        for(RolePermissionPool pool : pools) {
            pids.add(pool.getPid());
        }
        List<Permission> permissions = permissionDao.load(pids);
        Collections.sort(permissions);
        return permissions;
    }

}
