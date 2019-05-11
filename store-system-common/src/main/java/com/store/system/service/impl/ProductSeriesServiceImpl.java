package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.store.system.dao.ProductBrandDao;
import com.store.system.dao.ProductSeriesDao;
import com.store.system.dao.ProductSeriesPoolDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.ProductBrandService;
import com.store.system.service.ProductSeriesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProductSeriesServiceImpl implements ProductSeriesService {

    @Resource
    private ProductSeriesDao productSeriesDao;
    @Resource
    private ProductBrandDao productBrandDao;
    @Resource
    private ProductSeriesPoolDao productSeriesPoolDao;

    private TransformFieldSetUtils poolFieldSetUtils = new TransformFieldSetUtils(ProductSeriesPool.class);

    private TransformMapUtils mapUtils = new TransformMapUtils(ProductSeries.class);

    private void check(ProductSeries productSeries) throws StoreSystemException {
        long bid = productSeries.getBid();
        String name = productSeries.getName();
        if(bid == 0) throw new StoreSystemException("品牌不能为空");
        if(StringUtils.isBlank(name)) throw new StoreSystemException("名称不能为空");
    }

    @Override
    public ProductSeries add(ProductSeries productSeries) throws Exception {
        check(productSeries);
        productSeries = productSeriesDao.insert(productSeries);
        return productSeries;
    }

    @Override
    public boolean update(ProductSeries productSeries) throws Exception {
        check(productSeries);
        ProductSeries oldProductSeries = productSeriesDao.load(productSeries);
        if(oldProductSeries==null){throw new StoreSystemException("未找到该系列!");}
        if(productSeries.getBid()>0){
            ProductBrand productBrand = productBrandDao.load(productSeries.getBid());
            if(productBrand==null){throw new StoreSystemException("未找到该品牌!");}
            oldProductSeries.setBid(productSeries.getBid());
        }
        if(productSeries.getName()!=null){
            oldProductSeries.setName(productSeries.getName());
        }
        if(productSeries.getDesc()!=null){
            oldProductSeries.setDesc(productSeries.getDesc());
        }
        if(productSeries.getIcon()!=null){
            oldProductSeries.setIcon(productSeries.getIcon());
        }
        if(productSeries.getSort()>0){
            oldProductSeries.setSort(productSeries.getSort());
        }
        oldProductSeries.setStatus(productSeries.getStatus());
        boolean res = productSeriesDao.update(oldProductSeries);
        return res;
    }

    @Override
    public boolean del(long id) throws Exception {
        ProductSeries productSeries = productSeriesDao.load(id);
        if(null != productSeries) {
            productSeries.setStatus(ProductSeries.status_delete);
            return productSeriesDao.update(productSeries);
        }
        return false;
    }

    @Override
    public ProductSeries load(long id) throws Exception {
        return productSeriesDao.load(id);
    }

    @Override
    public List<ProductSeries> getAllList(long bid) throws Exception {
        return productSeriesDao.getAllList(bid, ProductSeries.status_nomore);
    }

    @Override
    public boolean addPool(ProductSeriesPool pool) throws Exception {
        ProductSeriesPool sign = productSeriesPoolDao.load(pool);
        if(sign != null) throw new StoreSystemException("已添加");
        pool = productSeriesPoolDao.insert(pool);
        return pool != null;
    }

    @Override
    public boolean delPool(ProductSeriesPool pool) throws Exception {
        return productSeriesPoolDao.delete(pool);
    }

    @Override
    public List<ProductSeries> getSubAllList(long subid, long bid) throws Exception {
        List<ProductSeriesPool> pools = productSeriesPoolDao.getAllList(subid, bid);
        Set<Long> sids = poolFieldSetUtils.fieldList(pools, "sid");
        List<ProductSeries> seriesList = productSeriesDao.load(Lists.newArrayList(sids));
        Map<Long, ProductSeries> seriesMap = mapUtils.listToMap(seriesList, "id");
        List<ProductSeries> res = Lists.newArrayList();
        for(ProductSeriesPool pool : pools) {
            ProductSeries series = seriesMap.get(pool.getSid());
            if(series != null) res.add(series);
        }
        return res;
    }
}
