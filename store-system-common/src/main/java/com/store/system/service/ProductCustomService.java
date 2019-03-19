package com.store.system.service;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.model.ProductCustom;

public interface ProductCustomService {

    public ProductCustom add(ProductCustom productCustom) throws Exception;

    public boolean updateStatus(long id, int status) throws Exception;

    public Pager getBackPager(Pager pager, long psid, int status) throws Exception;

    public Pager getBackSubPager(Pager pager, long sid, int status) throws Exception;

}
