package com.store.system.service;


import com.store.system.model.Mission;
import com.store.system.model.UserMissionPool;

public interface UserMissionPoolService {

    public UserMissionPool update(UserMissionPool userMissionPool,Mission mission)throws Exception;

}
