package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.PayInfo;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName PayInfoDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/7/23 17:02
 * @Version 1.0
 **/
public interface PayInfoDao extends HDao<PayInfo>{

    public List<PayInfo> getAllList(long boId) throws DataAccessException;
}
