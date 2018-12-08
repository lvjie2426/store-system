package com.store.system.service.impl;

import com.store.system.dao.ProductProviderDao;
import com.store.system.exception.GlassesException;
import com.store.system.model.ProductProvider;
import com.store.system.service.ProductProviderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductProviderServiceImpl implements ProductProviderService {

    @Resource
    private ProductProviderDao productProviderDao;

    private void check(ProductProvider productProvider) throws GlassesException {
        String name = productProvider.getName();
        if(StringUtils.isBlank(name)) throw new GlassesException("名称不能为空");
    }

    @Override
    public ProductProvider add(ProductProvider productProvider) throws Exception {
        check(productProvider);
        productProvider = productProviderDao.insert(productProvider);
        return productProvider;
    }

    @Override
    public boolean update(ProductProvider productProvider) throws Exception {
        check(productProvider);
        boolean res = productProviderDao.update(productProvider);
        return res;
    }

    @Override
    public boolean del(long id) throws Exception {
        ProductProvider productProvider = productProviderDao.load(id);
        if(null != productProvider) {
            productProvider.setStatus(ProductProvider.status_delete);
            return productProviderDao.update(productProvider);
        }
        return false;
    }

    @Override
    public List<ProductProvider> getAllList() throws Exception {
        return productProviderDao.getAllList(ProductProvider.status_nomore);
    }

}
