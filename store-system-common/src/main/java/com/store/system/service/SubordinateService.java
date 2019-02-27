package com.store.system.service;

import com.store.system.client.ClientSubordinate;
import com.store.system.model.Subordinate;
import com.quakoo.baseFramework.model.pagination.Pager;

import java.util.List;

public interface SubordinateService {

	public ClientSubordinate load(long sid) throws Exception;

	public Subordinate insert(Subordinate subordinate) throws Exception;

	public boolean update(Subordinate subordinate) throws Exception;

	public boolean delete(long id) throws Exception;

	public Pager getBackPage(Pager pager, String name) throws Exception;

}
