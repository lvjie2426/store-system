package com.store.system.service.impl;

import com.store.system.dao.RoleInitTemplateDao;
import com.store.system.dao.RoleTemplateItemDao;
import com.store.system.model.RoleInitTemplate;
import com.store.system.model.RoleTemplateItem;
import com.store.system.service.RoleInitTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("roleInitTemplateService")
public class RoleInitTemplateServiceImpl implements RoleInitTemplateService {

    private  static Logger logger = LoggerFactory.getLogger(RoleInitTemplateServiceImpl.class);

    @Resource
    private RoleInitTemplateDao roleInitTemplateDao;

    @Resource
    private RoleTemplateItemDao roleTemplateItemDao;

    @Override
    public RoleInitTemplate add(RoleInitTemplate roleInitTemplate) throws Exception {
        return roleInitTemplateDao.insert(roleInitTemplate);
    }

    @Override
    public boolean update(RoleInitTemplate roleInitTemplate) throws Exception {
        return roleInitTemplateDao.update(roleInitTemplate);
    }

    @Override
    public boolean del(long id) throws Exception {
        List<RoleTemplateItem> roleTemplateItems = roleTemplateItemDao.getAllList(id);
        for(RoleTemplateItem one : roleTemplateItems) {
            roleTemplateItemDao.delete(one.getId());
        }
        return roleInitTemplateDao.delete(id);
    }

    @Override
    public List<RoleInitTemplate> getAll() throws Exception {
        return roleInitTemplateDao.getAllList();
    }




}
