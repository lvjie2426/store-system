package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.PayPassport;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface PayPassportDao extends HDao<PayPassport> {

    public List<PayPassport> getAllList(int status) throws DataAccessException;

    public List<PayPassport> getAllList(long subId, int status) throws DataAccessException;
}
