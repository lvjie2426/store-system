package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.attendance.PunchCardLog;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface PunchCardDao extends HDao<PunchCardLog> {

    public List<PunchCardLog> getAllList(long uid, double parseDouble, int size) throws DataAccessException;

    public List<PunchCardLog> getAllList(long uid, long day) throws DataAccessException;

    public int getCount(long uid) throws DataAccessException;
}
