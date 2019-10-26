package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.model.pagination.PagerSession;
import com.quakoo.baseFramework.model.pagination.service.PagerRequestService;
import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.quakoo.ext.RowMapperHelp;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.store.system.client.ClientInventoryDetail;
import com.store.system.dao.*;
import com.store.system.model.*;
import com.store.system.service.InventoryDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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

    private TransformMapUtils nameMapUtils = new TransformMapUtils(ProductPropertyName.class);

    private TransformMapUtils valueMapUtils = new TransformMapUtils(ProductPropertyValue.class);

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
    private ProductPropertyNameDao productPropertyNameDao;

    @Resource
    private ProductPropertyValueDao productPropertyValueDao;

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
            Map<Long,Object> map = sku.getProperties();
            Map<Object,Object> map_value = Maps.newHashMap();
            if (null != sku) {
                client.setP_code(sku.getCode());
                client.setP_properties(sku.getProperties());
                client.setP_retailPrice(sku.getRetailPrice());
                client.setP_costPrice(sku.getCostPrice());
                client.setP_integralPrice(sku.getIntegralPrice());

                Set<Long> keys = map.keySet();
                List<ProductPropertyName> names = productPropertyNameDao.load(Lists.newArrayList(keys));
                Map<Long, ProductPropertyName> nameMap = nameMapUtils.listToMap(names, "id");
                //如果是输入属性，则直接封装value返回
                //如果是非输入属性，则根据ID从value表中查找到content封装返回
                for (Map.Entry<Long, ProductPropertyName> entry : nameMap.entrySet()) {
                    if (entry.getValue().getInput() == ProductPropertyName.input_no) {
                        Set<Long> values = Sets.newHashSet();
                        for (Object object : map.values()) {
                            values.add(Long.parseLong(object.toString()));
                        }
                        List<ProductPropertyValue> valueList = productPropertyValueDao.load(Lists.newArrayList(values));
                        Map<Long, ProductPropertyValue> valueMap = valueMapUtils.listToMap(valueList, "id");

                        for (Map.Entry<Long, Object> skuEntry : map.entrySet()) {
                            map_value.put(nameMap.get(skuEntry.getKey()).getContent(), valueMap.get(Long.parseLong(skuEntry.getValue().toString())).getContent());
                        }
                        client.setP_properties_value(map_value);
                    } else {
                        for (Map.Entry<Long, Object> skuEntry : map.entrySet()) {
                            map_value.put(nameMap.get(skuEntry.getKey()).getContent(), skuEntry.getValue());
                        }
                        client.setP_properties_value(map_value);
                    }

                }
            }
            res.add(client);
        }
        return res;
    }

    @Override
    public Pager getBackendPager(Pager pager, long wid, long cid) throws Exception {
        String sql = "select * from inventory_detail where 1 = 1 ";
        String sqlCount = "select count(id) from inventory_detail where 1 = 1 ";
        if(wid > 0) {
            sql = sql + " and wid = " + wid;
            sqlCount = sqlCount + " and wid = " + wid;
        }
        if(cid > 0) {
            sql = sql + " and p_cid = " + cid;
            sqlCount = sqlCount + " and p_cid = " + cid;
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
    public Pager getPager(final Pager pager, final long wid, final long cid) throws Exception {
        return new PagerRequestService<InventoryDetail>(pager, 0) {
            @Override
            public List<InventoryDetail> step1GetPageResult(String cursor, int size) throws Exception {
                return inventoryDetailDao.getPageList(wid,cid,Double.parseDouble(cursor),size);
            }

            @Override
            public int step2GetTotalCount() throws Exception {
                return inventoryDetailDao.getCount(wid,cid);
            }

            @Override
            public List<InventoryDetail> step3FilterResult(List<InventoryDetail> unTransformDatas, PagerSession session) throws Exception {
                return unTransformDatas;
            }

            @Override
            public List<?> step4TransformData(List<InventoryDetail> unTransformDatas, PagerSession session) throws Exception {

                return transformClients(unTransformDatas);
            }
        }.getPager();
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

    @Override
    public List<ClientInventoryDetail> getAllList(long subid) throws Exception {
        List<InventoryDetail> details = inventoryDetailDao.getAllListBySubId(subid);
        return transformClients(details);
    }

    @Override
    public List<ClientInventoryDetail> getWaringList(long wid, long cid) throws Exception {
        List<InventoryDetail> details = inventoryDetailDao.getAllListByWidAndCid(wid, cid);
        Set<Long> p_spuids = fieldSetUtils.fieldList(details, "p_spuid");
        List<ProductSPU> productSPUList = productSPUDao.load(Lists.newArrayList(p_spuids));
        Map<Long, ProductSPU> spuMap = spuMapUtils.listToMap(productSPUList, "id");
        List<InventoryDetail> res = Lists.newArrayList();
        //sku库存数量低于预警设置数量
        for (InventoryDetail detail : details) {
            ProductSPU spu = spuMap.get(detail.getP_spuid());
            if (detail.getNum() <= spu.getUnderRemind()) {
                res.add(detail);
            }
        }
        return transformClients(res);
    }

    @Override
    public List<ClientInventoryDetail> getExpireList(long wid, long cid) throws Exception {
        List<InventoryDetail> details = inventoryDetailDao.getAllListByWidAndCid(wid, cid);
        Set<Long> p_skuids = fieldSetUtils.fieldList(details, "p_skuid");
        List<ProductSKU> productSKUList = productSKUDao.load(Lists.newArrayList(p_skuids));
        Map<Long, ProductSKU> skuMap = skuMapUtils.listToMap(productSKUList, "id");

        List<InventoryDetail> res = Lists.newArrayList();
        //sku保质期
        for (InventoryDetail detail : details) {
            ProductSKU sku = skuMap.get(detail.getP_skuid());
            if(sku!=null) {
                long current = System.currentTimeMillis();
                //护理产品的保质期结束时间
                long nurseEndTime=0;
                long endTime=0;

                if(sku.getProperties().containsKey(33L)){
                    nurseEndTime = Long.parseLong((String) sku.getProperties().get(33L));
                }
                //隐形眼镜的保质期
                if(sku.getProperties().containsKey(35L)) {
                    endTime = Long.parseLong((String)  sku.getProperties().get(35L));
                }
                //若到期时间在三天之内，则为到期产品
                if (nurseEndTime - current <= 3 * 60 * 60 * 24 * 1000) {
                    res.add(detail);
                } else if (endTime - current <= 3 * 60 * 60 * 24 * 1000) {
                    res.add(detail);
                }
            }
        }
        return transformClients(res);
    }


}
