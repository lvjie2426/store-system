package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.store.system.dao.IntegralActivityDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.IntegralActivity;
import com.store.system.service.IntegralActivityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName IntegralActivityServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 14:10
 * @Version 1.0
 **/
@Service
public class IntegralActivityServiceImpl implements IntegralActivityService{

    @Resource
    private IntegralActivityDao integralActivityDao;

    private void check(IntegralActivity integralActivity) throws StoreSystemException {
        if (integralActivity.getSid() == 0) throw new StoreSystemException("公司ID不能为空");
        if (StringUtils.isBlank(integralActivity.getTitle())) throw new StoreSystemException("标题不能为空");
        if (integralActivity.getSkuIds().size() == 0) throw new StoreSystemException("活动商品IDs不能为空");
        if (integralActivity.getType() != IntegralActivity.TYPE_MONEY
                || integralActivity.getType() != IntegralActivity.TYPE_RATE )
            throw new StoreSystemException("赠送积分类型有误");
        if (integralActivity.getDescSubtract() == 0) throw new StoreSystemException("金额或折扣不能为空");
        if (integralActivity.getStartTime() == 0) throw new StoreSystemException("活动开始时间不能为空");
        if (integralActivity.getEndTime() == 0) throw new StoreSystemException("活动结束时间不能为空");
    }

    @Override
    public IntegralActivity add(IntegralActivity integralActivity) throws Exception {
        check(integralActivity);
        return integralActivityDao.insert(integralActivity);
    }

    @Override
    public boolean delete(long id) throws Exception {
        IntegralActivity integralActivity = integralActivityDao.load(id);
        integralActivity.setStatus(IntegralActivity.STATUS_DELETE);
        return integralActivityDao.update(integralActivity);
    }

    @Override
    public boolean update(IntegralActivity integralActivity) throws Exception {
        return integralActivityDao.update(integralActivity);
    }

    @Override
    public boolean updateStatus(long id, int status) throws Exception {
        IntegralActivity integralActivity = integralActivityDao.load(id);
        integralActivity.setStatus(status);
        return integralActivityDao.update(integralActivity);
    }

    @Override
    public List<IntegralActivity> getAllList(long psid) throws Exception {
        return integralActivityDao.getAllList(psid);
    }

    @Override
    public List<IntegralActivity> getIngList(long psid) throws Exception {
        List<IntegralActivity> res = Lists.newArrayList();
        List<IntegralActivity> list = integralActivityDao.getAllList(psid);
        long currentTime = System.currentTimeMillis();
        for (IntegralActivity one : list) {
            if (currentTime >= one.getStartTime() && currentTime <= one.getEndTime()) {
                res.add(one);
            }
        }
        return res;
    }

    @Override
    public List<IntegralActivity> getHistoryList(long psid) throws Exception {
        List<IntegralActivity> res = Lists.newArrayList();
        List<IntegralActivity> list = integralActivityDao.getAllList(psid);
        long currentTime = System.currentTimeMillis();
        for (IntegralActivity one : list) {
            if (currentTime > one.getEndTime()) {
                res.add(one);
            }
        }
        return res;
    }

}
