package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.model.MarketingTimingSms;

import java.util.List;

public interface MarketingTimingSmsService {

    public MarketingTimingSms add(MarketingTimingSms marketingTimingSms) throws Exception;

    public boolean update(MarketingTimingSms marketingTimingSms) throws Exception;

    public boolean del(long id) throws Exception;

    public Pager getBackPager(Pager pager, long subid) throws Exception;

    public List<MarketingTimingSms> getNoSendList(int size) throws Exception;

    public void sendSMS(MarketingTimingSms marketingTimingSms) throws Exception;

    public boolean updateById(MarketingTimingSms marketingTimingSms) throws Exception;
}
