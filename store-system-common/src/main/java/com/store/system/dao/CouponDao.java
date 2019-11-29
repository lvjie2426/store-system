package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.Coupon;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName CouponDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 15:27
 * @Version 1.0
 **/
public interface CouponDao extends HDao<Coupon> {

    List<Coupon> getAllList(long psid, int status, int open) throws DataAccessException;
}
