package com.store.system.service.impl;

import com.store.system.dao.MissionDao;
import com.store.system.dao.UserMissionPoolDao;
import com.store.system.model.Mission;
import com.store.system.model.UserMissionPool;
import com.store.system.service.UserMissionPoolService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserMissionPoolServiceImpl implements UserMissionPoolService{

    @Resource
    private MissionDao missionDao;

    @Resource
    private UserMissionPoolDao userMissionPoolDao;

    @Override
    public UserMissionPool update(UserMissionPool userMissionPool,Mission mission) throws Exception {
        UserMissionPool oldUserMissionPool = userMissionPoolDao.load(userMissionPool);
        if(oldUserMissionPool!=null){
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
            //判断任务是否完成
            if(mission.getMissionStatus()==Mission.missionStatus_nofinish){
                if(userMissionPool.getProgress()>=100){
                    mission.setMissionStatus(Mission.missionStatus_finish);
                    missionDao.update(mission);
                }
            }
            if(flag){
                return oldUserMissionPool;
            }
        }
        return null;
    }
}
