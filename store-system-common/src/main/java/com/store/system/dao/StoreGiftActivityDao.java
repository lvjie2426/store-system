package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.StoreGiftActivity;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName StoreGiftActivityDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/26 16:46
 * @Version 1.0
 **/
public interface StoreGiftActivityDao extends HDao<StoreGiftActivity> {

    List<StoreGiftActivity> getAllList(long psid) throws DataAccessException;
}
