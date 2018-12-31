package com.store.system.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.client.ClientProductSKU;
import com.store.system.client.ClientProductSPU;
import com.store.system.dao.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.ProductService;
import org.apache.commons.lang3.StringUtils;
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
    private ProductProviderDao productProviderDao;

    @Resource
    private ProductCategoryDao productCategoryDao;

    @Resource
    private ProductBrandDao productBrandDao;

    @Resource
    private ProductSeriesDao productSeriesDao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    private void checkSPU(ProductSPU productSPU) throws StoreSystemException {
        String propertyJson = productSPU.getPropertyJson();
        Map<Long, Object> propertyMap = null;
        try {
            propertyMap = JsonUtils.fromJson(propertyJson, new TypeReference<Map<Long, Object>>() {});
        } catch (Exception e) {
            throw new StoreSystemException("SPU属性格式错误");
        }

        int type = productSPU.getType();
        long subid = productSPU.getSubid();
        long pid = productSPU.getPid();
        long cid = productSPU.getCid();
        long bid = productSPU.getBid();
        long sid = productSPU.getSid();
        if(subid == 0) throw new StoreSystemException("SPU店铺不能为空");
        if(cid == 0) throw new StoreSystemException("SPU类目不能为空");
        if(bid == 0) throw new StoreSystemException("SPU品牌不能为空");

        int count = productSPUDao.getCount(type, subid, pid, cid, bid, sid);
        if(count > 0) throw new StoreSystemException("已添加此产品的SPU");


        List<ProductPropertyName> spuNames = productPropertyNameDao.getAllList(cid, ProductPropertyName.status_nomore);
        for(Iterator<ProductPropertyName> it = spuNames.iterator(); it.hasNext();) {
            ProductPropertyName name = it.next();
            if(name.getMultiple() != ProductPropertyName.type_spu) it.remove();
        }
        Set<Long> nameIds = nameFieldSetUtils.fieldList(spuNames, "id");
        for(long nameId : nameIds) {
            if(!propertyMap.keySet().contains(nameId)) throw new StoreSystemException("SPU缺少关键属性");
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

    private void checkSKU(ProductSKU productSKU) throws StoreSystemException {
        String propertyJson = productSKU.getPropertyJson();
        Map<Long, Object> propertyMap = null;
        try {
            propertyMap = JsonUtils.fromJson(propertyJson, new TypeReference<Map<Long, Object>>() {});
        } catch (Exception e) {
            throw new StoreSystemException("SKU属性格式错误");
        }

        long spuid = productSKU.getSpuid();
        String code = productSKU.getCode();
        if(spuid == 0) throw new StoreSystemException("SPU不能为空");
        if(StringUtils.isBlank(code)) throw new StoreSystemException("产品编码不能为空");

        int count = productSKUDao.getCount(spuid, code);
        if(count > 0) throw new StoreSystemException("已添加此产品的SKU");

        ProductSPU productSPU = productSPUDao.load(productSKU.getSpuid());
        long cid = productSPU.getCid();
        List<ProductPropertyName> skuNames = productPropertyNameDao.getAllList(cid, ProductPropertyName.status_nomore);
        for(Iterator<ProductPropertyName> it = skuNames.iterator(); it.hasNext();) {
            ProductPropertyName name = it.next();
            if(name.getMultiple() != ProductPropertyName.type_sku) it.remove();
        }
        Set<Long> nameIds = nameFieldSetUtils.fieldList(skuNames, "id");
        for(long nameId : nameIds) {
            if(!propertyMap.keySet().contains(nameId)) throw new StoreSystemException("SKU缺少关键属性");
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

    private void check(ProductSPU productSPU, List<ProductSKU> productSKUList) throws StoreSystemException {
        String propertyJson = productSPU.getPropertyJson();
        Map<Long, Object> propertyMap = null;
        try {
            propertyMap = JsonUtils.fromJson(propertyJson, new TypeReference<Map<Long, Object>>() {});
        } catch (Exception e) {
            throw new StoreSystemException("SPU属性格式错误");
        }

        if(productSPU.getSubid() == 0) throw new StoreSystemException("SPU店铺不能为空");
        if(productSPU.getCid() == 0) throw new StoreSystemException("SPU类目不能为空");
        if(productSPU.getBid() == 0) throw new StoreSystemException("SPU品牌不能为空");

        long cid = productSPU.getCid();
        List<ProductPropertyName> names = productPropertyNameDao.getAllList(cid, ProductPropertyName.status_nomore);
        Set<Long> spuNames = Sets.newHashSet();
        Set<Long> skuNames = Sets.newHashSet();
        for(ProductPropertyName name : names) {
            if(name.getMultiple() == ProductPropertyName.type_spu) spuNames.add(name.getId());
            else skuNames.add(name.getId());
        }

        for(long nameId : spuNames) {
            if(!propertyMap.keySet().contains(nameId)) throw new StoreSystemException("SPU缺少关键属性");
        }

        for(ProductSKU productSKU : productSKUList) {
            propertyJson = productSKU.getPropertyJson();
            propertyMap = null;
            try {
                propertyMap = JsonUtils.fromJson(propertyJson, new TypeReference<Map<Long, Object>>() {});
            } catch (Exception e) {
                throw new StoreSystemException("SKU属性格式错误");
            }
            for(long nameId : skuNames) {
                if(!propertyMap.keySet().contains(nameId)) throw new StoreSystemException("SKU缺少关键属性");
            }
        }
    }

    @Override
    public void add(ProductSPU productSPU, List<ProductSKU> productSKUList) throws Exception {
        check(productSPU, productSKUList);
        productSPU = productSPUDao.insert(productSPU);
        if(null == productSPU) throw new StoreSystemException("SPU添加错误");
        long spuid = productSPU.getId();
        for(ProductSKU productSKU : productSKUList) {
            productSKU.setSpuid(spuid);
            productSKUDao.insert(productSKU);
        }
    }

    @Override
    public ClientProductSPU loadSPU(long id) throws Exception {
        ProductSPU productSPU = productSPUDao.load(id);
        ClientProductSPU clientProductSPU = transformClient(productSPU);
        List<ProductSKU> productSKUList = productSKUDao.getAllList(id, ProductSKU.status_nomore);
        List<ClientProductSKU> skuList = Lists.newArrayList();
        for(ProductSKU one : productSKUList) {
            ClientProductSKU clientProductSKU = new ClientProductSKU(one);
            skuList.add(clientProductSKU);
        }
        clientProductSPU.setSkuList(skuList);
        return clientProductSPU;
    }

    private ClientProductSPU transformClient(ProductSPU productSPU) throws Exception {
        ClientProductSPU clientProductSPU = new ClientProductSPU(productSPU);
        ProductProvider provider = productProviderDao.load(clientProductSPU.getPid());
        if(null != provider) clientProductSPU.setProviderName(provider.getName());
        ProductCategory category = productCategoryDao.load(clientProductSPU.getCid());
        if(null != category) clientProductSPU.setCategoryName(category.getName());
        ProductBrand brand = productBrandDao.load(clientProductSPU.getBid());
        if(null != brand) clientProductSPU.setBrandName(brand.getName());
        ProductSeries series = productSeriesDao.load(clientProductSPU.getSid());
        if(null != series) clientProductSPU.setSeriesName(series.getName());
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
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        List<ClientProductSPU> data = Lists.newArrayList();
        for(ProductSPU one : productSPUList) {
            ClientProductSPU clientProductSPU = new ClientProductSPU(one);
            data.add(clientProductSPU);
        }
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public ClientProductSPU selectSPU(int type, long subid, long pid, long cid, long bid, long sid) throws Exception {
        int count = productSPUDao.getCount(type, subid, pid, cid, bid, sid);
        if(count == 0) throw new StoreSystemException("没有此类产品");
        List<ProductSPU> productSPUList = productSPUDao.getAllList(type, subid, pid, cid, bid, sid);
        ProductSPU productSPU = productSPUList.get(0);
        ClientProductSPU clientProductSPU = transformClient(productSPU);
        return clientProductSPU;
    }

}
