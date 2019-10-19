package com.store.system.service.impl;

import com.store.system.dao.UserSettingsDao;
import com.store.system.model.attendance.UserSettings;
import com.store.system.service.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-09-20 18:24
 **/
@Service
public class UserSettingsServiceImpl implements UserSettingsService {

    @Autowired
    private UserSettingsDao userSettingsDao;


    @Override
    public UserSettings load(long uid) throws Exception {
        UserSettings load = userSettingsDao.load(uid);
        if(load==null){
            UserSettings userSettings=new UserSettings();
            userSettings.setUid(uid);
            userSettings.setChangeShift_status(UserSettings.status_off);
            userSettings.setLeave_status(UserSettings.status_off);
            userSettings.setWorkOvertime_status(UserSettings.status_off);
            userSettings.setFillCard_status(UserSettings.status_off);
           return userSettingsDao.insert(userSettings);
        }
        return load;
    }

    @Override
    public void update(UserSettings userSettings) throws Exception {
        UserSettings load = userSettingsDao.load(userSettings.getUid());
        if(load!=null){
            userSettingsDao.update(userSettings);
        }
    }
}
