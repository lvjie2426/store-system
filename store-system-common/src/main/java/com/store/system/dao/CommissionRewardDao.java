package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.CommissionReward;

import java.util.List;

/**
 * @ProjectName: store-system
 * @Author: LiHaoJie
 * @Description: 任务提成奖励记录
 * @Date: 2019/7/18 10:46
 * @Version: 1.0
 */
public interface CommissionRewardDao extends HDao<CommissionReward> {
    List<CommissionReward> getAllByUser(long id, long subid, int type_reward)throws  Exception;
}
