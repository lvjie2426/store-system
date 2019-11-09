package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.InventoryCheckBill;

import java.util.List;

public interface InventoryCheckBillDao extends HDao<InventoryCheckBill> {

    List<InventoryCheckBill> getAll(long subid, double cursor, int size);

    List<InventoryCheckBill> getPageList(long subid, double cursor, int size);

}
