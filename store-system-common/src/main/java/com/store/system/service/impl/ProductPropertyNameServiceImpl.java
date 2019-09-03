package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.store.system.client.ClientProductPropertyName;
import com.store.system.client.ClientProductSPU;
import com.store.system.dao.ProductCategoryDao;
import com.store.system.dao.ProductPropertyNameDao;
import com.store.system.dao.ProductPropertyNamePoolDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.ProductCategoryService;
import com.store.system.service.ProductPropertyNameService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
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
    private JdbcTemplate jdbcTemplate;
    @Resource
    private ProductCategoryDao productCategoryDao;

    @Resource
    private ProductPropertyNamePoolDao productPropertyNamePoolDao;

    private TransformFieldSetUtils poolFieldSetUtils = new TransformFieldSetUtils(ProductPropertyNamePool.class);

    private TransformMapUtils mapUtils = new TransformMapUtils(ProductPropertyName.class);

    private void check(ProductPropertyName productPropertyName) throws StoreSystemException {
        String content = productPropertyName.getContent();
        if (StringUtils.isBlank(content)) throw new StoreSystemException("内容不能为空");
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
        if (null != productPropertyName) {
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
    public  boolean updateStatus(long id, int status)  throws Exception {
        ProductPropertyName name = productPropertyNameDao.load(id);
        name.setStatus(status);
        return productPropertyNameDao.update(name);
    }

    @Override
    public boolean addPool(ProductPropertyNamePool pool) throws Exception {
        ProductPropertyNamePool sign = productPropertyNamePoolDao.load(pool);
        if (sign != null) throw new StoreSystemException("已添加");
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
        for (ProductPropertyNamePool pool : pools) {
            ProductPropertyName propertyName = propertyNameMap.get(pool.getPnid());
            if (propertyName != null) res.add(propertyName);
        }
        return res;
    }

    @Override
    public Pager search(Pager pager, long cid, int type, String content, int input, int defaul, int multiple,int status) throws Exception {
        String sql = "SELECT  *  FROM `product_property_name` where  1=1 ";
        String countSql = "SELECT  count(1)  FROM `product_property_name` where  1=1 ";
        final String limit = "  limit %d , %d ";
        if (status > -1)  {
            sql = sql + " and `status` = " + status;
            countSql = countSql + " and `status` = " + status;
        }
        if (cid > 0) {
            sql = sql + " and `cid` = " + cid;
            countSql = countSql + " and `cid` = " + cid;
        }
        if (type > 0) {
            sql = sql + " and `type` = " + type;
            countSql = countSql + " and `type` = " + type;
        }
        if (input > -1) {
            sql = sql + " and `input` = " + input;
            countSql = countSql + " and `input` = " + input;
        }
        if (defaul > -1) {
            sql = sql + " and `defaul` = " + defaul;
            countSql = countSql + " and `defaul` = " + defaul;
        }
        if (multiple > -1) {
            sql = sql + " and `multiple` = " + multiple;
            countSql = countSql + " and `multiple` = " + multiple;
        }

        if (StringUtils.isNotBlank(content)) {
            sql = sql + " and `content` like '%" + content + "%'";
            countSql = countSql + " and `content` like '%" + content + "%'";
        }

        sql = sql + " order  by sort desc";
        sql = sql + String.format(limit, pager.getSize() * (pager.getPage() - 1), pager.getSize());
        List<ProductPropertyName> productPropertyNameList = jdbcTemplate.query(sql, new HyperspaceBeanPropertyRowMapper(ProductPropertyName.class));
        int count = jdbcTemplate.queryForObject(countSql, Integer.class);
        pager.setData(transformClients(productPropertyNameList));
        pager.setTotalCount(count);
        return pager;
    }
    private List<ClientProductPropertyName> transformClients(List<ProductPropertyName> productPropertyNameList) throws Exception {
        List<ClientProductPropertyName> res = Lists.newArrayList();
        if (productPropertyNameList.size() == 0) return res;

        for(ProductPropertyName productPropertyName:productPropertyNameList){
            ClientProductPropertyName clientProductPropertyName=new ClientProductPropertyName(productPropertyName);
            ProductCategory load = productCategoryDao.load(productPropertyName.getCid());
            clientProductPropertyName.setCName(load.getName());
            res.add(clientProductPropertyName);
        }

        return res;
    }

}
