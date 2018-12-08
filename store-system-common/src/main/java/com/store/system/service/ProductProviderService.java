package com.store.system.service;

import com.store.system.model.ProductProvider;

import java.util.List;

public interface ProductProviderService {

    public ProductProvider add(ProductProvider productProvider) throws Exception;

    public boolean update(ProductProvider productProvider) throws Exception;

    public boolean del(long id) throws Exception;

    public List<ProductProvider> getAllList() throws Exception;

}
