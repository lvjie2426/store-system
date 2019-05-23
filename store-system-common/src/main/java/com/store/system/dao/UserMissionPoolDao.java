package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.UserMissionPool;

public interface UserMissionPoolDao extends HDao<UserMissionPool>{

    public UserMissionPool load(long mid,long uid)throws Exception;
}
