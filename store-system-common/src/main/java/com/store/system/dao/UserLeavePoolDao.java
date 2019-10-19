package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.attendance.UserLeavePool;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface UserLeavePoolDao extends HDao<UserLeavePool> {

    public List<UserLeavePool> getAllList(long uid,Double cursor,int size);

    public int getCountByUser(long uid, int status) throws DataAccessException;

    public List<UserLeavePool> getPageListByUser(long uid, int status, double cursor, int size)
            throws DataAccessException;

}
