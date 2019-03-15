package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.MarketingCoupon;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface MarketingCouponDao extends HDao<MarketingCoupon> {

    public List<MarketingCoupon> getAllList(long subid, int status) throws DataAccessException;

    public List<MarketingCoupon> getAllList(long subid, int status, int open) throws DataAccessException;

}
