package com.store.system.service;


import com.store.system.model.UserMissionPool;

public interface UserMissionPoolService {

    public UserMissionPool load (long mid,long uid)throws Exception;

    public UserMissionPool update(UserMissionPool userMissionPool)throws Exception;

}
