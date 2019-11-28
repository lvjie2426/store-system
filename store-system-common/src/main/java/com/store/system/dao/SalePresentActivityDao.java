package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.SalePresentActivity;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName SalePresentActivityDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 11:11
 * @Version 1.0
 **/
public interface SalePresentActivityDao extends HDao<SalePresentActivity>{

    List<SalePresentActivity> getAllList(long psid) throws DataAccessException;
}
