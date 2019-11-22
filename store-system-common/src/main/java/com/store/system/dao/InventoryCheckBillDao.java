package com.store.system.dao;

import com.quakoo.space.interfaces.HDao;
import com.store.system.model.InventoryCheckBill;

import java.util.List;

public interface InventoryCheckBillDao extends HDao<InventoryCheckBill> {

    List<InventoryCheckBill> getCreatePageList(long createUid, int status, double cursor, int size);

    List<InventoryCheckBill> getCreatePageList(long createUid, double cursor, int size);

    List<InventoryCheckBill> getPageList(long subid, int status, double cursor, int size);

    List<InventoryCheckBill> getPageList(long subid, double cursor, int size);

    List<InventoryCheckBill> getAllList(long subid, int status);

    List<InventoryCheckBill> getCreateList(long createUid, int status);
}
