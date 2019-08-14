package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.client.ClientSubordinate;
import com.store.system.client.ResultClient;
import com.store.system.model.Company;
import com.store.system.model.Subordinate;

import java.util.List;

public interface CompanyService {


    public ResultClient insert(Company company) throws Exception;

    public ResultClient update(Company company) throws Exception;

    public Pager getAll(Pager pager)throws Exception;

    public Pager getOverdue(Pager pager)throws Exception;

    public List<Company> getMp(int mpId)throws Exception;

    public boolean checkStatus(List<Long> ids)throws Exception;
}
