package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.store.system.dao.CouponDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Coupon;
import com.store.system.service.CouponService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName CouponServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 15:32
 * @Version 1.0
 **/
@Service
public class CouponServiceImpl implements CouponService{

    @Resource
    private CouponDao couponDao;

    private void check(Coupon coupon) throws StoreSystemException {
        if (coupon.getPsid() == 0) throw new StoreSystemException("公司ID不能为空");
        if (StringUtils.isBlank(coupon.getTitle())) throw new StoreSystemException("标题不能为空");
        if (coupon.getDescFull() == 0) throw new StoreSystemException("使用条件消费满不能为空");
        if (coupon.getDescSubtract() == 0) throw new StoreSystemException("金额或折扣不能为空");
        if (coupon.getSkuIds().size() == 0) throw new StoreSystemException("使用商品IDs不能为空");
        if (coupon.getType() != Coupon.TYPE_FULL
                && coupon.getType() != Coupon.TYPE_DIRECTIONAL
                && coupon.getType() != Coupon.TYPE_DISCOUNT ) {
            throw new StoreSystemException("赠送积分类型有误");
        }
        if (coupon.getStartTime() == 0) throw new StoreSystemException("活动开始时间不能为空");
        if (coupon.getEndTime() == 0) throw new StoreSystemException("活动结束时间不能为空");
    }

    @Override
    public Coupon add(Coupon coupon) throws Exception {
        check(coupon);
        return couponDao.insert(coupon);
    }

    @Override
    public boolean delete(long id) throws Exception {
        Coupon integralActivity = couponDao.load(id);
        integralActivity.setStatus(Coupon.STATUS_DELETE);
        return couponDao.update(integralActivity);
    }

    @Override
    public boolean update(Coupon coupon) throws Exception {
        return couponDao.update(coupon);
    }

    @Override
    public boolean updateStatus(long id, int status) throws Exception {
        Coupon integralActivity = couponDao.load(id);
        integralActivity.setStatus(status);
        return couponDao.update(integralActivity);
    }

    @Override
    public List<Coupon> getAllList(long psid) throws Exception {
        return couponDao.getAllList(psid);
    }

    @Override
    public List<Coupon> getIngList(long psid) throws Exception {
        List<Coupon> res = Lists.newArrayList();
        List<Coupon> list = couponDao.getAllList(psid);
        long currentTime = System.currentTimeMillis();
        for (Coupon one : list) {
            if (currentTime >= one.getStartTime() && currentTime <= one.getEndTime()) {
                res.add(one);
            }
        }
        return res;
    }

    @Override
    public List<Coupon> getHistoryList(long psid) throws Exception {
        List<Coupon> res = Lists.newArrayList();
        List<Coupon> list = couponDao.getAllList(psid);
        long currentTime = System.currentTimeMillis();
        for (Coupon one : list) {
            if (currentTime > one.getEndTime()) {
                res.add(one);
            }
        }
        return res;
    }
}
