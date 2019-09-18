package com.store.system.service.impl;

import com.store.system.dao.SubSettingsDao;
import com.store.system.model.attendance.SubSettings;
import com.store.system.service.SubSettingsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName SubSettingsServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/18 18:27
 * @Version 1.0
 **/
@Service
public class SubSettingsServiceImpl implements SubSettingsService {

    @Resource
    private SubSettingsDao subSettingsDao;


    @Override
    public void add(SubSettings subSettings) throws Exception {
        SubSettings dbInfo = subSettingsDao.load(subSettings.getSubId());
        if (dbInfo == null) {
            subSettingsDao.insert(subSettings);
        } else {
            subSettingsDao.update(subSettings);
        }
    }

    @Override
    public SubSettings load(long subId) throws Exception {
        return subSettingsDao.load(subId);
    }
}
