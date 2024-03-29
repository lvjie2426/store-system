package com.store.system.dao;


import com.quakoo.space.interfaces.HDao;
import com.store.system.model.attendance.AttendanceTemplate;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface AttendanceTemplateDao extends HDao<AttendanceTemplate> {

    public List<AttendanceTemplate> getAllList(long subId);

    public List<AttendanceTemplate> getUserList(long uid) throws DataAccessException;
}
