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
}
