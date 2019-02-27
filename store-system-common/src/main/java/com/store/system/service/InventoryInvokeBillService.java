package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.model.InventoryInvokeBill;

public interface InventoryInvokeBillService {

    public InventoryInvokeBill add(InventoryInvokeBill inventoryInvokeBill) throws Exception;

    public boolean update(InventoryInvokeBill inventoryInvokeBill) throws Exception;

    public boolean del(long id) throws Exception;

    public boolean submit(long id) throws Exception;

    public void pass(long id, long checkUid, long outUid) throws Exception;

    public void noPass(long id, long checkUid) throws Exception;


    public Pager getCreatePager(Pager pager, long createUid) throws Exception;

    public Pager getCheckPager(Pager pager, long subid) throws Exception;

}
