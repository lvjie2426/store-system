package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.model.*;

import java.util.List;

public interface ProductPropertyNameService {

    public ProductPropertyName add(ProductPropertyName productPropertyName) throws Exception;

    public boolean update(ProductPropertyName productBrand) throws Exception;

    public boolean del(long id) throws Exception;

    public ProductPropertyName load(long id) throws Exception;

    public List<ProductPropertyName> getAllList(long cid) throws Exception;

    public boolean updateStatus(long id, int status) throws Exception;





    public boolean addPool(ProductPropertyNamePool pool) throws Exception;

    public boolean delPool(ProductPropertyNamePool pool) throws Exception;

    public List<ProductPropertyName> getSubAllList(long subid, long cid) throws Exception;

    public  Pager search(Pager pager, long cid, int type, String content, int input, int defaul, int multiple,int status)throws Exception;
}
