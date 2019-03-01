package com.store.system.service;

import com.quakoo.space.annotation.domain.SortKey;
import com.store.system.model.ProductBrand;
import com.store.system.model.ProductBrandPool;

import java.util.List;

public interface ProductBrandService {

    public ProductBrand add(ProductBrand productBrand) throws Exception;

    public boolean update(ProductBrand productBrand) throws Exception;

    public boolean del(long id) throws Exception;

    public List<ProductBrand> getAllList() throws Exception;





    public boolean addPool(ProductBrandPool pool) throws Exception;

    public boolean delPool(ProductBrandPool pool) throws Exception;

    public List<ProductBrand> getSubAllList(long subid) throws Exception;

}
