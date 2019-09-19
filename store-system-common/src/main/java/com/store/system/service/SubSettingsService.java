package com.store.system.service;

import com.store.system.model.attendance.SubSettings;

/**
 * @ClassName SubSettingsService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/18 18:26
 * @Version 1.0
 **/
public interface SubSettingsService {

    public void update(SubSettings subSettings) throws Exception;

    public SubSettings load(long subId) throws Exception;
}
