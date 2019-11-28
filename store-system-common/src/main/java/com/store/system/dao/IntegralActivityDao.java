package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.IntegralActivity;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName IntegralActivityDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 14:04
 * @Version 1.0
 **/
public interface IntegralActivityDao extends HDao<IntegralActivity> {

    List<IntegralActivity> getAllList(long sid) throws DataAccessException;

}
