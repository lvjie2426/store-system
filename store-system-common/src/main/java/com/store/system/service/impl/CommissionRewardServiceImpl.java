package com.store.system.service.impl;

import com.store.system.dao.CommissionRewardDao;
import com.store.system.dao.SubordinateDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Commission;
import com.store.system.model.CommissionReward;
import com.store.system.model.Subordinate;
import com.store.system.model.User;
import com.store.system.service.CommissionRewardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public Integer getAllByUser(User user, long subid) throws Exception {
        List<CommissionReward> allByUser = commissionRewardDao.getAllByUser(user.getId(), subid, CommissionReward.type_reward);
        Integer price=0;
        for(CommissionReward commissionReward:allByUser){
            price+= commissionReward.getPrice();
        }
        return price;
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
