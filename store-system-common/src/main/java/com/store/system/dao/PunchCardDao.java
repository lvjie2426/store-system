package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.attendance.PunchCardLog;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface PunchCardDao extends HDao<PunchCardLog> {

    List<PunchCardLog> getAllList(long uid, double parseDouble, int size) throws DataAccessException;

    public int getCount(long uid) throws DataAccessException;
}
