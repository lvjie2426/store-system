package com.store.system.dao;


import com.store.system.model.Role;
import com.s7.space.interfaces.HDao;

import java.util.List;


public interface RoleDao extends HDao<Role> {

    public List<Role> getAllList(long sid);
}
