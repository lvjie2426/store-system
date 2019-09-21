package com.store.system.service;

import com.store.system.model.attendance.UserSettings;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-09-20 18:24
 **/

public interface UserSettingsService  {

   public UserSettings load(long id)throws Exception;

    public void update(UserSettings userSettings)throws Exception;
}
