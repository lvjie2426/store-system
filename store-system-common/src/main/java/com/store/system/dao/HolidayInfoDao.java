package com.store.system.dao;


import com.quakoo.space.interfaces.HDao;
import com.store.system.model.attendance.HolidayInfo;

import java.util.List;

public interface HolidayInfoDao extends HDao<HolidayInfo> {

    public List<HolidayInfo> getAll();
}
