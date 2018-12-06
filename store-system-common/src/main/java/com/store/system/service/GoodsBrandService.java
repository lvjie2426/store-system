package com.store.system.service;

import com.store.system.model.GoodsBrand;

import java.util.List;

public interface GoodsBrandService {

    public GoodsBrand add(GoodsBrand goodsBrand) throws Exception;

    public boolean update(GoodsBrand goodsBrand) throws Exception;

    public boolean del(long id) throws Exception;

    public List<GoodsBrand> getAllList() throws Exception;

}
