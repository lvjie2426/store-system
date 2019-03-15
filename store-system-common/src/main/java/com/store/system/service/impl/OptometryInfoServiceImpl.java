package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.store.system.client.ClientOptometryInfo;
import com.store.system.dao.OptometryInfoDao;
import com.store.system.dao.SubordinateDao;
import com.store.system.dao.UserDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.OptometryInfo;
import com.store.system.model.Subordinate;
import com.store.system.model.User;
import com.store.system.service.OptometryInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class OptometryInfoServiceImpl implements OptometryInfoService {

    private TransformMapUtils subMapUtils = new TransformMapUtils(Subordinate.class);

    private TransformMapUtils userMapUtils = new TransformMapUtils(User.class);

    @Resource
    private OptometryInfoDao optometryInfoDao;

    @Resource
    private SubordinateDao subordinateDao;

    @Resource
    private UserDao userDao;

    private void check(OptometryInfo optometryInfo) throws StoreSystemException {
        if(optometryInfo.getUid() == 0) throw new StoreSystemException("顾客ID为空");
        if(optometryInfo.getOptUid() == 0) throw new StoreSystemException("验光师ID为空");
        if(optometryInfo.getOptTime() == 0) throw new StoreSystemException("验光时间为空");
    }

    @Override
    public OptometryInfo add(OptometryInfo optometryInfo) throws Exception {
        check(optometryInfo);
        long subid = optometryInfo.getSubid();
        Subordinate subordinate = subordinateDao.load(subid);
        if(subordinate.getPid() == 0) throw new StoreSystemException("公司ID错误");
        optometryInfo.setpSubid(subordinate.getPid());
        optometryInfo = optometryInfoDao.insert(optometryInfo);
        return optometryInfo;
    }

    @Override
    public boolean update(OptometryInfo optometryInfo) throws Exception {
        check(optometryInfo);
        long subid = optometryInfo.getSubid();
        Subordinate subordinate = subordinateDao.load(subid);
        if(subordinate.getPid() == 0) throw new StoreSystemException("公司ID错误");
        optometryInfo.setpSubid(subordinate.getPid());
        boolean res = optometryInfoDao.update(optometryInfo);
        return res;
    }

    @Override
    public boolean del(long id) throws Exception {
        return optometryInfoDao.delete(id);
    }

    private List<ClientOptometryInfo> transformClients(List<OptometryInfo> list) throws Exception {
        Set<Long> sids = Sets.newHashSet();
        Set<Long> uids = Sets.newHashSet();
        for(OptometryInfo one : list) {
            if(one.getSubid() > 0) sids.add(one.getSubid());
            if(one.getpSubid() > 0) sids.add(one.getpSubid());
            uids.add(one.getUid());
            uids.add(one.getOptUid());
        }
        List<Subordinate> subordinates = subordinateDao.load(Lists.newArrayList(sids));
        Map<Long, Subordinate> subordinateMap = subMapUtils.listToMap(subordinates, "id");
        List<User> users = userDao.load(Lists.newArrayList(uids));
        Map<Long, User> userMap = userMapUtils.listToMap(users, "id");
        List<ClientOptometryInfo> res = Lists.newArrayList();
        for(OptometryInfo one : list) {
            ClientOptometryInfo clientOptometryInfo = new ClientOptometryInfo(one);
            if(one.getSubid() > 0) {
                Subordinate subordinate = subordinateMap.get(one.getSubid());
                if(subordinate != null) clientOptometryInfo.setSubName(subordinate.getName());
            }
            if(one.getpSubid() > 0) {
                Subordinate subordinate = subordinateMap.get(one.getpSubid());
                if(subordinate != null) clientOptometryInfo.setpSubName(subordinate.getName());
            }
            User customer = userMap.get(one.getUid());
            if(customer != null) clientOptometryInfo.setCustomerName(customer.getName());
            User user = userMap.get(one.getOptUid());
            if(user != null) clientOptometryInfo.setOptUserName(user.getName());
            res.add(clientOptometryInfo);
        }
        return res;
    }

    @Override
    public List<ClientOptometryInfo> getList(long uid, int size) throws Exception {
        List<OptometryInfo> list = optometryInfoDao.getList(uid, size);
        return transformClients(list);
    }
}
