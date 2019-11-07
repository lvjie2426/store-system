package com.store.system.service;

import com.store.system.model.CommissionReward;
import com.store.system.model.User;

/**
 * @ProjectName: store-system
 * @Author: LiHaoJie
 * @Description: 任务提成奖励记录
 * @Date: 2019/7/18 10:48
 * @Version: 1.0
 */
public interface CommissionRewardService {

    /**
     * 添加记录
     * @return
     * @throws Exception
     */
    public CommissionReward add(CommissionReward commissionReward)throws Exception;


    Integer getAllByUser(User user, long subid)throws Exception;
}
