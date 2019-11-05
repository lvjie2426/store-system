package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.store.system.dao.TimeSettingDao;
import com.store.system.model.attendance.TimeSetting;
import com.store.system.service.TimeSettingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName TimeSettingServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/5 17:41
 * @Version 1.0
 **/
@Service
public class TimeSettingServiceImpl implements TimeSettingService{

    @Resource
    private TimeSettingDao timeSettingDao;

    @Override
    public TimeSetting add(TimeSetting timeSettings) throws Exception {
        return timeSettingDao.insert(timeSettings);
    }

    @Override
    public void addList(List<TimeSetting> timeSettings, long sid) throws Exception {
        List<String> strList = Lists.newArrayList();
        List<TimeSetting> timeSettingList = timeSettingDao.getAllList(sid);
        for(TimeSetting timeSetting:timeSettingList){
            strList.add(timeSetting.getInterval());
        }
        for(TimeSetting timeSetting:timeSettingList){
            if (!strList.contains(timeSetting.getInterval())){
                timeSettingDao.insert(timeSetting);
            }
        }
    }

    @Override
    public boolean del(long id) throws Exception {
        return timeSettingDao.delete(id);
    }

    @Override
    public boolean update(TimeSetting city) throws Exception {
        return timeSettingDao.update(city);
    }

    @Override
    public List<TimeSetting> getAllList(long sid) throws Exception {
        return timeSettingDao.getAllList(sid);
    }




}
