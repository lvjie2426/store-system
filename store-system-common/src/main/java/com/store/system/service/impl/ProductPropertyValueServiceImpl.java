package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.store.system.dao.ProductPropertyValueDao;
import com.store.system.dao.ProductPropertyValuePoolDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ProductPropertyValue;
import com.store.system.model.ProductPropertyValuePool;
import com.store.system.service.ProductPropertyValueService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProductPropertyValueServiceImpl implements ProductPropertyValueService {

    @Resource
    private ProductPropertyValueDao productPropertyValueDao;

    @Resource
    private ProductPropertyValuePoolDao productPropertyValuePoolDao;

    private TransformFieldSetUtils poolFieldSetUtils = new TransformFieldSetUtils(ProductPropertyValuePool.class);

    private TransformMapUtils mapUtils = new TransformMapUtils(ProductPropertyValue.class);

    private void check(ProductPropertyValue productPropertyValue) throws StoreSystemException {
        String content = productPropertyValue.getContent();
        if(StringUtils.isBlank(content)) throw new StoreSystemException("内容不能为空");
    }

    @Override
    public ProductPropertyValue add(ProductPropertyValue productPropertyValue) throws Exception {
        check(productPropertyValue);
        productPropertyValue = productPropertyValueDao.insert(productPropertyValue);
        return productPropertyValue;
    }

    @Override
    public boolean update(ProductPropertyValue productPropertyValue) throws Exception {
        check(productPropertyValue);
        boolean res = productPropertyValueDao.update(productPropertyValue);
        return res;
    }

    @Override
    public boolean del(long id) throws Exception {
        ProductPropertyValue productPropertyValue = productPropertyValueDao.load(id);
        if(null != productPropertyValue) {
            productPropertyValue.setStatus(ProductPropertyValue.status_delete);
            return productPropertyValueDao.update(productPropertyValue);
        }
        return false;
    }

    @Override
    public List<ProductPropertyValue> getAllList(long pnid) throws Exception {
        return productPropertyValueDao.getAllList(pnid, ProductPropertyValue.status_nomore);
    }

    @Override
    public boolean addPool(ProductPropertyValuePool pool) throws Exception {
        ProductPropertyValuePool sign = productPropertyValuePoolDao.load(pool);
        if(sign != null) throw new StoreSystemException("已添加");
        pool = productPropertyValuePoolDao.insert(pool);
        return pool != null;
    }

    @Override
    public boolean delPool(ProductPropertyValuePool pool) throws Exception {
        return productPropertyValuePoolDao.delete(pool);
    }

    @Override
    public List<ProductPropertyValue> getSubAllList(long subid, long pnid) throws Exception {
        List<ProductPropertyValuePool> pools = productPropertyValuePoolDao.getAllList(subid, pnid);
        Set<Long> pvids = poolFieldSetUtils.fieldList(pools, "pvid");
        List<ProductPropertyValue> propertyValues = productPropertyValueDao.load(Lists.newArrayList(pvids));
        Map<Long, ProductPropertyValue> propertyValueMap = mapUtils.listToMap(propertyValues, "id");
        List<ProductPropertyValue> res = Lists.newArrayList();
        for(ProductPropertyValuePool pool : pools) {
            ProductPropertyValue propertyValue = propertyValueMap.get(pool.getPvid());
            if(propertyValue != null) res.add(propertyValue);
        }
        return res;
    }
}
