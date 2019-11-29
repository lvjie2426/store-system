package com.store.system.service;

import com.store.system.client.ClientCoupon;
import com.store.system.model.Coupon;

import java.util.List;

/**
 * @ClassName CouponService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 15:32
 * @Version 1.0
 **/
public interface CouponService {

    Coupon add(Coupon coupon) throws Exception;

    boolean delete(long id) throws Exception;

    boolean update(Coupon coupon) throws Exception;

    boolean updateOpen(long id, int open) throws Exception;

    List<Coupon> getAllList(long psid) throws Exception;

    List<ClientCoupon> getIngList(long psid) throws Exception;

    List<ClientCoupon> getHistoryList(long psid) throws Exception;
}
