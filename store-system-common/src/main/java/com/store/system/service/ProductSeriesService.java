package com.store.system.service;

import com.store.system.model.ProductBrand;
import com.store.system.model.ProductBrandPool;
import com.store.system.model.ProductSeries;
import com.store.system.model.ProductSeriesPool;

import java.util.List;

public interface ProductSeriesService {

    public ProductSeries add(ProductSeries productSeries) throws Exception;

    public boolean update(ProductSeries productSeries) throws Exception;

    public boolean del(long id) throws Exception;

    public ProductSeries load(long id) throws Exception;

    public List<ProductSeries> getAllList(long bid) throws Exception;




    public boolean addPool(ProductSeriesPool pool) throws Exception;

    public boolean delPool(ProductSeriesPool pool) throws Exception;

    public List<ProductSeries> getSubAllList(long subid, long bid) throws Exception;

}
