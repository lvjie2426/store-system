package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.LoginUserPool;
import com.store.system.model.UserLog;

import java.util.List;

public interface UserLogDao extends HDao<UserLog> {

   public List<UserLog> getAllByUid(long uid, int type)throws Exception;
}

