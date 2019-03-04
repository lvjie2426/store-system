package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.model.Customer;

public interface CustomerService {

    public Customer add(Customer customer) throws Exception;

    public boolean update(Customer customer) throws Exception;

    public boolean del(long id) throws Exception;


    public Pager getBackPager(Pager pager, long pSubid) throws Exception;

    public Pager getBackSubPager(Pager pager, long subid) throws Exception;

}
