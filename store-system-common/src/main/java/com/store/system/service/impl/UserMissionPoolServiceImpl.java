package com.store.system.service.impl;

import com.store.system.dao.UserMissionPoolDao;
import com.store.system.model.UserMissionPool;
import com.store.system.service.SubordinateMissionPoolService;
import com.store.system.service.UserMissionPoolService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserMissionPoolServiceImpl implements UserMissionPoolService{

    @Resource
    private UserMissionPoolDao userMissionPoolDao;

    @Override
    public UserMissionPool load(long mid, long uid) throws Exception {
        return userMissionPoolDao.load(mid,uid);
    }

    @Override
    public UserMissionPool add(UserMissionPool userMissionPool) throws Exception {
        UserMissionPool oldUserMissionPool = userMissionPoolDao.load(userMissionPool);
        if(oldUserMissionPool==null){
            return userMissionPoolDao.insert(userMissionPool);
        }else{
            if(oldUserMissionPool.getNumber()!=userMissionPool.getNumber()){
                oldUserMissionPool.setNumber(userMissionPool.getNumber());
            }
            if(oldUserMissionPool.getPrice()!=userMissionPool.getPrice()){
                oldUserMissionPool.setPrice(userMissionPool.getPrice());
            }
            if(oldUserMissionPool.getProgress()!=userMissionPool.getProgress()){
                oldUserMissionPool.setProgress(userMissionPool.getProgress());
            }
            List<Long> oids = oldUserMissionPool.getOids();
            oids.add(userMissionPool.getOids().get(0));
            userMissionPool.setOids(oids);
            boolean flag = userMissionPoolDao.update(oldUserMissionPool);
            if(flag){
                return oldUserMissionPool;
            }
        }
        return null;
    }
}
