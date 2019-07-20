package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.Commission;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName CommissionDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/13 19:24
 * @Version 1.0
 **/
public interface CommissionDao extends HDao<Commission> {

    public List<Commission> getAllList(long subId)throws Exception;

}
