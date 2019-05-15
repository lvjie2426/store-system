package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.store.system.dao.ProductCategoryDao;
import com.store.system.dao.ProductCategoryPoolDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ProductBrand;
import com.store.system.model.ProductBrandPool;
import com.store.system.model.ProductCategory;
import com.store.system.model.ProductCategoryPool;
import com.store.system.service.ProductCategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Resource
    private ProductCategoryDao productCategoryDao;

    @Resource
    private ProductCategoryPoolDao productCategoryPoolDao;

    private TransformFieldSetUtils poolFieldSetUtils = new TransformFieldSetUtils(ProductCategoryPool.class);

    private TransformMapUtils mapUtils = new TransformMapUtils(ProductCategory.class);

    private void check(ProductCategory productCategory) throws StoreSystemException {
        String name = productCategory.getName();
        if(StringUtils.isBlank(name)) throw new StoreSystemException("名称不能为空");
    }

    @Override
    public ProductCategory add(ProductCategory productCategory) throws Exception {
        check(productCategory);
        productCategory = productCategoryDao.insert(productCategory);
        return productCategory;
    }

    @Override
    public boolean update(ProductCategory productCategory) throws Exception {
        check(productCategory);
        ProductCategory load = productCategoryDao.load(productCategory.getId());
        load.setName(productCategory.getName());
        boolean res = productCategoryDao.update(load);
        return res;
    }

    @Override
    public boolean del(long id) throws Exception {
        ProductCategory productCategory = productCategoryDao.load(id);
        if(null != productCategory) {
            productCategory.setStatus(ProductCategory.status_delete);
            return productCategoryDao.update(productCategory);
        }
        return false;
    }

    @Override
    public List<ProductCategory> getAllList() throws Exception {
        return productCategoryDao.getAllList(ProductCategory.status_nomore);
    }

    @Override
    public boolean addPool(ProductCategoryPool pool) throws Exception {
        ProductCategoryPool sign = productCategoryPoolDao.load(pool);
        if(sign != null) throw new StoreSystemException("已添加");
        pool = productCategoryPoolDao.insert(pool);
        return pool != null;
    }

    @Override
    public boolean delPool(ProductCategoryPool pool) throws Exception {
        return productCategoryPoolDao.delete(pool);
    }

    @Override
    public List<ProductCategory> getSubAllList(long subid) throws Exception {
        List<ProductCategoryPool> pools = productCategoryPoolDao.getAllList(subid);
        Set<Long> cids = poolFieldSetUtils.fieldList(pools, "cid");
        List<ProductCategory> categories = productCategoryDao.load(Lists.newArrayList(cids));
        Map<Long, ProductCategory> categoryMap = mapUtils.listToMap(categories, "id");
        List<ProductCategory> res = Lists.newArrayList();
        for(ProductCategoryPool pool : pools) {
            ProductCategory category = categoryMap.get(pool.getCid());
            if(category != null) res.add(category);
        }
        return res;
    }
}
