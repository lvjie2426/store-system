package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.model.InventoryInBill;


public interface InventoryInBillService {

    public InventoryInBill add(InventoryInBill inventoryInBill) throws Exception;

    public boolean update(InventoryInBill inventoryInBill) throws Exception;

    public boolean del(long id) throws Exception;

    public boolean submit(long id) throws Exception;

    public void pass(long id, long checkUid) throws Exception;

    public void noPass(long id, long checkUid) throws Exception;



    public Pager getCreatePager(Pager pager, long createUid, long startTime, long endTime, int type) throws Exception;

    public Pager getCheckPager(Pager pager, long subid, long startTime, long endTime, int type) throws Exception;

    public Pager getCreateWebPager(Pager pager, long createUid) throws Exception;

    public Pager getCheckWebPager(Pager pager, long subid) throws Exception;

}
