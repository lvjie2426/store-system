package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.model.MarketingShotMsg;

public interface MarketingShotMsgService {

    public MarketingShotMsg add(MarketingShotMsg marketingShotMsg) throws Exception;

    public boolean update(MarketingShotMsg marketingShotMsg) throws Exception;

    public boolean del(long id) throws Exception;

    public Pager getBackPager(Pager pager, long subid) throws Exception;

}
