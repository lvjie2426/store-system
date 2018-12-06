package com.store.system.service.impl;

import com.store.system.dao.GoodsFieldDao;
import com.store.system.dao.GoodsFieldItemDao;
import com.store.system.exception.GlassesException;
import com.store.system.model.GoodsField;
import com.store.system.model.GoodsFieldItem;
import com.store.system.service.GoodsFieldService;
import com.s7.baseFramework.transform.TransformFieldSetUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service
public class GoodsFieldServiceImpl implements GoodsFieldService {

    private TransformFieldSetUtils goodsFieldFieldSetUtils = new TransformFieldSetUtils(GoodsField.class);

    private TransformFieldSetUtils goodsFieldItemFieldSetUtils = new TransformFieldSetUtils(GoodsFieldItem.class);

    @Resource
    private GoodsFieldDao goodsFieldDao;

    @Resource
    private GoodsFieldItemDao goodsFieldItemDao;

    private void check(GoodsField goodsField) throws GlassesException {
        if(StringUtils.isBlank(goodsField.getKey()))
            throw new GlassesException("唯一值不能为空");
        List<GoodsField> list = goodsFieldDao.getAllList(goodsField.getType(), GoodsField.status_nomore);
        Set<String> keys = goodsFieldFieldSetUtils.fieldList(list, "key");
        if(keys.contains(goodsField.getKey()))
            throw new GlassesException("唯一值存在");
    }

    @Override
    public GoodsField add(GoodsField goodsField) throws Exception {
        check(goodsField);
        GoodsField res = goodsFieldDao.insert(goodsField);
        return res;
    }

    @Override
    public boolean update(GoodsField goodsField) throws Exception {
        check(goodsField);
        boolean res = goodsFieldDao.update(goodsField);
        return res;
    }

    @Override
    public boolean del(long id) throws Exception {
        GoodsField goodsField = goodsFieldDao.load(id);
        if(null != goodsField) {
            goodsField.setStatus(GoodsField.status_delete);
            return goodsFieldDao.update(goodsField);
        }
        return false;
    }

    @Override
    public List<GoodsField> getAllList(int type) throws Exception {
        List<GoodsField> res = goodsFieldDao.getAllList(type, GoodsField.status_nomore);
        return res;
    }




    private void check(GoodsFieldItem goodsFieldItem) throws GlassesException {
        if(StringUtils.isBlank(goodsFieldItem.getContent()))
            throw new GlassesException("内容不能为空");
        List<GoodsFieldItem> list = goodsFieldItemDao.getAllList(goodsFieldItem.getGfid(), GoodsFieldItem.status_nomore);
        Set<String> contents = goodsFieldItemFieldSetUtils.fieldList(list, "content");
        if(contents.contains(goodsFieldItem.getContent()))
            throw new GlassesException("内容存在");
    }

    @Override
    public GoodsFieldItem addItem(GoodsFieldItem goodsFieldItem) throws Exception {
        check(goodsFieldItem);
        GoodsFieldItem res = goodsFieldItemDao.insert(goodsFieldItem);
        return res;
    }

    @Override
    public boolean updateItem(GoodsFieldItem goodsFieldItem) throws Exception {
        check(goodsFieldItem);
        boolean res = goodsFieldItemDao.update(goodsFieldItem);
        return res;
    }

    @Override
    public boolean delItem(long id) throws Exception {
        GoodsFieldItem goodsFieldItem = goodsFieldItemDao.load(id);
        if(null != goodsFieldItem) {
            goodsFieldItem.setStatus(GoodsFieldItem.status_delete);
            return goodsFieldItemDao.update(goodsFieldItem);
        }
        return false;
    }

    @Override
    public List<GoodsFieldItem> getItemAllList(long gfid) throws Exception {
        List<GoodsFieldItem> res = goodsFieldItemDao.getAllList(gfid, GoodsFieldItem.status_nomore);
        return res;
    }
}
