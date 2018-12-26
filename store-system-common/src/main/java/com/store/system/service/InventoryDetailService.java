package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.client.ClientInventoryDetail;

import java.util.List;

public interface InventoryDetailService {

    public Pager getPager(Pager pager, long wid, long cid) throws Exception;

    public List<ClientInventoryDetail> getAllList(long wid, long p_spuid) throws Exception;

}
