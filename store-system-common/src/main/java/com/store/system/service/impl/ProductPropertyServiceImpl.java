package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.store.system.client.ClientProductProperty;
import com.store.system.client.ClientProductPropertyValue;
import com.store.system.dao.ProductPropertyNameDao;
import com.store.system.dao.ProductPropertyValueDao;
import com.store.system.model.ProductPropertyName;
import com.store.system.model.ProductPropertyValue;
import com.store.system.service.ProductPropertyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductPropertyServiceImpl implements ProductPropertyService {

    @Resource
    private ProductPropertyNameDao productPropertyNameDao;

    @Resource
    private ProductPropertyValueDao productPropertyValueDao;

    @Override
    public List<ClientProductProperty> getAllProperties(long cid) throws Exception {
        List<ProductPropertyName> names = productPropertyNameDao.getAllList(cid, ProductPropertyName.status_nomore);
        List<ClientProductProperty> res = Lists.newArrayList();
        for(ProductPropertyName name : names) {
            ClientProductProperty property = new ClientProductProperty(name);
            res.add(property);

            if(property.getInput() == ProductPropertyName.input_no) {
                List<ProductPropertyValue> list = productPropertyValueDao.getAllList(property.getId(), ProductPropertyValue.status_nomore);
                List<ClientProductPropertyValue> values = Lists.newArrayList();
                for(ProductPropertyValue one : list) {
                    ClientProductPropertyValue value = new ClientProductPropertyValue(one);
                    values.add(value);
                }
                property.setValues(values);
            }
        }
        return res;
    }

}
