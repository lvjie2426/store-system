package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.UserMissionPool;

import java.util.List;

public interface UserMissionPoolDao extends HDao<UserMissionPool>{

    public List<UserMissionPool> getList(long mid, long uid)throws Exception;
}
