package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.store.system.dao.StoreGiftActivityDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.StoreGiftActivity;
import com.store.system.service.StoreGiftActivityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName StoreGiftActivityServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/26 16:50
 * @Version 1.0
 **/
@Service
public class StoreGiftActivityServiceImpl implements StoreGiftActivityService {

    @Resource
    private StoreGiftActivityDao storeGiftActivityDao;

    private void check(StoreGiftActivity storeGiftActivity) throws StoreSystemException {
        if (storeGiftActivity.getPsid() == 0) throw new StoreSystemException("公司ID不能为空");
        if (StringUtils.isBlank(storeGiftActivity.getTitle())) throw new StoreSystemException("标题不能为空");
        if (storeGiftActivity.getStorePrice() == 0) throw new StoreSystemException("一次性储值金额不能为空");
        if (storeGiftActivity.getCouponId() == 0) throw new StoreSystemException("优惠券ID不能为空");
        if (storeGiftActivity.getStartTime() == 0) throw new StoreSystemException("活动开始时间不能为空");
        if (storeGiftActivity.getEndTime() == 0) throw new StoreSystemException("活动结束时间不能为空");
    }

    @Override
    public StoreGiftActivity add(StoreGiftActivity storeGiftActivity) throws Exception {
        check(storeGiftActivity);
        return storeGiftActivityDao.insert(storeGiftActivity);
    }

    @Override
    public boolean delete(long id) throws Exception {
        return storeGiftActivityDao.delete(id);
    }

    @Override
    public boolean update(StoreGiftActivity storeGiftActivity) throws Exception {
        return storeGiftActivityDao.update(storeGiftActivity);
    }

    @Override
    public boolean updateStatus(long id, int status) throws Exception {
        StoreGiftActivity storeGiftActivity = storeGiftActivityDao.load(id);
        storeGiftActivity.setStatus(status);
        return storeGiftActivityDao.update(storeGiftActivity);
    }

    @Override
    public List<StoreGiftActivity> getAllList(long psid) throws Exception {
        return storeGiftActivityDao.getAllList(psid);
    }

    @Override
    public List<StoreGiftActivity> getIngList(long psid) throws Exception {
        List<StoreGiftActivity> res = Lists.newArrayList();
        List<StoreGiftActivity> list = storeGiftActivityDao.getAllList(psid);
        long currentTime = System.currentTimeMillis();
        for (StoreGiftActivity one : list) {
            if (currentTime >= one.getStartTime() && currentTime <= one.getEndTime()) {
                res.add(one);
            }
        }
        return res;
    }

    @Override
    public List<StoreGiftActivity> getHistoryList(long psid) throws Exception {
        List<StoreGiftActivity> res = Lists.newArrayList();
        List<StoreGiftActivity> list = storeGiftActivityDao.getAllList(psid);
        long currentTime = System.currentTimeMillis();
        for (StoreGiftActivity one : list) {
            if (currentTime > one.getEndTime()) {
                res.add(one);
            }
        }
        return res;
    }
}
