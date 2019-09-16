package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.store.system.client.ClientLeave;
import com.store.system.dao.LeaveDao;
import com.store.system.dao.UserDao;
import com.store.system.dao.UserLeavePoolDao;
import com.store.system.model.User;
import com.store.system.model.attendance.Leave;
import com.store.system.model.attendance.UserLeavePool;
import com.store.system.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-09-16 14:53
 **/
@Service
public class LeaveServiceImpl implements LeaveService {

    private TransformFieldSetUtils UserLeave = new TransformFieldSetUtils(UserLeavePool.class);

    @Autowired
    private LeaveDao leaveDao;
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserLeavePoolDao userLeavePoolDao;
    @Override
    public Leave add(Leave leave) throws Exception {
        // todo ApprovalLog表需要记录一条数据
        return leaveDao.insert(leave);
    }

    @Override
    public ClientLeave load(long id) throws Exception {
        return  transformClients(leaveDao.load(id));
    }

    @Override
    public List<ClientLeave> getListByUid(long uid) throws Exception {
        List<UserLeavePool> allList = userLeavePoolDao.getAllList(uid);
        Set<Long> lid = UserLeave.fieldList(allList, "lid");
        List<Leave> load = leaveDao.load(Lists.newArrayList(lid));
        List<ClientLeave> clientLeaveList=new ArrayList<>(load.size());
        for(Leave leave:load){
            ClientLeave clientLeave=  transformClients(leave);
            clientLeaveList.add(clientLeave);
        }
        return clientLeaveList;
    }

    public ClientLeave transformClients(Leave leave){
        ClientLeave clientLeave=new ClientLeave(leave);
        User load = userDao.load(leave.getCheckUid());
        if(load!=null){
            clientLeave.setCheckName(load.getName());
        }
        return clientLeave;
    }
}
