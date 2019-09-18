package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.attendance.ApprovalLog;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName ApprovalLogDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/12 15:48
 * @Version 1.0
 **/
public interface ApprovalLogDao extends HDao<ApprovalLog> {

    public List<ApprovalLog> getList(long checkUid,double cursor,int size) throws DataAccessException;
}
