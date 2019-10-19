package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.attendance.AttendanceRanking;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName AttendanceRankingDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/12 15:50
 * @Version 1.0
 **/
public interface AttendanceRankingDao extends HDao<AttendanceRanking> {

    public List<AttendanceRanking> getSubListByDay(long sid, long subId, long day) throws DataAccessException;

    public List<AttendanceRanking> getSubListByMonth(long sid, long subId, long month) throws DataAccessException;

    public List<AttendanceRanking> getSubListByYear(long sid, long subId, long year) throws DataAccessException;
}
