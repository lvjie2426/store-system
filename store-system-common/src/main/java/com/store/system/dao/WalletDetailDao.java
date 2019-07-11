package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.WalletDetail;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface WalletDetailDao extends HDao<WalletDetail> {

    public List<WalletDetail> getPageList(long uid, int type, double cursor, int size) throws DataAccessException;

    public List<WalletDetail> getPageList(long uid, double cursor, int size) throws DataAccessException;

    public List<WalletDetail> getBetweenTimeList(long startTime, long endTime) throws DataAccessException;

}
