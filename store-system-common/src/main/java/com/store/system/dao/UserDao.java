package com.store.system.dao;

import com.store.system.model.User;
import com.quakoo.space.interfaces.HDao;
import org.springframework.dao.DataAccessException;

import java.util.List;


public interface UserDao extends HDao<User> {

    public List<User> getAllList();

    public List<User> getAllList(long rid, int status) throws Exception;

    public int getCount(long rid) throws DataAccessException;


    public List<User> getAllList(long sid, int userType, int status) throws DataAccessException;

    public List<User> getAllListByPsid(long psid, int userType, int status) throws DataAccessException;

    public List<User> getAllLists( int userType, int status,String contactPhone) throws DataAccessException;

    public User getUserByPhone(int userType, int status,String contactPhone) throws Exception;

    public List<User> getAllListByJob(int userType,int status,String job,long sid)throws Exception;

    public List<User> getAllUserList(Long sid, int userType,double s,int size)throws Exception;

    public List<User> getPagerAllList(Long sid, int userType_backendUser, int status_nomore, double v, int i)throws Exception;
}
