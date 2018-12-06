package com.store.system.dao;

import com.store.system.model.Subordinate;
import com.s7.space.interfaces.HDao;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface SubordinateDao extends HDao<Subordinate>{
	
	public List<Subordinate> getAllList(int status) throws DataAccessException;

	public List<Subordinate> getAllSubordinateByProvince(long province, long city, int status) throws DataAccessException;

	public List<Subordinate> getIdByName(String name) throws DataAccessException;




}
