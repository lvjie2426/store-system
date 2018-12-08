package com.store.system.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.s7.baseFramework.jackson.JsonUtils;
import com.s7.baseFramework.model.pagination.Pager;
import com.s7.baseFramework.transform.TransformFieldSetUtils;
import com.s7.ext.RowMapperHelp;
import com.store.system.client.ClientProductSKU;
import com.store.system.client.ClientProductSPU;
import com.store.system.dao.ProductPropertyNameDao;
import com.store.system.dao.ProductSKUDao;
import com.store.system.dao.ProductSPUDao;
import com.store.system.exception.GlassesException;
import com.store.system.model.ProductPropertyName;
import com.store.system.model.ProductSKU;
import com.store.system.model.ProductSPU;
import com.store.system.service.ProductService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    private TransformFieldSetUtils nameFieldSetUtils = new TransformFieldSetUtils(ProductPropertyName.class);

    private RowMapperHelp<ProductSPU> spuRowMapper = new RowMapperHelp<>(ProductSPU.class);

    @Resource
    private ProductPropertyNameDao productPropertyNameDao;

    @Resource
    private ProductSPUDao productSPUDao;

    @Resource
    private ProductSKUDao productSKUDao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    private void checkSPU(ProductSPU productSPU) throws GlassesException {
        String propertyJson = productSPU.getPropertyJson();
        Map<Long, Object> propertyMap = null;
        try {
            propertyMap = JsonUtils.fromJson(propertyJson, new TypeReference<Map<Long, Object>>() {});
        } catch (Exception e) {
            throw new GlassesException("SPU属性格式错误");
        }

        if(productSPU.getSubid() == 0) throw new GlassesException("SPU店铺不能为空");
        if(productSPU.getCid() == 0) throw new GlassesException("SPU类目不能为空");
        if(productSPU.getBid() == 0) throw new GlassesException("SPU品牌不能为空");

        long cid = productSPU.getCid();
        List<ProductPropertyName> spuNames = productPropertyNameDao.getAllList(cid, ProductPropertyName.status_nomore);
        for(Iterator<ProductPropertyName> it = spuNames.iterator(); it.hasNext();) {
            ProductPropertyName name = it.next();
            if(name.getMultiple() == ProductPropertyName.multiple_yes) it.remove();
        }
        Set<Long> nameIds = nameFieldSetUtils.fieldList(spuNames, "id");
        for(long nameId : nameIds) {
            if(!propertyMap.keySet().contains(nameId)) throw new GlassesException("SPU缺少关键属性");
        }
    }

    @Override
    public boolean updateSPU(ProductSPU productSPU) throws Exception {
        checkSPU(productSPU);
        boolean res = productSPUDao.update(productSPU);
        return res;
    }

    @Override
    public boolean updateSKU(ProductSKU productSKU) throws Exception {
        checkSKU(productSKU);
        boolean res = productSKUDao.update(productSKU);
        return res;
    }

    @Override
    public ProductSPU addSPU(ProductSPU productSPU) throws Exception {
        checkSPU(productSPU);
        productSPU = productSPUDao.insert(productSPU);
        return productSPU;
    }

    private void checkSKU(ProductSKU productSKU) throws GlassesException {
        String propertyJson = productSKU.getPropertyJson();
        Map<Long, Object> propertyMap = null;
        try {
            propertyMap = JsonUtils.fromJson(propertyJson, new TypeReference<Map<Long, Object>>() {});
        } catch (Exception e) {
            throw new GlassesException("SKU属性格式错误");
        }

        if(productSKU.getSpuid() == 0) throw new GlassesException("SPU不能为空");

        ProductSPU productSPU = productSPUDao.load(productSKU.getSpuid());
        long cid = productSPU.getCid();
        List<ProductPropertyName> skuNames = productPropertyNameDao.getAllList(cid, ProductPropertyName.status_nomore);
        for(Iterator<ProductPropertyName> it = skuNames.iterator(); it.hasNext();) {
            ProductPropertyName name = it.next();
            if(name.getMultiple() == ProductPropertyName.multiple_no) it.remove();
        }
        Set<Long> nameIds = nameFieldSetUtils.fieldList(skuNames, "id");
        for(long nameId : nameIds) {
            if(!propertyMap.keySet().contains(nameId)) throw new GlassesException("SKU缺少关键属性");
        }
    }

    @Override
    public ProductSKU addSKU(ProductSKU productSKU) throws Exception {
        checkSKU(productSKU);
        productSKU = productSKUDao.insert(productSKU);
        return productSKU;
    }

    @Override
    public void change(ProductSPU productSPU, List<ProductSKU> addProductSKUList, List<ProductSKU> updateProductSKUList,
                       List<Long> delSkuids) throws Exception {
        List<ProductSKU> productSKUList = Lists.newArrayList(addProductSKUList);
        productSKUList.addAll(updateProductSKUList);
        check(productSPU, productSKUList);
        productSPUDao.update(productSPU);

        long spuid = productSPU.getId();
        if(addProductSKUList.size() > 0) {
            for(ProductSKU productSKU : addProductSKUList) {
                productSKU.setSpuid(spuid);
                productSKUDao.insert(productSKU);
            }
        }

        if(updateProductSKUList.size() > 0) {
            for(ProductSKU productSKU : updateProductSKUList) {
                productSKUDao.update(productSKU);
            }
        }

        if(delSkuids.size() > 0) {
            List<ProductSKU> delProductSKUList = productSKUDao.load(delSkuids);
            for(ProductSKU productSKU : delProductSKUList) {
                if(null != productSKU) {
                    productSKU.setStatus(ProductSKU.status_delete);
                    productSKUDao.update(productSKU);
                }
            }
        }
    }

    private void check(ProductSPU productSPU, List<ProductSKU> productSKUList) throws GlassesException {
        String propertyJson = productSPU.getPropertyJson();
        Map<Long, Object> propertyMap = null;
        try {
            propertyMap = JsonUtils.fromJson(propertyJson, new TypeReference<Map<Long, Object>>() {});
        } catch (Exception e) {
            throw new GlassesException("SPU属性格式错误");
        }

        if(productSPU.getSubid() == 0) throw new GlassesException("SPU店铺不能为空");
        if(productSPU.getCid() == 0) throw new GlassesException("SPU类目不能为空");
        if(productSPU.getBid() == 0) throw new GlassesException("SPU品牌不能为空");

        long cid = productSPU.getCid();
        List<ProductPropertyName> names = productPropertyNameDao.getAllList(cid, ProductPropertyName.status_nomore);
        Set<Long> spuNames = Sets.newHashSet();
        Set<Long> skuNames = Sets.newHashSet();
        for(ProductPropertyName name : names) {
            if(name.getMultiple() == ProductPropertyName.multiple_no) spuNames.add(name.getId());
            else skuNames.add(name.getId());
        }

        for(long nameId : spuNames) {
            if(!propertyMap.keySet().contains(nameId)) throw new GlassesException("SPU缺少关键属性");
        }

        for(ProductSKU productSKU : productSKUList) {
            propertyJson = productSKU.getPropertyJson();
            propertyMap = null;
            try {
                propertyMap = JsonUtils.fromJson(propertyJson, new TypeReference<Map<Long, Object>>() {});
            } catch (Exception e) {
                throw new GlassesException("SKU属性格式错误");
            }
            for(long nameId : skuNames) {
                if(!propertyMap.keySet().contains(nameId)) throw new GlassesException("SKU缺少关键属性");
            }
        }
    }

    @Override
    public void add(ProductSPU productSPU, List<ProductSKU> productSKUList) throws Exception {
        check(productSPU, productSKUList);
        productSPU = productSPUDao.insert(productSPU);
        if(null == productSPU) throw new GlassesException("SPU添加错误");
        long spuid = productSPU.getId();
        for(ProductSKU productSKU : productSKUList) {
            productSKU.setSpuid(spuid);
            productSKUDao.insert(productSKU);
        }
    }

    @Override
    public ClientProductSPU loadSPU(long id) throws Exception {
        ProductSPU productSPU = productSPUDao.load(id);
        ClientProductSPU clientProductSPU = new ClientProductSPU(productSPU);
        List<ProductSKU> productSKUList = productSKUDao.getAllList(id, ProductSKU.status_nomore);
        List<ClientProductSKU> skuList = Lists.newArrayList();
        for(ProductSKU one : productSKUList) {
            ClientProductSKU clientProductSKU = new ClientProductSKU(one);
            skuList.add(clientProductSKU);
        }
        clientProductSPU.setSkuList(skuList);
        return clientProductSPU;
    }

    @Override
    public boolean delSKU(long id) throws Exception {
        ProductSKU productSKU = productSKUDao.load(id);
        if(null != productSKU) {
            productSKU.setStatus(ProductSKU.status_delete);
            return productSKUDao.update(productSKU);
        }
        return false;
     }

    @Override
    public boolean delSPU(long id) throws Exception {
        ProductSPU productSPU = productSPUDao.load(id);
        if(null != productSPU) {
            productSPU.setStatus(ProductSPU.status_delete);
            return productSPUDao.update(productSPU);
        }
        return false;
    }

    @Override
    public Pager getBackPager(Pager pager, long subid, long cid, long pid, long bid, long sid) throws Exception {
        String sql = "SELECT * FROM `product_spu` where `status` = " + ProductSPU.status_nomore;
        String sqlCount = "SELECT COUNT(id) FROM `product_spu` where `status` = " + ProductSPU.status_nomore;
        String limit = " limit %d , %d ";
        if(subid > 0) {
            sql += " and subid = " + subid;
            sqlCount += " and subid = " + subid;
        }
        if(cid > 0) {
            sql += " and cid = " + cid;
            sqlCount += " and cid = " + cid;
        }
        if(pid > 0) {
            sql += " and pid = " + pid;
            sqlCount += " and pid = " + pid;
        }
        if(bid > 0) {
            sql += " and bid = " + bid;
            sqlCount += " and bid = " + bid;
        }
        if(sid > 0) {
            sql += " and sid = " + sid;
            sqlCount += " and sid = " + sid;
        }
        sql = sql + " order  by `sort` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<ProductSPU> productSPUList = this.jdbcTemplate.query(sql, spuRowMapper);
        int count = this.jdbcTemplate.queryForInt(sqlCount);
        List<ClientProductSPU> data = Lists.newArrayList();
        for(ProductSPU one : productSPUList) {
            ClientProductSPU clientProductSPU = new ClientProductSPU(one);
            data.add(clientProductSPU);
        }
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

}