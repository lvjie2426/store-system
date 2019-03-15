package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.UserGrade;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface UserGradeDao extends HDao<UserGrade> {

    public List<UserGrade> getAllList(long subid) throws DataAccessException;

}
