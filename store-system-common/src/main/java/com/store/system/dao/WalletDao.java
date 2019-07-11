package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.Wallet;
import org.springframework.dao.DataAccessException;

public interface WalletDao extends HDao<Wallet> {

    public double getTotalMoney() throws DataAccessException;

}
