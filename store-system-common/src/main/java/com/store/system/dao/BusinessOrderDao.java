package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.BusinessOrder;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @ClassName BusinessBusinessOrderDao
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/7/23 17:01
 * @Version 1.0
 **/
public interface BusinessOrderDao extends HDao<BusinessOrder> {

    public List<BusinessOrder> getAllList(long subId, int status, int makeStatus) throws DataAccessException;

    public List<BusinessOrder> getAllList(long subId, int makeStatus) throws DataAccessException;

    public List<BusinessOrder> getUserList(long uid, int status, int makeStatus) throws DataAccessException;

    public List<BusinessOrder> getUserList(long staffId, int status, int makeStatus, double cursor, int size) throws DataAccessException;

    public List<BusinessOrder> getPageList(long subId, int status, int makeStatus, long day, double cursor, int size) throws DataAccessException;

    public List<BusinessOrder> getPageList(long subId, int status, int makeStatus, double cursor, int size) throws DataAccessException;

    public int getUserCount(long staffId, int status, int makeStatus) throws DataAccessException;

    public int getCount(long subId, int status, int makeStatus) throws DataAccessException;

    public int getCount(long subId, int status, int makeStatus, long day) throws DataAccessException;
}
