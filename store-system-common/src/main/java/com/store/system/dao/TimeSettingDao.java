package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.attendance.TimeSetting;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName TimeSettingDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/5 17:38
 * @Version 1.0
 **/
public interface TimeSettingDao extends HDao<TimeSetting>{

    List<TimeSetting> getAllList(long sid) throws DataAccessException;
}
