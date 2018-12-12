package com.store.system.service;

import com.s7.baseFramework.model.pagination.Pager;
import com.store.system.client.ClientProductSPU;
import com.store.system.model.ProductSKU;
import com.store.system.model.ProductSPU;

import java.util.List;

public interface ProductService {

    public ProductSPU addSPU(ProductSPU productSPU) throws Exception;

    public ProductSKU addSKU(ProductSKU productSKU) throws Exception;

    public void add(ProductSPU productSPU, List<ProductSKU> productSKUList) throws Exception;

    public ClientProductSPU loadSPU(long id) throws Exception;

    public boolean updateSPU(ProductSPU productSPU) throws Exception;

    public boolean updateSKU(ProductSKU productSKU) throws Exception;

    public void change(ProductSPU productSPU, List<ProductSKU> addProductSKUList, List<ProductSKU> updateProductSKUList,
                       List<Long> delSkuids) throws Exception;

    public boolean delSKU(long id) throws Exception;

    public boolean delSPU(long id) throws Exception;

    public Pager getBackPager(Pager pager, long subid, long cid, long pid, long bid, long sid) throws Exception;



    public ClientProductSPU selectSPU(int type, long subid, long pid, long cid, long bid, long sid) throws Exception;

}
