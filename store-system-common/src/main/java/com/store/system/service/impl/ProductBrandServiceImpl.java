package com.store.system.service.impl;

import com.store.system.dao.ProductBrandDao;
import com.store.system.exception.GlassesException;
import com.store.system.model.ProductBrand;
import com.store.system.service.ProductBrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductBrandServiceImpl implements ProductBrandService {

    @Resource
    private ProductBrandDao productBrandDao;

    private void check(ProductBrand productBrand) throws GlassesException {
        String name = productBrand.getName();
        if(StringUtils.isBlank(name)) throw new GlassesException("名称不能为空");
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

}
