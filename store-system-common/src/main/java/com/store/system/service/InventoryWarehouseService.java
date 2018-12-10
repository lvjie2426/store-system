package com.store.system.service;

import com.store.system.client.ClientInventoryWarehouse;
import com.store.system.model.InventoryWarehouse;

import java.util.List;

public interface InventoryWarehouseService {

    public InventoryWarehouse add(InventoryWarehouse inventoryWarehouse) throws Exception;

    public boolean update(InventoryWarehouse inventoryWarehouse) throws Exception;

    public boolean del(long id) throws Exception;

    public List<ClientInventoryWarehouse> getAllList(long subid) throws Exception;

}
