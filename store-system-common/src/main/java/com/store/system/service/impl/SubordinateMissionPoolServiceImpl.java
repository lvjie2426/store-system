package com.store.system.service.impl;

import com.quakoo.ext.RowMapperHelp;
import com.store.system.dao.MissionDao;
import com.store.system.dao.SubordinateMissionPoolDao;
import com.store.system.model.Mission;
import com.store.system.model.SubordinateMissionPool;
import com.store.system.service.SubordinateMissionPoolService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SubordinateMissionPoolServiceImpl  implements SubordinateMissionPoolService{

    @Resource
    private SubordinateMissionPoolDao subordinateMissionPoolDao;

    @Resource
    private MissionDao missionDao;


    @Override
    public SubordinateMissionPool update(SubordinateMissionPool subordinateMissionPool,Mission mission) throws Exception {
        SubordinateMissionPool oldSubordinateMission = subordinateMissionPoolDao.load(subordinateMissionPool);
        if(oldSubordinateMission!=null){
            if (oldSubordinateMission.getNumber()!=subordinateMissionPool.getNumber()){
                oldSubordinateMission.setNumber(subordinateMissionPool.getNumber());
            }
            if(oldSubordinateMission.getProgress()!=subordinateMissionPool.getProgress()){
                oldSubordinateMission.setProgress(subordinateMissionPool.getProgress());
            }
            if(oldSubordinateMission.getPrice()!=subordinateMissionPool.getPrice()){
                oldSubordinateMission.setPrice(subordinateMissionPool.getPrice());
            }
            List<Long> oids = oldSubordinateMission.getOids();
            oids.add(subordinateMissionPool.getOids().get(0));
            oldSubordinateMission.setOids(oids);
            boolean flag = subordinateMissionPoolDao.update(oldSubordinateMission);
            //判断任务是否完成
            if(mission.getMissionStatus()==Mission.missionStatus_nofinish){
                if(subordinateMissionPool.getProgress()>=100){
                    mission.setMissionStatus(Mission.missionStatus_finish);
                    missionDao.update(mission);
                }
            }
            if(flag){
                return oldSubordinateMission;
            }
        }
        return null;
    }
}
