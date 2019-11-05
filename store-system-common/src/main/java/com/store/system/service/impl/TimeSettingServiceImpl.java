package com.store.system.service.impl;

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
    public TimeSetting add(TimeSetting city) throws Exception {
        return timeSettingDao.insert(city);
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
