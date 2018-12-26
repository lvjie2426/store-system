package com.store.system.dao;

import com.store.system.model.RoleInitTemplate;
import com.quakoo.space.interfaces.HDao;

import java.util.List;

public interface RoleInitTemplateDao extends HDao<RoleInitTemplate> {


    public List<RoleInitTemplate> getAllList();


}
