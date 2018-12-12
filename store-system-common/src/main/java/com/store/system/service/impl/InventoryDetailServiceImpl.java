package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.s7.baseFramework.model.pagination.Pager;
import com.s7.baseFramework.transform.TransformFieldSetUtils;
import com.s7.baseFramework.transform.TransformMapUtils;
import com.s7.ext.RowMapperHelp;
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

    private RowMapperHelp<ClientInventoryDetail> rowMapper = new RowMapperHelp<>(ClientInventoryDetail.class);

    private TransformFieldSetUtils fieldSetUtils = new TransformFieldSetUtils(ClientInventoryDetail.class);

    private TransformMapUtils warehouseMapUtils = new TransformMapUtils(InventoryWarehouse.class);

    private TransformMapUtils providerMapUtils = new TransformMapUtils(ProductProvider.class);

    private TransformMapUtils categoryMapUtils = new TransformMapUtils(ProductCategory.class);

    private TransformMapUtils brandMapUtils = new TransformMapUtils(ProductBrand.class);

    private TransformMapUtils seriesMapUtils = new TransformMapUtils(ProductSeries.class);

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
    private JdbcTemplate jdbcTemplate;

    @Override
    public Pager getPager(Pager pager, long wid, long cid) throws Exception {
        String sql = "select d.id, d.wid, spu.type as p_type, spu.pid as p_pid, spu.cid as p_cid, spu.bid as p_bid, spu.sid as p_sid, " +
                     "spu.name as p_name, spu.id as p_spuid, sku.id as p_skuid, sku.code as p_code, sku.propertyJson as p_propertyJson, " +
                     "sku.retailPrice as p_retailPrice, sku.costPrice as p_costPrice, sku.integralPrice as p_integralPrice, d.num, d.ctime from " +
                     "inventory_detail as d, product_sku as sku, product_spu as spu where d.p_skuid = sku.id and d.p_spuid = spu.id";
        String sqlCount = "select count(d.id) from inventory_detail as d, product_sku as sku," +
                          "product_spu as spu where d.p_skuid = sku.id and d.p_spuid = spu.id ";
        if(wid > 0) {
            sql = sql + " and d.wid = " + wid;
            sqlCount = sqlCount + " and d.wid = " + wid;
        }
        if(cid > 0) {
            sql = sql + " and spu.cid = " + cid;
            sqlCount = sqlCount + " and spu.cid = " + cid;
        }
        String limit = " limit %d , %d ";
        sql = sql + " order by d.ctime desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<ClientInventoryDetail> details = this.jdbcTemplate.query(sql, rowMapper);

        Set<Long> wids = fieldSetUtils.fieldList(details, "wid");
        List<InventoryWarehouse> warehouses = inventoryWarehouseDao.load(Lists.newArrayList(wids));
        Map<Long, InventoryWarehouse> warehouseMap = warehouseMapUtils.listToMap(warehouses, "id");

        Set<Long> p_pids = fieldSetUtils.fieldList(details, "p_pid");
        List<ProductProvider> providers = productProviderDao.load(Lists.newArrayList(p_pids));
        Map<Long, ProductProvider> providerMap = providerMapUtils.listToMap(providers, "id");

        Set<Long> p_cids = fieldSetUtils.fieldList(details, "p_cid");
        List<ProductCategory> categories = productCategoryDao.load(Lists.newArrayList(p_cids));
        Map<Long, ProductCategory> categoryMap = categoryMapUtils.listToMap(categories, "id");

        Set<Long> p_bids = fieldSetUtils.fieldList(details, "p_bid");
        List<ProductBrand> brands = productBrandDao.load(Lists.newArrayList(p_bids));
        Map<Long, ProductBrand> brandMap = brandMapUtils.listToMap(brands, "id");

        Set<Long> p_sids = fieldSetUtils.fieldList(details, "p_sid");
        List<ProductSeries> seriesList = productSeriesDao.load(Lists.newArrayList(p_sids));
        Map<Long, ProductSeries> seriesMap = seriesMapUtils.listToMap(seriesList, "id");

        for(ClientInventoryDetail one : details) {
            InventoryWarehouse warehouse = warehouseMap.get(one.getWid());
            if(null != warehouse) one.setWarehouseName(warehouse.getName());
            ProductProvider provider = providerMap.get(one.getP_pid());
            if(null != provider) one.setProviderName(provider.getName());
            ProductCategory category = categoryMap.get(one.getP_cid());
            if(null != category) one.setCategoryName(category.getName());
            ProductBrand brand = brandMap.get(one.getP_bid());
            if(null != brand) one.setBrandName(brand.getName());
            ProductSeries series = seriesMap.get(one.getP_sid());
            if(null != series) one.setSeriesName(series.getName());
        }

        int count = this.jdbcTemplate.queryForInt(sqlCount);
        pager.setData(details);
        pager.setTotalCount(count);
        return pager;
    }

}
