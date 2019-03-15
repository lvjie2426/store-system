package com.store.system.service;

import com.store.system.model.UserGrade;

import java.util.List;

public interface UserGradeService {

    public UserGrade add(UserGrade customerGrade) throws Exception;

    public boolean update(UserGrade customerGrade) throws Exception;

    public boolean del(long id) throws Exception;


    public List<UserGrade> getAllList(long subid) throws Exception;

    public UserGrade loadGrade(long subid, long score) throws Exception;

}
