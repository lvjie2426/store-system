package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.attendance.UserAttendanceStatistics;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface UserAttendanceStatisticsDao extends HDao<UserAttendanceStatistics> {

    public List<UserAttendanceStatistics> getUserStatisticsByMonth(long uid) throws DataAccessException;

}
