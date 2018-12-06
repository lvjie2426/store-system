package com.store.system.service.impl;

import com.store.system.dao.GoodsSeriesDao;
import com.store.system.exception.GlassesException;
import com.store.system.model.GoodsSeries;
import com.store.system.service.GoodsSeriesService;
import com.s7.baseFramework.transform.TransformFieldSetUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service
public class GoodsSeriesServiceImpl implements GoodsSeriesService {

    private TransformFieldSetUtils goodsSeriesFieldSetUtils = new TransformFieldSetUtils(GoodsSeries.class);

    @Resource
    private GoodsSeriesDao goodsSeriesDao;

    private void check(GoodsSeries goodsSeries) throws GlassesException {
        if(StringUtils.isBlank(goodsSeries.getName()))
            throw new GlassesException("名称不能为空");
        List<GoodsSeries> list = goodsSeriesDao.getAllList(goodsSeries.getGbid(), GoodsSeries.status_nomore);
        Set<String> names = goodsSeriesFieldSetUtils.fieldList(list, "name");
        if(names.contains(goodsSeries.getName()))
            throw new GlassesException("名称存在");
    }

    @Override
    public GoodsSeries add(GoodsSeries goodsSeries) throws Exception {
        check(goodsSeries);
        GoodsSeries res = goodsSeriesDao.insert(goodsSeries);
        return res;
    }

    @Override
    public boolean update(GoodsSeries goodsSeries) throws Exception {
        check(goodsSeries);
        boolean res = goodsSeriesDao.update(goodsSeries);
        return res;
    }

    @Override
    public boolean del(long id) throws Exception {
        GoodsSeries goodsSeries = goodsSeriesDao.load(id);
        if(null != goodsSeries) {
            goodsSeries.setStatus(GoodsSeries.status_delete);
            return goodsSeriesDao.update(goodsSeries);
        }
        return false;
    }

    @Override
    public List<GoodsSeries> getAllList(long gbid) throws Exception {
        List<GoodsSeries> res = goodsSeriesDao.getAllList(gbid, GoodsSeries.status_nomore);
        return res;
    }

}
