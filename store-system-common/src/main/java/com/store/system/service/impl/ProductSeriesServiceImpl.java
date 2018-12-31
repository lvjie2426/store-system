package com.store.system.service.impl;

import com.store.system.dao.ProductSeriesDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ProductSeries;
import com.store.system.service.ProductSeriesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductSeriesServiceImpl implements ProductSeriesService {

    @Resource
    private ProductSeriesDao productSeriesDao;

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
        boolean res = productSeriesDao.update(productSeries);
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
    public List<ProductSeries> getAllList(long bid) throws Exception {
        return productSeriesDao.getAllList(bid, ProductSeries.status_nomore);
    }
}
