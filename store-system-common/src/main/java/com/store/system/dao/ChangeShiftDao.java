package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.attendance.ChangeShift;
import org.apache.commons.httpclient.util.DateParseException;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName ChangeShiftDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/12 15:52
 * @Version 1.0
 **/
public interface ChangeShiftDao extends HDao<ChangeShift>{

    public List<ChangeShift> getListByUid(long askUid)throws DataAccessException;

    public List<ChangeShift> getListByReplaceUid(long replaceUid)throws DataAccessException;
}
