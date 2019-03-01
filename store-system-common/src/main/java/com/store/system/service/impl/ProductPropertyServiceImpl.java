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
import java.util.Iterator;
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

    /**
     * 获取入库的时候要明确的SKU属性
     * method_name: getSKUProperties
     * params: [cid]
     * return: java.util.List<com.store.system.client.ClientProductProperty>
     * creat_user: lihao
     * creat_date: 2019/2/28
     * creat_time: 14:44
     **/
    @Override
    public List<ClientProductProperty> getSKUProperties(long cid) throws Exception {
        List<ProductPropertyName> names = productPropertyNameDao.getAllList(cid, ProductPropertyName.status_nomore);
        for(Iterator<ProductPropertyName> it = names.iterator(); it.hasNext();) {
            ProductPropertyName one = it.next();
            if(one.getType() != ProductPropertyName.type_sku) it.remove();
        }
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
