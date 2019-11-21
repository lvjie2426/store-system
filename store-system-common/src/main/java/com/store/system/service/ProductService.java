package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.client.ClientInventoryDetail;
import com.store.system.client.ClientProductCategory;
import com.store.system.client.ClientProductSKU;
import com.store.system.client.ClientProductSPU;
import com.store.system.model.Commission;
import com.store.system.model.ProductSKU;
import com.store.system.model.ProductSPU;
import com.store.system.model.User;

import java.util.List;
import java.util.Map;

public interface ProductService {

    public ProductSPU addSPU(ProductSPU productSPU) throws Exception;

    public ProductSKU addSKU(ProductSKU productSKU) throws Exception;

    public void add(ProductSPU productSPU, List<ProductSKU> productSKUList, String brandName, String seriesName, List<Commission> commissions) throws Exception;

    public ClientProductSPU loadSPU(long id) throws Exception;

    public boolean updateSPU(ProductSPU productSPU) throws Exception;

    public boolean updateSKU(ProductSKU productSKU) throws Exception;

    public void change(ProductSPU productSPU, List<ProductSKU> addProductSKUList, List<ProductSKU> updateProductSKUList,
                       List<Long> delSkuids, String brandName, String seriesName, List<Commission> commissions) throws Exception;

    public boolean delSKU(long id) throws Exception;

    public boolean delSPU(long id) throws Exception;

    public Pager getSPUBackPager(Pager pager, long subid, long cid, long pid, long bid, long sid,int type,String name,int saleStatus,long subId) throws Exception;


    public ClientProductSPU selectSPU(long subid, long pid, long cid, long bid, long sid) throws Exception;

    public List<ClientProductSPU> selectSPU(long subid, long cid) throws Exception;

    public Pager getSaleSPUBackPager(Pager pager, long pSubid, long subid, long cid, long bid, int type) throws Exception;

    public Pager getSaleSPUPager(Pager pager, long pSubid, long subid, int type, String brandSeries) throws Exception;

    public List<ClientProductSKU> getSaleSKUAllList(long subid, long spuid, long uid) throws Exception;

    public boolean updateSaleStatus(long id, int open)throws Exception;

    public List<ProductSKU> getSkuBySubid(long subid,int type)throws Exception;

    public boolean checkStatus(List<Long> ids,User user) throws Exception;

    public Pager getSPUNoNirNumPager(Pager pager, long subid)throws Exception;

    public Pager getCommSpu(Pager pager,long subid, long cid,String name)throws Exception;

    // 用于按照name搜索商品
    public ClientProductCategory searchSpu(String name)throws Exception;

    public Map<Object, Object> getProperties(Object clazz, Object client, String property) throws Exception;

}
