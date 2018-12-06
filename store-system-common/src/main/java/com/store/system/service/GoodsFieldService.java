package com.store.system.service;

import com.store.system.model.GoodsField;
import com.store.system.model.GoodsFieldItem;

import java.util.List;

public interface GoodsFieldService {

    public GoodsField add(GoodsField goodsField) throws Exception;

    public boolean update(GoodsField goodsField) throws Exception;

    public boolean del(long id) throws Exception;

    public List<GoodsField> getAllList(int type) throws Exception;




    public GoodsFieldItem addItem(GoodsFieldItem goodsFieldItem) throws Exception;

    public boolean updateItem(GoodsFieldItem goodsFieldItem) throws Exception;

    public boolean delItem(long id) throws Exception;

    public List<GoodsFieldItem> getItemAllList(long gfid) throws Exception;

}
