package com.store.system.service;

import com.store.system.model.attendance.TimeSetting;

import java.util.List;

/**
 * @ClassName TimeSettingService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/5 17:40
 * @Version 1.0
 **/
public interface TimeSettingService {

    TimeSetting add(TimeSetting timeSetting) throws Exception;

    void addList(List<TimeSetting> timeSettings, long sid) throws Exception;

    boolean del(long id) throws Exception;

    boolean update(TimeSetting timeSetting) throws Exception;

    List<TimeSetting> getAllList(long sid) throws Exception;
}
