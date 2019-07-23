package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.model.User;
import com.store.system.model.UserGrade;
import com.sun.jimi.core.util.P;

import java.util.List;

public interface UserGradeService {

    public UserGrade add(UserGrade customerGrade) throws Exception;

    public boolean update(UserGrade customerGrade) throws Exception;

    public boolean del(long id) throws Exception;

    public UserGrade load(long id) throws Exception;

    public List<UserGrade> getAllList(long subid) throws Exception;

    public Pager getByPager(Pager pager, long subid) throws Exception;

    public UserGrade loadGrade(long subid, long score) throws Exception;

    //积分升级
    public void checkUserScore(User user)throws Exception;

}
