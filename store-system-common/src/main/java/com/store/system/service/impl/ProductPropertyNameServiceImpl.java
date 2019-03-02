package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.store.system.dao.ProductPropertyNameDao;
import com.store.system.dao.ProductPropertyNamePoolDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.ProductPropertyNameService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProductPropertyNameServiceImpl implements ProductPropertyNameService {

    @Resource
    private ProductPropertyNameDao productPropertyNameDao;

    @Resource
    private ProductPropertyNamePoolDao productPropertyNamePoolDao;

    private TransformFieldSetUtils poolFieldSetUtils = new TransformFieldSetUtils(ProductPropertyNamePool.class);

    private TransformMapUtils mapUtils = new TransformMapUtils(ProductPropertyName.class);

    private void check(ProductPropertyName productPropertyName) throws StoreSystemException {
        String content = productPropertyName.getContent();
        if(StringUtils.isBlank(content)) throw new StoreSystemException("内容不能为空");
    }

    @Override
    public ProductPropertyName add(ProductPropertyName productPropertyName) throws Exception {
        check(productPropertyName);
        productPropertyName = productPropertyNameDao.insert(productPropertyName);
        return productPropertyName;
    }

    @Override
    public boolean update(ProductPropertyName productPropertyName) throws Exception {
        check(productPropertyName);
        boolean res = productPropertyNameDao.update(productPropertyName);
        return res;
    }

    @Override
    public boolean del(long id) throws Exception {
        ProductPropertyName productPropertyName = productPropertyNameDao.load(id);
        if(null != productPropertyName) {
            productPropertyName.setStatus(ProductPropertyName.status_delete);
            return productPropertyNameDao.update(productPropertyName);
        }
        return false;
    }

    @Override
    public ProductPropertyName load(long id) throws Exception {
        return productPropertyNameDao.load(id);
    }

    @Override
    public List<ProductPropertyName> getAllList(long cid) throws Exception {
        return productPropertyNameDao.getAllList(cid, ProductPropertyName.status_nomore);
    }

    @Override
    public boolean addPool(ProductPropertyNamePool pool) throws Exception {
        ProductPropertyNamePool sign = productPropertyNamePoolDao.load(pool);
        if(sign != null) throw new StoreSystemException("已添加");
        pool = productPropertyNamePoolDao.insert(pool);
        return pool != null;
    }

    @Override
    public boolean delPool(ProductPropertyNamePool pool) throws Exception {
        return productPropertyNamePoolDao.delete(pool);
    }

    @Override
    public List<ProductPropertyName> getSubAllList(long subid, long cid) throws Exception {
        List<ProductPropertyNamePool> pools = productPropertyNamePoolDao.getAllList(subid, cid);
        Set<Long> pnids = poolFieldSetUtils.fieldList(pools, "pnid");
        List<ProductPropertyName> propertyNames = productPropertyNameDao.load(Lists.newArrayList(pnids));
        Map<Long, ProductPropertyName> propertyNameMap = mapUtils.listToMap(propertyNames, "id");
        List<ProductPropertyName> res = Lists.newArrayList();
        for(ProductPropertyNamePool pool : pools) {
            ProductPropertyName propertyName = propertyNameMap.get(pool.getPnid());
            if(propertyName != null) res.add(propertyName);
        }
        return res;
    }
}
