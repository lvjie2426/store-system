package com.store.system.service;

import com.store.system.model.ProductBrand;
import com.store.system.model.ProductBrandPool;
import com.store.system.model.ProductProvider;
import com.store.system.model.ProductProviderPool;

import java.util.List;

public interface ProductProviderService {

    public ProductProvider add(ProductProvider productProvider) throws Exception;

    public boolean update(ProductProvider productProvider) throws Exception;

    public boolean del(long id) throws Exception;

    public List<ProductProvider> getAllList() throws Exception;





    public boolean addPool(ProductProviderPool pool) throws Exception;

    public boolean delPool(ProductProviderPool pool) throws Exception;

    public List<ProductProvider> getSubAllList(long subid) throws Exception;

}
