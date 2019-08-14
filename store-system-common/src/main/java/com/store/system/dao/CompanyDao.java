package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.Company;
import com.store.system.model.Subordinate;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface CompanyDao extends HDao<Company>{


	public List<Company> getAll()throws Exception;

	public int getCount()throws Exception;
}
