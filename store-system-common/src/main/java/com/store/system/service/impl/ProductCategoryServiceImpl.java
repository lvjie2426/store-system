package com.store.system.service.impl;

import com.store.system.dao.ProductCategoryDao;
import com.store.system.exception.GlassesException;
import com.store.system.model.ProductCategory;
import com.store.system.service.ProductCategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Resource
    private ProductCategoryDao productCategoryDao;

    private void check(ProductCategory productCategory) throws GlassesException {
        String name = productCategory.getName();
        if(StringUtils.isBlank(name)) throw new GlassesException("名称不能为空");
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
        boolean res = productCategoryDao.update(productCategory);
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

}
