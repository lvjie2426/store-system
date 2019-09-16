package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.attendance.AttendanceLog;

import java.util.List;

public interface AttendanceLogDao extends HDao<AttendanceLog> {

    public List<AttendanceLog> getAllListBySubDay(long subId, long day);

    public List<AttendanceLog> getAllListBySubMonth(long sid, long month);

    public List<AttendanceLog> getAllListByUserMonth(long sid, long month, long uid);

    public List<AttendanceLog> getAllListByUserDay(long sid, long day, long uid);


}
