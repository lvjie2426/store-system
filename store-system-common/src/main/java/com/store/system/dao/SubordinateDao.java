package com.store.system.dao;

import com.store.system.model.Subordinate;
import com.quakoo.space.interfaces.HDao;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface SubordinateDao extends HDao<Subordinate>{

	public List<Subordinate> getAllList(int status) throws DataAccessException;

	public List<Subordinate> getAllList(long pid, int status) throws DataAccessException;

	public List<Subordinate> getAllList(long pid, int status,double cursor, int size) throws DataAccessException;

}
