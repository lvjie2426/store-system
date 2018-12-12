package com.store.system.service;

import com.s7.baseFramework.model.pagination.Pager;

public interface InventoryDetailService {

    public Pager getPager(Pager pager, long wid, long cid) throws Exception;

}
