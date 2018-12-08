package com.store.system.service;

import com.store.system.model.ProductBrand;

import java.util.List;

public interface ProductBrandService {

    public ProductBrand add(ProductBrand productBrand) throws Exception;

    public boolean update(ProductBrand productBrand) throws Exception;

    public boolean del(long id) throws Exception;

    public List<ProductBrand> getAllList() throws Exception;

}
