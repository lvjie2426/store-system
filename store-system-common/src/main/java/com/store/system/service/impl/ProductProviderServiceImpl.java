package com.store.system.service.impl;

import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.store.system.dao.ProductProviderDao;
import com.store.system.dao.ProductProviderPoolDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ProductProvider;
import com.store.system.model.ProductProviderPool;
import com.store.system.service.ProductProviderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductProviderServiceImpl implements ProductProviderService {

    @Resource
    private ProductProviderDao productProviderDao;

    @Resource
    private ProductProviderPoolDao productProviderPoolDao;

    private TransformFieldSetUtils poolFieldSetUtils = new TransformFieldSetUtils(ProductProviderPool.class);

    private TransformMapUtils mapUtils = new TransformMapUtils(ProductProvider.class);

    private void check(ProductProvider productProvider) throws StoreSystemException {
        String name = productProvider.getName();
        if(StringUtils.isBlank(name)) throw new StoreSystemException("名称不能为空");
        List<ProductProvider> productProviders = productProviderDao.getAllList(ProductProvider.status_nomore);
        for(ProductProvider provider:productProviders){
            if(provider.getName().equals(productProvider.getName())){
                throw new StoreSystemException("该供应商已存在,不可重复添加！");
            }
        }
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


    @Override
    public boolean addPool(ProductProviderPool pool) throws Exception {
        ProductProviderPool sign = productProviderPoolDao.load(pool);
        if(sign != null) throw new StoreSystemException("已添加");
        pool = productProviderPoolDao.insert(pool);
        return pool != null;
    }

    @Override
    public boolean delPool(ProductProviderPool pool) throws Exception {
        return productProviderPoolDao.delete(pool);
    }

    @Override
    public List<ProductProvider> getSubAllList(long subid) throws Exception {
        List<ProductProvider> providers = productProviderDao.getAllList(ProductProvider.status_nomore,subid);
        return providers;
    }

}
