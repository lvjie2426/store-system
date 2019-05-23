package com.store.system.service.impl;

import com.quakoo.ext.RowMapperHelp;
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
    @Override
    public SubordinateMissionPool load(long mid,long sid) throws Exception {
        return subordinateMissionPoolDao.load(mid,sid);
    }

    @Override
    public SubordinateMissionPool add(SubordinateMissionPool subordinateMissionPool) throws Exception {
        SubordinateMissionPool oldSubordinateMission = subordinateMissionPoolDao.load(subordinateMissionPool);
        if(subordinateMissionPool==null){
            return subordinateMissionPoolDao.insert(subordinateMissionPool);
        }else{
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
            if(flag){
                return oldSubordinateMission;
            }
            return null;
        }
    }
}
