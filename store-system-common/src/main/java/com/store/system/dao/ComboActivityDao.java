package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.ComboActivity;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName ComboActivityDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 14:29
 * @Version 1.0
 **/
public interface ComboActivityDao extends HDao<ComboActivity> {

    List<ComboActivity> getAllList(long psid) throws DataAccessException;
}
