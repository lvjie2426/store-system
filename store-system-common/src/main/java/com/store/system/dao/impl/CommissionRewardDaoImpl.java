package com.store.system.dao.impl;

import com.quakoo.space.CacheBaseDao;
import com.quakoo.space.annotation.dao.HyperspaceDao;
import com.quakoo.space.enums.HyperspaceType;
import com.store.system.dao.CommissionRewardDao;
import com.store.system.model.CommissionReward;

import java.util.List;
import java.util.Map;

/**
 * @ProjectName: store-system
 * @Author: LiHaoJie
 * @Description: 任务提成奖励记录
 * @Date: 2019/7/18 10:46
 * @Version: 1.0
 */
@HyperspaceDao(type = HyperspaceType.cache)
public class CommissionRewardDaoImpl extends CacheBaseDao<CommissionReward> implements CommissionRewardDao{
    @Override
    public Map<String, List<String>> getCacheMap() {
        return null;
    }
}
