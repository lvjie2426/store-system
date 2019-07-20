package com.store.system.service.impl;

import com.store.system.dao.CommissionRewardDao;
import com.store.system.dao.SubordinateDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.CommissionReward;
import com.store.system.model.Subordinate;
import com.store.system.service.CommissionRewardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ProjectName: store-system
 * @Author: LiHaoJie
 * @Description: 任务提成奖励记录
 * @Date: 2019/7/18 10:49
 * @Version: 1.0
 */
@Service
public class CommissionRewardServiceImpl implements CommissionRewardService {

    @Resource
    private SubordinateDao subordinateDao;
    @Resource
    private CommissionRewardDao commissionRewardDao;

    @Override
    public CommissionReward add(CommissionReward commissionReward) throws Exception {
        check(commissionReward);
        return commissionRewardDao.insert(commissionReward);
    }

    private void check(CommissionReward commissionReward) {
        if(commissionReward.getUid() == 0){
            throw new StoreSystemException("用户ID不能为空!");
        }
        if(commissionReward.getSid() == 0){
            throw new StoreSystemException("门店ID不能为空!");
        }
        Subordinate subordinate = subordinateDao.load(commissionReward.getSid());
        if(subordinate.getPid()==0){
            throw new StoreSystemException("门店ID错误!");
        }
    }
}
