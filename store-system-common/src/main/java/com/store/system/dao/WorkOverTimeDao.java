package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.attendance.WorkOverTime;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName WorkOverTimeDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/12 16:04
 * @Version 1.0
 **/
public interface WorkOverTimeDao extends HDao<WorkOverTime> {

   public List<WorkOverTime> getListByUid(long askUid,double cursor,int size)throws DataAccessException;

    public List<WorkOverTime> getAllListByUid(long id,int status)throws DataAccessException;

    public List<WorkOverTime> getAllListByDay(long askUid, int status, long day)throws DataAccessException;

    public List<WorkOverTime> getAllListByMonth(long askUid, int status, long month)throws DataAccessException;

    public List<WorkOverTime> getAllListByYear(long askUid, int status, long year)throws DataAccessException;
}
