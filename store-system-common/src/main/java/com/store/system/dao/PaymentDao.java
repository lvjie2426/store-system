package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.Payment;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName PaymentDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/5/17 14:21
 * @Version 1.0
 **/
public interface PaymentDao extends HDao<Payment> {

    public List<Payment> getUsedList(long psid, int payType, int type) throws DataAccessException;
}
