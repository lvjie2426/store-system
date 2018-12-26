package com.store.system.service;

import com.store.system.client.ClientSubordinate;
import com.store.system.model.Subordinate;
import com.quakoo.baseFramework.model.pagination.Pager;

import java.util.List;

public interface SubordinateService {

	public List<ClientSubordinate> load(List<Long> ids) throws Exception;

	public ClientSubordinate load(long sid) throws Exception;

	public Subordinate insert(Subordinate subordinate) throws Exception;

	public boolean update(Subordinate subordinate) throws Exception;

	public boolean delete(long id) throws Exception;

	public List<ClientSubordinate> getAll() throws Exception;

	public Pager getBackPage(Pager pager, String name) throws Exception;

	public List<ClientSubordinate> exportSubordinate(String name) throws Exception;

	public List<Subordinate> getIdByName(String name) throws Exception;

	public List<Subordinate> getAllList() throws Exception;




}
