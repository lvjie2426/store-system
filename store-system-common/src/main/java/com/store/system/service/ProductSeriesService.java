package com.store.system.service;

import com.store.system.model.ProductSeries;

import java.util.List;

public interface ProductSeriesService {

    public ProductSeries add(ProductSeries productSeries) throws Exception;

    public boolean update(ProductSeries productSeries) throws Exception;

    public boolean del(long id) throws Exception;

    public List<ProductSeries> getAllList(long bid) throws Exception;

}
