package com.store.system.service;

import com.store.system.model.MarketingCoupon;

import java.util.List;

public interface MarketingCouponService {

    public MarketingCoupon add(MarketingCoupon marketingCoupon) throws Exception;

    public boolean updateOpen(long id, int open) throws Exception;

    public boolean updateSort(long id, long sort) throws Exception;

    public boolean del(long id) throws Exception;

    public List<MarketingCoupon> getAllList(long subid) throws Exception;

    public List<MarketingCoupon> getCanUseList(long subid, int money, int num, long time) throws Exception; //促销方式选择



    public int calculateMoney(long mcid, int num, int money) throws Exception; //计算优惠券优惠的金额

}
