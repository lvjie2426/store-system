package com.store.system.service;

import com.store.system.model.ProductPropertyValue;
import com.store.system.model.ProductPropertyValuePool;

import java.util.List;

public interface ProductPropertyValueService {

    public ProductPropertyValue add(ProductPropertyValue productPropertyValue) throws Exception;

    public boolean update(ProductPropertyValue productPropertyValue) throws Exception;

    public boolean del(long id) throws Exception;

    public ProductPropertyValue load(long id) throws Exception;

    public List<ProductPropertyValue> getAllList(long pnid) throws Exception;





    public boolean addPool(ProductPropertyValuePool pool) throws Exception;

    public boolean delPool(ProductPropertyValuePool pool) throws Exception;

    public List<ProductPropertyValue> getSubAllList(long subid, long pnid) throws Exception;

}
