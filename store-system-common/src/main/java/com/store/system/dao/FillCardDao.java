package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.attendance.FillCard;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName FillCardDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/17 18:57
 * @Version 1.0
 **/
public interface FillCardDao extends HDao<FillCard> {

    public List<FillCard> getPager(long id, double v, int size)throws DataAccessException;

}
