package com.store.system.service.impl;

import com.store.system.client.ClientWorkOverTime;
import com.store.system.dao.UserDao;
import com.store.system.dao.WorkOverTimeDao;
import com.store.system.model.User;
import com.store.system.model.attendance.WorkOverTime;
import com.store.system.service.WorkOverTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-09-16 10:50
 **/
@Service
public class WorkOverTimeServiceImpl implements WorkOverTimeService {

    @Autowired
    private WorkOverTimeDao workOverTimeDao;
    @Autowired
    private UserDao userDao;


    @Override
    public WorkOverTime add(WorkOverTime workOverTime) throws Exception {
        return workOverTimeDao.insert(workOverTime);
    }

    @Override
    public ClientWorkOverTime load(long id) throws Exception {
        return  transformClients(workOverTimeDao.load(id));
    }

    @Override
    public List<ClientWorkOverTime> getListByUid(long askUid) throws Exception {
        List<WorkOverTime> listByUid = workOverTimeDao.getListByUid(askUid);
        List<ClientWorkOverTime> clientWorkOverTimeList=new ArrayList<>(listByUid.size());
        for(WorkOverTime workOverTime:listByUid){
            ClientWorkOverTime clientWorkOverTime=transformClients(workOverTime);
            clientWorkOverTimeList.add(clientWorkOverTime);
        }
        return clientWorkOverTimeList;
    }

    public ClientWorkOverTime transformClients(WorkOverTime workOverTime){
        ClientWorkOverTime clientWorkOverTime=new ClientWorkOverTime(workOverTime);
        User load = userDao.load(workOverTime.getCheckUid());
        if(load!=null){
            clientWorkOverTime.setCheckName(load.getName());

        }
        User load1 = userDao.load(workOverTime.getCopyUid());
        if(load1!=null){
            clientWorkOverTime.setCopyName(load1.getName());
        }
        return clientWorkOverTime;
    }
}
