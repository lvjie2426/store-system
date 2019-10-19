package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.model.InventoryOutBill;

public interface InventoryOutBillService {

    public InventoryOutBill add(InventoryOutBill inventoryOutBill) throws Exception;

    public boolean update(InventoryOutBill inventoryOutBill) throws Exception;

    public boolean del(long id) throws Exception;

    public boolean submit(long id) throws Exception;

    public void pass(long id, long checkUid) throws Exception;

    public void noPass(long id, long checkUid) throws Exception;


    public Pager getCreatePager(Pager pager, long createUid) throws Exception;

    public Pager getCheckPager(Pager pager, long subid) throws Exception;

    public Pager getAllPager(Pager pager, long subid, int type)throws Exception;
}
