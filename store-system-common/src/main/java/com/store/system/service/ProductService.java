package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.client.ClientProductSKU;
import com.store.system.client.ClientProductSPU;
import com.store.system.model.ProductSKU;
import com.store.system.model.ProductSPU;
import com.store.system.model.UserGradeCategoryDiscount;

import java.util.List;

public interface ProductService {

    public ProductSPU addSPU(ProductSPU productSPU) throws Exception;

    public ProductSKU addSKU(ProductSKU productSKU) throws Exception;

    public void add(ProductSPU productSPU, List<ProductSKU> productSKUList, String brandName, String seriesName) throws Exception;

    public ClientProductSPU loadSPU(long id) throws Exception;

    public boolean updateSPU(ProductSPU productSPU) throws Exception;

    public boolean updateSKU(ProductSKU productSKU) throws Exception;

    public void change(ProductSPU productSPU, List<ProductSKU> addProductSKUList, List<ProductSKU> updateProductSKUList,
                       List<Long> delSkuids, String brandName, String seriesName) throws Exception;

    public boolean delSKU(long id) throws Exception;

    public boolean delSPU(long id) throws Exception;

    public Pager getSPUBackPager(Pager pager, long subid, long cid, long pid, long bid, long sid,int type,String name,int saleStatus) throws Exception;



    public ClientProductSPU selectSPU(long subid, long pid, long cid, long bid, long sid) throws Exception;

    public Pager getSaleSPUBackPager(Pager pager, long pSubid, long subid, long cid, long bid, int type) throws Exception;

    public List<ClientProductSKU> getSaleSKUAllList(long subid, long spuid, long uid) throws Exception;

    public boolean updateSaleStatus(long id, int open)throws Exception;

    public List<ProductSKU> getSkuBySubid(long subid,int type)throws Exception;

    public boolean checkStatus(List<Long> ids) throws Exception;

    public Pager getSPUNoNirNumPager(Pager pager, long subid)throws Exception;
}
