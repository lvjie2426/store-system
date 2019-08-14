package com.store.system.service.impl;

import com.store.system.dao.MissionDao;
import com.store.system.dao.UserLogDao;
import com.store.system.dao.UserMissionPoolDao;
import com.store.system.dao.impl.UserLogDaoImpl;
import com.store.system.model.Mission;
import com.store.system.model.UserLog;
import com.store.system.model.UserMissionPool;
import com.store.system.service.UserLogService;
import com.store.system.service.UserMissionPoolService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserLogServiceImpl implements UserLogService{

@Resource
private UserLogDao  userLogDao;

    @Override
    public UserLog add(UserLog userLog) throws Exception {
        Boolean update=true;
        UserLog info=new UserLog();
        info.setUid(userLog.getUid());
        info.setSubid(userLog.getSubid());
        UserLog dbUserLog=userLogDao.load(info);
        if(dbUserLog==null){
            update=false;
            dbUserLog=info;
        }
        dbUserLog.setDesc(userLog.getDesc());
        dbUserLog.setEducation(userLog.getEducation());
        dbUserLog.setExaminationDate(userLog.getExaminationDate());
        dbUserLog.setExaminationOrder(userLog.getExaminationOrder());
        dbUserLog.setLecturer(userLog.getLecturer());
        dbUserLog.setTests(userLog.getTests());
        dbUserLog.setTrainContent(userLog.getTrainContent());
        dbUserLog.setTrainForm(userLog.getTrainForm());
        dbUserLog.setTrainUnit(userLog.getTrainUnit());
        dbUserLog.setType(userLog.getType());
        dbUserLog.setWorkYear(userLog.getWorkYear());
        if(update){
            userLogDao.update(dbUserLog);
        }else{
            userLogDao.insert(dbUserLog);
        }
        return dbUserLog;
    }

    @Override
    public List<UserLog> getInfoByUid(long uid,int type) throws Exception {

        return  userLogDao.getAllByUid(uid,type);
    }
}
