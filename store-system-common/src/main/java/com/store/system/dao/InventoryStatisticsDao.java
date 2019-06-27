package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.InventoryStatistics;
import com.store.system.model.SaleCategoryStatistics;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName InventoryStatisticsDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/27 15:50
 * @Version 1.0
 **/
public interface InventoryStatisticsDao extends HDao<InventoryStatistics>{

    public List<InventoryStatistics> getSubList(long subId, long day) throws DataAccessException;
}
