package com.store.system.service;

import com.store.system.model.ProductBrand;
import com.store.system.model.ProductBrandPool;
import com.store.system.model.ProductCategory;
import com.store.system.model.ProductCategoryPool;

import java.util.List;

public interface ProductCategoryService {

    public ProductCategory add(ProductCategory productCategory) throws Exception;

    public boolean update(ProductCategory productCategory) throws Exception;

    public boolean del(long id) throws Exception;

    public List<ProductCategory> getAllList() throws Exception;




    public boolean addPool(ProductCategoryPool pool) throws Exception;

    public boolean delPool(ProductCategoryPool pool) throws Exception;

    public List<ProductCategory> getSubAllList(long subid) throws Exception;

}
