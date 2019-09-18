package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.attendance.AttendanceLog;

import java.util.List;

public interface AttendanceLogDao extends HDao<AttendanceLog> {

    public List<AttendanceLog> getAllListBySubDay(long subId, long day);

    public List<AttendanceLog> getAllListBySubMonth(long subId, long month);

    public List<AttendanceLog> getAllListBySubYear(long subId, long year);

    public List<AttendanceLog> getAllListBySubWeek(long subId, long week);

    public List<AttendanceLog> getAllListByUserMonth(long sid, long subId, long month, long uid);

    public List<AttendanceLog> getAllListByUserDay(long sid, long subId, long day, long uid);


}
