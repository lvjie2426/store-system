package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.attendance.SubordinateAttendanceStatistics;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface SubordinateAttendanceStatisticsDao extends HDao<SubordinateAttendanceStatistics> {

    public List<SubordinateAttendanceStatistics> getAllList(long sid, long month) throws DataAccessException;

}
