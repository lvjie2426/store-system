package com.store.system.service;

import com.store.system.model.GoodsSeries;

import java.util.List;

public interface GoodsSeriesService {

    public GoodsSeries add(GoodsSeries goodsSeries) throws Exception;

    public boolean update(GoodsSeries goodsSeries) throws Exception;

    public boolean del(long id) throws Exception;

    public List<GoodsSeries> getAllList(long gbid) throws Exception;

}
