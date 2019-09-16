package com.store.system.service.impl;

import com.store.system.client.ClientChangeShift;
import com.store.system.dao.ChangeShiftDao;
import com.store.system.dao.UserDao;
import com.store.system.model.User;
import com.store.system.model.attendance.ChangeShift;
import com.store.system.service.ChangeShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-09-16 13:58
 **/

@Service
public class ChangeShiftServiceImpl implements ChangeShiftService {

    @Autowired
    private ChangeShiftDao changeShiftDao;
    @Autowired
    private UserDao  userDao;
    @Override
    public ChangeShift add(ChangeShift changeShift) throws Exception {
        return changeShiftDao.insert(changeShift);
    }

    @Override
    public ClientChangeShift load(long id) throws Exception {
        return TransFormCliens(changeShiftDao.load(id));
    }

    @Override
    public List<ClientChangeShift> getListByUid(long uid) throws Exception {
        List<ChangeShift> listByUid = changeShiftDao.getListByUid(uid);
        List<ClientChangeShift> clientChangeShifts=new ArrayList<>(listByUid.size());
        for(ChangeShift changeShift:listByUid){
            ClientChangeShift clientChangeShift=TransFormCliens(changeShift);
            clientChangeShifts.add(clientChangeShift);
        }
        return clientChangeShifts;
    }

    @Override
    public List<ClientChangeShift> getListByReplaceUid(long replaceUid) throws Exception {
        List<ChangeShift> listByUid = changeShiftDao.getListByReplaceUid(replaceUid);
        List<ClientChangeShift> clientChangeShifts=new ArrayList<>(listByUid.size());
        for(ChangeShift changeShift:listByUid){
            ClientChangeShift clientChangeShift=TransFormCliens(changeShift);
            clientChangeShifts.add(clientChangeShift);
        }
        return clientChangeShifts;
    }

    public ClientChangeShift TransFormCliens(ChangeShift changeShift){
        ClientChangeShift clientChangeShift=new ClientChangeShift(changeShift);
        User load = userDao.load(changeShift.getReplaceUid());
        if(load!=null){
            clientChangeShift.setReplaceName(load.getName());
        }
        User load1 = userDao.load(changeShift.getCopyUid());
        if(load1!=null){
            clientChangeShift.setCopyName(load1.getName());
        }
        return clientChangeShift;
    }

}
