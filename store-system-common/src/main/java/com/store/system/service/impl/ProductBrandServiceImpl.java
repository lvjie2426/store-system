package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.store.system.dao.ProductBrandDao;
import com.store.system.dao.ProductBrandPoolDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ProductBrand;
import com.store.system.model.ProductBrandPool;
import com.store.system.service.ProductBrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.search.SearchTerm;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProductBrandServiceImpl implements ProductBrandService {

    @Resource
    private ProductBrandDao productBrandDao;

    @Resource
    private ProductBrandPoolDao productBrandPoolDao;

    private TransformFieldSetUtils poolFieldSetUtils = new TransformFieldSetUtils(ProductBrandPool.class);

    private TransformMapUtils mapUtils = new TransformMapUtils(ProductBrand.class);

    private void check(ProductBrand productBrand) throws StoreSystemException {
        String name = productBrand.getName();
        if(StringUtils.isBlank(name)) throw new StoreSystemException("名称不能为空");
    }

    @Override
    public ProductBrand add(ProductBrand productBrand) throws Exception {
        check(productBrand);
        productBrand = productBrandDao.insert(productBrand);
        return productBrand;
    }

    @Override
    public boolean update(ProductBrand productBrand) throws Exception {
        check(productBrand);
        boolean res = productBrandDao.update(productBrand);
        return res;
    }

    @Override
    public boolean del(long id) throws Exception {
        ProductBrand productBrand = productBrandDao.load(id);
        if(null != productBrand) {
            productBrand.setStatus(ProductBrand.status_delete);
            return productBrandDao.update(productBrand);
        }
        return false;
    }

    @Override
    public List<ProductBrand> getAllList() throws Exception {
        return productBrandDao.getAllList(ProductBrand.status_nomore);
    }


    @Override
    public boolean addPool(ProductBrandPool pool) throws Exception {
        ProductBrandPool sign = productBrandPoolDao.load(pool);
        if(sign != null) throw new StoreSystemException("已添加");
        pool = productBrandPoolDao.insert(pool);
        return pool != null;
    }

    @Override
    public boolean delPool(ProductBrandPool pool) throws Exception {
        return productBrandPoolDao.delete(pool);
    }

    @Override
    public List<ProductBrand> getSubAllList(long subid) throws Exception {
        List<ProductBrandPool> pools = productBrandPoolDao.getAllList(subid);
        Set<Long> bids = poolFieldSetUtils.fieldList(pools, "bid");
        List<ProductBrand> brands = productBrandDao.load(Lists.newArrayList(bids));
        Map<Long, ProductBrand> brandMap = mapUtils.listToMap(brands, "id");
        List<ProductBrand> res = Lists.newArrayList();
        for(ProductBrandPool pool : pools) {
            ProductBrand brand = brandMap.get(pool.getBid());
            if(brand != null) res.add(brand);
        }
        return res;
    }
}
