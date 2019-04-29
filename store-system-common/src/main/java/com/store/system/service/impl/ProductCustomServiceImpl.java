package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.client.ClientProductCustom;
import com.store.system.dao.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.ProductCustomService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProductCustomServiceImpl implements ProductCustomService {

    @Resource
    private ProductCustomDao productCustomDao;

    @Resource
    private SubordinateDao subordinateDao;

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

    private RowMapperHelp<ProductCustom> rowMapper = new RowMapperHelp<>(ProductCustom.class);

    private TransformFieldSetUtils spuFieldSetUtils = new TransformFieldSetUtils(ProductSPU.class);

    private TransformMapUtils subMapUtils = new TransformMapUtils(Subordinate.class);

    private TransformMapUtils spuMapUtils = new TransformMapUtils(ProductSPU.class);

    private TransformMapUtils skuMapUtils = new TransformMapUtils(ProductSKU.class);

    private TransformMapUtils providerMapUtils = new TransformMapUtils(ProductProvider.class);

    private TransformMapUtils categoryMapUtils = new TransformMapUtils(ProductCategory.class);

    private TransformMapUtils brandMapUtils = new TransformMapUtils(ProductBrand.class);

    private TransformMapUtils seriesMapUtils = new TransformMapUtils(ProductSeries.class);

    private void check(ProductCustom productCustom) throws StoreSystemException {
        if(productCustom.getPsid() == 0) throw new StoreSystemException("公司ID错误");
        if(productCustom.getSid() == 0) throw new StoreSystemException("分店ID错误");
        if(productCustom.getSpuid() == 0) throw new StoreSystemException("SPU错误");
        if(productCustom.getSkuid() == 0) throw new StoreSystemException("SKU错误");
        if(productCustom.getNum() == 0) throw new StoreSystemException("定制数量错误");
        if(productCustom.getPrice() == 0) throw new StoreSystemException("定制单价错误");
    }

    @Override
    public ProductCustom add(ProductCustom productCustom) throws Exception {
        check(productCustom);
        productCustom = productCustomDao.insert(productCustom);
        return productCustom;
    }

    @Override
    public boolean updateStatus(long id, int status) throws Exception {
        ProductCustom productCustom = productCustomDao.load(id);
        if(productCustom != null) {
            productCustom.setStatus(status);
            return productCustomDao.update(productCustom);
        }
        return false;
    }

    private List<ClientProductCustom> transformClients(List<ProductCustom> productCustoms) throws Exception {
        List<ClientProductCustom> res = Lists.newArrayList();
        Set<Long> subids = Sets.newHashSet();
        Set<Long> spuids = Sets.newHashSet();
        Set<Long> skuids = Sets.newHashSet();
        for(ProductCustom productCustom : productCustoms) {
            if(productCustom.getPsid() > 0) subids.add(productCustom.getPsid());
            if(productCustom.getSid() > 0) subids.add(productCustom.getSid());
            spuids.add(productCustom.getSpuid());
            skuids.add(productCustom.getSkuid());
        }
        List<Subordinate> subordinates = subordinateDao.load(Lists.newArrayList(subids));
        Map<Long, Subordinate> subordinateMap = subMapUtils.listToMap(subordinates, "id");

        List<ProductSPU> spus = productSPUDao.load(Lists.newArrayList(spuids));
        Map<Long, ProductSPU> spuMap = spuMapUtils.listToMap(spus, "id");

        List<ProductSKU> skus = productSKUDao.load(Lists.newArrayList(skuids));
        Map<Long, ProductSKU> skuMap = skuMapUtils.listToMap(skus, "id");

        Set<Long> pids = spuFieldSetUtils.fieldList(spus, "pid");
        List<ProductProvider> providers = productProviderDao.load(Lists.newArrayList(pids));
        Map<Long, ProductProvider> providerMap = providerMapUtils.listToMap(providers, "id");

        Set<Long> cids = spuFieldSetUtils.fieldList(spus, "cid");
        List<ProductCategory> categories = productCategoryDao.load(Lists.newArrayList(cids));
        Map<Long, ProductCategory> categoryMap = categoryMapUtils.listToMap(categories, "id");

        Set<Long> bids = spuFieldSetUtils.fieldList(spus, "bid");
        List<ProductBrand> brands = productBrandDao.load(Lists.newArrayList(bids));
        Map<Long, ProductBrand> brandMap = brandMapUtils.listToMap(brands, "id");

        Set<Long> sids = spuFieldSetUtils.fieldList(spus, "sid");
        List<ProductSeries> seriesList = productSeriesDao.load(Lists.newArrayList(sids));
        Map<Long, ProductSeries> seriesMap = seriesMapUtils.listToMap(seriesList, "id");

        for(ProductCustom one : productCustoms) {
            ClientProductCustom clientProductCustom = new ClientProductCustom(one);
            Subordinate subordinate = subordinateMap.get(clientProductCustom.getPsid());
            if(subordinate != null) clientProductCustom.setPSubName(subordinate.getName());
            subordinate = subordinateMap.get(clientProductCustom.getSid());
            if(subordinate != null) clientProductCustom.setSubName(subordinate.getName());
            ProductSPU spu = spuMap.get(clientProductCustom.getSpuid());
            if(spu != null) {
                clientProductCustom.setName(spu.getName());
                ProductProvider provider = providerMap.get(spu.getPid());
                if(provider != null) clientProductCustom.setProviderName(provider.getName());
                ProductCategory category = categoryMap.get(spu.getCid());
                if(category != null) clientProductCustom.setCategoryName(category.getName());
                ProductBrand brand = brandMap.get(spu.getBid());
                if(brand != null) clientProductCustom.setBrandName(brand.getName());
                ProductSeries series = seriesMap.get(spu.getSid());
                if(series != null) clientProductCustom.setSeriesName(series.getName());
            }
            ProductSKU sku = skuMap.get(clientProductCustom.getSkuid());
            if(sku != null) {
                clientProductCustom.setCode(sku.getCode());
                clientProductCustom.setProperties(sku.getProperties());
            }
            res.add(clientProductCustom);
        }
        return res;
    }

    @Override
    public Pager getBackPager(Pager pager, long psid, int status) throws Exception {
        String sql = "SELECT * FROM `product_custom` where psid = " + psid + " and `status` = " + status;
        String sqlCount = "SELECT COUNT(id) FROM `product_custom` where psid = " + psid + " and `status` = " + status;
        String limit = " limit %d , %d ";
        sql = sql + " order  by `ctime` asc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<ProductCustom> productCustoms = this.jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        List<ClientProductCustom> data = transformClients(productCustoms);
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public Pager getBackSubPager(Pager pager, long sid, int status) throws Exception {
        String sql = "SELECT * FROM `product_custom` where sid = " + sid + " and `status` = " + status;
        String sqlCount = "SELECT COUNT(id) FROM `product_custom` where sid = " + sid + " and `status` = " + status;
        String limit = " limit %d , %d ";
        sql = sql + " order  by `ctime` asc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<ProductCustom> productCustoms = this.jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        List<ClientProductCustom> data = transformClients(productCustoms);
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }
}
