package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.StatisticsCustomerJob;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ProjectName: store-system
 * @Author: LiHaoJie
 * @Description:
 * @Date: 2019/6/15 16:16
 * @Version: 1.0
 */
public interface StatisticsCustomerJobDao extends HDao<StatisticsCustomerJob> {

    public List<StatisticsCustomerJob> getDayList(long subid,int day)throws DataAccessException;

    public List<StatisticsCustomerJob> getWeekList(long subid,int week)throws DataAccessException;

    public List<StatisticsCustomerJob> getList(long subid)throws DataAccessException;

}
