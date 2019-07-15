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

	public List<ClientSubordinate> getTwoLevelAllList(long pid) throws Exception;

	public Pager getOneLevelPager(Pager pager) throws Exception;

	public Subordinate insertStore(Subordinate subordinate)throws Exception;

	public boolean updateStore(Subordinate subordinate)throws Exception;

    public Pager getSubordinateStoreByName(Pager pager,long sid, String name)throws Exception;

	public boolean updateStatus(long id, int status)throws Exception;

	public List<Subordinate> getAllSubordinate()throws Exception;

	public List<Subordinate> getAllParentSubordinate()throws Exception;

	public List<Subordinate> getAllList()throws Exception;
}
