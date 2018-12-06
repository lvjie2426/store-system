package com.store.system.dao;

import com.store.system.model.RoleTemplateItem;
import com.s7.space.interfaces.HDao;

import java.util.List;

public interface RoleTemplateItemDao extends HDao<RoleTemplateItem> {


    public List<RoleTemplateItem> getAllList(long roleInitTemplateId);


}
