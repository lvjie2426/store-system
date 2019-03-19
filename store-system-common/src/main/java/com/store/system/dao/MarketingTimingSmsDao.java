package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.MarketingTimingSms;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface MarketingTimingSmsDao extends HDao<MarketingTimingSms> {

    public List<MarketingTimingSms> getList(int status, int send, int size) throws DataAccessException;

}
