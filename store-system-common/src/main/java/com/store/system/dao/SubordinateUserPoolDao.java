package com.store.system.dao;

import com.store.system.model.SubordinateUserPool;
import com.quakoo.space.interfaces.HDao;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface SubordinateUserPoolDao extends HDao<SubordinateUserPool> {

	public List<SubordinateUserPool> getAllList(long sid, int status) throws DataAccessException;


	public int getCount(long sid, int status) throws DataAccessException;


	public List<SubordinateUserPool> getPageList(long sid, int status, double cursor, int size) throws DataAccessException;


}
