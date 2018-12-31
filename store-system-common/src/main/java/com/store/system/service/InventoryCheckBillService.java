package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.model.InventoryCheckBill;

public interface InventoryCheckBillService {

    public InventoryCheckBill add(InventoryCheckBill inventoryCheckBill) throws Exception;

    public boolean update(InventoryCheckBill inventoryCheckBill) throws Exception;

    public boolean del(long id) throws Exception;

    public boolean submitCheck(long id) throws Exception; //提交盘点

    public boolean saveCheck(InventoryCheckBill inventoryCheckBill) throws Exception; //盘点

    public boolean endCheck(long id, long checkUid) throws Exception;



    public Pager getCreatePager(Pager pager, long createUid) throws Exception;

    public Pager getCheckPager(Pager pager) throws Exception;

}
