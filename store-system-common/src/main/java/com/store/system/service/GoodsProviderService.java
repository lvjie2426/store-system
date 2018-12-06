package com.store.system.service;

import com.store.system.model.GoodsProvider;

import java.util.List;

public interface GoodsProviderService {

    public GoodsProvider add(GoodsProvider goodsProvider) throws Exception;

    public boolean update(GoodsProvider goodsProvider) throws Exception;

    public boolean del(long id) throws Exception;

    public List<GoodsProvider> getAllList() throws Exception;

}
