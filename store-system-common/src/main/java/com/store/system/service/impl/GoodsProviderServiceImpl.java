package com.store.system.service.impl;

import com.store.system.dao.GoodsProviderDao;
import com.store.system.exception.GlassesException;
import com.store.system.model.GoodsProvider;
import com.store.system.service.GoodsProviderService;
import com.s7.baseFramework.transform.TransformFieldSetUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service
public class GoodsProviderServiceImpl implements GoodsProviderService {

    private TransformFieldSetUtils goodsProviderFieldSetUtils = new TransformFieldSetUtils(GoodsProvider.class);

    @Resource
    private GoodsProviderDao goodsProviderDao;

    private void check(GoodsProvider goodsProvider) throws GlassesException {
        if(StringUtils.isBlank(goodsProvider.getName()))
            throw new GlassesException("名称不能为空");
        List<GoodsProvider> list = goodsProviderDao.getAllList(GoodsProvider.status_nomore);
        Set<String> names = goodsProviderFieldSetUtils.fieldList(list, "name");
        if(names.contains(goodsProvider.getName()))
            throw new GlassesException("名称存在");
    }

    @Override
    public GoodsProvider add(GoodsProvider goodsProvider) throws Exception {
        check(goodsProvider);
        GoodsProvider res = goodsProviderDao.insert(goodsProvider);
        return res;
    }

    @Override
    public boolean update(GoodsProvider goodsProvider) throws Exception {
        check(goodsProvider);
        boolean res = goodsProviderDao.update(goodsProvider);
        return res;
    }

    @Override
    public boolean del(long id) throws Exception {
        GoodsProvider goodsProvider = goodsProviderDao.load(id);
        if(null != goodsProvider) {
            goodsProvider.setStatus(GoodsProvider.status_delete);
            return goodsProviderDao.update(goodsProvider);
        }
        return false;
    }

    @Override
    public List<GoodsProvider> getAllList() throws Exception {
        List<GoodsProvider> res = goodsProviderDao.getAllList(GoodsProvider.status_nomore);
        return res;
    }

}
