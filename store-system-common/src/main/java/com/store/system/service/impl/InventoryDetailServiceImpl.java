package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.client.ClientInventoryDetail;
import com.store.system.dao.*;
import com.store.system.model.*;
import com.store.system.service.InventoryDetailService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class InventoryDetailServiceImpl implements InventoryDetailService {

    private RowMapperHelp<InventoryDetail> rowMapper = new RowMapperHelp<>(InventoryDetail.class);

    private TransformFieldSetUtils fieldSetUtils = new TransformFieldSetUtils(InventoryDetail.class);

    private TransformFieldSetUtils spuFieldSetUtils = new TransformFieldSetUtils(ProductSPU.class);

    private TransformMapUtils warehouseMapUtils = new TransformMapUtils(InventoryWarehouse.class);

    private TransformMapUtils providerMapUtils = new TransformMapUtils(ProductProvider.class);

    private TransformMapUtils categoryMapUtils = new TransformMapUtils(ProductCategory.class);

    private TransformMapUtils brandMapUtils = new TransformMapUtils(ProductBrand.class);

    private TransformMapUtils seriesMapUtils = new TransformMapUtils(ProductSeries.class);

    private TransformMapUtils spuMapUtils = new TransformMapUtils(ProductSPU.class);

    private TransformMapUtils skuMapUtils = new TransformMapUtils(ProductSKU.class);

    @Resource
    private InventoryWarehouseDao inventoryWarehouseDao;

    @Resource
    private ProductProviderDao productProviderDao;

    @Resource
    private ProductCategoryDao productCategoryDao;

    @Resource
    private ProductBrandDao productBrandDao;

    @Resource
    private ProductSeriesDao productSeriesDao;

    @Resource
    private ProductSPUDao productSPUDao;

    @Resource
    private ProductSKUDao productSKUDao;

    @Resource
    private InventoryDetailDao inventoryDetailDao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    private List<ClientInventoryDetail> transformClients(List<InventoryDetail> details) throws Exception {
        List<ClientInventoryDetail> res = Lists.newArrayList();
        Set<Long> p_spuids = fieldSetUtils.fieldList(details, "p_spuid");
        List<ProductSPU> productSPUList = productSPUDao.load(Lists.newArrayList(p_spuids));
        Map<Long, ProductSPU> spuMap = spuMapUtils.listToMap(productSPUList, "id");

        Set<Long> p_skuids = fieldSetUtils.fieldList(details, "p_skuid");
        List<ProductSKU> productSKUList = productSKUDao.load(Lists.newArrayList(p_skuids));
        Map<Long, ProductSKU> skuMap = skuMapUtils.listToMap(productSKUList, "id");

        Set<Long> wids = fieldSetUtils.fieldList(details, "wid");
        List<InventoryWarehouse> warehouses = inventoryWarehouseDao.load(Lists.newArrayList(wids));
        Map<Long, InventoryWarehouse> warehouseMap = warehouseMapUtils.listToMap(warehouses, "id");

        Set<Long> p_pids = spuFieldSetUtils.fieldList(productSPUList, "pid");
        List<ProductProvider> providers = productProviderDao.load(Lists.newArrayList(p_pids));
        Map<Long, ProductProvider> providerMap = providerMapUtils.listToMap(providers, "id");

        Set<Long> p_cids = spuFieldSetUtils.fieldList(productSPUList, "cid");
        List<ProductCategory> categories = productCategoryDao.load(Lists.newArrayList(p_cids));
        Map<Long, ProductCategory> categoryMap = categoryMapUtils.listToMap(categories, "id");

        Set<Long> p_bids = spuFieldSetUtils.fieldList(productSPUList, "bid");
        List<ProductBrand> brands = productBrandDao.load(Lists.newArrayList(p_bids));
        Map<Long, ProductBrand> brandMap = brandMapUtils.listToMap(brands, "id");

        Set<Long> p_sids = spuFieldSetUtils.fieldList(productSPUList, "sid");
        List<ProductSeries> seriesList = productSeriesDao.load(Lists.newArrayList(p_sids));
        Map<Long, ProductSeries> seriesMap = seriesMapUtils.listToMap(seriesList, "id");

        for(InventoryDetail one : details) {
            ClientInventoryDetail client = new ClientInventoryDetail(one);
            InventoryWarehouse warehouse = warehouseMap.get(one.getWid());
            if(null != warehouse) client.setWarehouseName(warehouse.getName());
            ProductSPU spu = spuMap.get(one.getP_spuid());
            if(null != spu) {
                client.setP_type(spu.getType());
                client.setP_pid(spu.getPid());
                ProductProvider provider = providerMap.get(spu.getPid());
                if(null != provider) client.setProviderName(provider.getName());
                ProductCategory category = categoryMap.get(spu.getCid());
                if(null != category) client.setCategoryName(category.getName());
                client.setP_bid(spu.getBid());
                ProductBrand brand = brandMap.get(spu.getBid());
                if(null != brand) client.setBrandName(brand.getName());
                client.setP_sid(spu.getSid());
                ProductSeries series = seriesMap.get(spu.getSid());
                if(null != series) client.setSeriesName(series.getName());
                client.setP_name(spu.getName());
            }
            ProductSKU sku = skuMap.get(one.getP_skuid());
            if(null != sku) {
                client.setP_code(sku.getCode());
                client.setP_properties(sku.getProperties());
                client.setP_retailPrice(sku.getRetailPrice());
                client.setP_costPrice(sku.getCostPrice());
                client.setP_integralPrice(sku.getIntegralPrice());
            }
            res.add(client);
        }
        return res;
    }

    @Override
    public Pager getPager(Pager pager, long wid, long cid) throws Exception {
        String sql = "select * from inventory_detail where 1 = 1 ";
        String sqlCount = "select count(id) from inventory_detail where 1 = 1 ";
        if(wid > 0) {
            sql = sql + " and wid = " + wid;
            sqlCount = sqlCount + " and wid = " + wid;
        }
        if(cid > 0) {
            sql = sql + " and cid = " + cid;
            sqlCount = sqlCount + " and cid = " + cid;
        }
        String limit = " limit %d , %d ";
        sql = sql + " order by ctime desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<InventoryDetail> details = this.jdbcTemplate.query(sql, rowMapper);
        List<ClientInventoryDetail> data = transformClients(details);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public List<ClientInventoryDetail> getAllList(long wid, long p_spuid) throws Exception {
        List<InventoryDetail> details = inventoryDetailDao.getAllListByWidAndSPU(wid, p_spuid);
        return transformClients(details);
    }

    @Override
    public List<InventoryDetail> getAllOriginList(long wid, long p_spuid) throws Exception {
        List<InventoryDetail> details = inventoryDetailDao.getAllListByWidAndSPU(wid, p_spuid);
        return details;
    }

}
