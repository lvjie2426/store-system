package com.store.system.service.impl;

import com.store.system.dao.UserMissionPoolDao;
import com.store.system.model.UserMissionPool;
import com.store.system.service.SubordinateMissionPoolService;
import com.store.system.service.UserMissionPoolService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserMissionPoolServiceImpl implements UserMissionPoolService{

    @Resource
    private UserMissionPoolDao userMissionPoolDao;

    @Override
    public UserMissionPool load(long mid, long uid) throws Exception {
        return userMissionPoolDao.load(mid,uid);
    }
}
