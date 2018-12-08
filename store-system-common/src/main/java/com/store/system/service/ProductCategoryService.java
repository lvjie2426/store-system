package com.store.system.service;

import com.store.system.model.ProductCategory;

import java.util.List;

public interface ProductCategoryService {

    public ProductCategory add(ProductCategory productCategory) throws Exception;

    public boolean update(ProductCategory productCategory) throws Exception;

    public boolean del(long id) throws Exception;

    public List<ProductCategory> getAllList() throws Exception;

}
