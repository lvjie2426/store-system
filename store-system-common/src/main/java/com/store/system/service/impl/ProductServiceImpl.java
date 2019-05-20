package com.store.system.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.client.ClientInventoryDetail;
import com.store.system.client.ClientInventoryWarehouse;
import com.store.system.client.ClientProductSKU;
import com.store.system.client.ClientProductSPU;
import com.store.system.dao.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.ProductService;
import com.store.system.service.UserGradeCategoryDiscountService;
import com.store.system.util.ArithUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private TransformFieldSetUtils nameFieldSetUtils = new TransformFieldSetUtils(ProductPropertyName.class);

    private RowMapperHelp<ProductSPU> spuRowMapper = new RowMapperHelp<>(ProductSPU.class);

    private TransformMapUtils providerMapUtils = new TransformMapUtils(ProductProvider.class);

    private TransformMapUtils brandMapUtils = new TransformMapUtils(ProductBrand.class);

    private TransformMapUtils seriesMapUtils = new TransformMapUtils(ProductSeries.class);

    private TransformMapUtils categoryMapUtils = new TransformMapUtils(ProductCategory.class);

    private TransformMapUtils warehouseMapUtils = new TransformMapUtils(InventoryWarehouse.class);

    private TransformMapUtils subordinateMapUtils = new TransformMapUtils(Subordinate.class);

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
    private SubordinateDao subordinateDao;

    @Resource
    private InventoryDetailDao inventoryDetailDao;

    @Resource
    private InventoryWarehouseDao inventoryWarehouseDao;

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private UserGradeCategoryDiscountService userGradeCategoryDiscountService;

    private void checkSPU(ProductSPU productSPU) throws StoreSystemException {
        int type = productSPU.getType();
        long subid = productSPU.getSubid();
        long pid = productSPU.getPid();
        long cid = productSPU.getCid();
        long bid = productSPU.getBid();
        long sid = productSPU.getSid();
        Map<Long, Object> properties = productSPU.getProperties();
        if (subid == 0) throw new StoreSystemException("SPU店铺不能为空");
        if (cid == 0) throw new StoreSystemException("SPU类目不能为空");
        if (bid == 0) throw new StoreSystemException("SPU品牌不能为空");

        int count = productSPUDao.getCount(type, subid, pid, cid, bid, sid);
        if (count > 0) throw new StoreSystemException("已添加此产品的SPU");


        List<ProductPropertyName> spuNames = productPropertyNameDao.getAllList(cid, ProductPropertyName.status_nomore);
        for (Iterator<ProductPropertyName> it = spuNames.iterator(); it.hasNext(); ) {
            ProductPropertyName name = it.next();
            if (name.getType() != ProductPropertyName.type_spu) it.remove();
        }
        Set<Long> nameIds = nameFieldSetUtils.fieldList(spuNames, "id");
        for (long nameId : nameIds) {
            if (!properties.keySet().contains(nameId)) throw new StoreSystemException("SPU缺少关键属性");
        }
    }

    @Override
    public boolean updateSPU(ProductSPU productSPU) throws Exception {
        checkSPU(productSPU);
        Subordinate subordinate = subordinateDao.load(productSPU.getSubid());
        if (subordinate.getPid() > 0) productSPU.setSubid(subordinate.getPid());
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
        Subordinate subordinate = subordinateDao.load(productSPU.getSubid());
        if (subordinate.getPid() > 0) productSPU.setSubid(subordinate.getPid());
        productSPU = productSPUDao.insert(productSPU);
        return productSPU;
    }

    private void checkSKU(ProductSKU productSKU) throws StoreSystemException {
        Map<Long, Object> properties = productSKU.getProperties();
        long spuid = productSKU.getSpuid();
        String code = productSKU.getCode();
        if (spuid == 0) throw new StoreSystemException("SPU不能为空");
        if (StringUtils.isBlank(code)) throw new StoreSystemException("产品编码不能为空");

        int count = productSKUDao.getCount(spuid, code);
        if (count > 0) throw new StoreSystemException("已添加此产品的SKU");

        ProductSPU productSPU = productSPUDao.load(productSKU.getSpuid());
        long cid = productSPU.getCid();
        List<ProductPropertyName> skuNames = productPropertyNameDao.getAllList(cid, ProductPropertyName.status_nomore);
        for (Iterator<ProductPropertyName> it = skuNames.iterator(); it.hasNext(); ) {
            ProductPropertyName name = it.next();
            if (name.getType() != ProductPropertyName.type_sku) it.remove();
        }
        Set<Long> nameIds = nameFieldSetUtils.fieldList(skuNames, "id");
        for (long nameId : nameIds) {
            if (!properties.keySet().contains(nameId)) throw new StoreSystemException("SKU缺少关键属性");
        }
    }

    @Override
    public ProductSKU addSKU(ProductSKU productSKU) throws Exception {
        checkSKU(productSKU);
        productSKU = productSKUDao.insert(productSKU);
        return productSKU;
    }

    @Override
    @Transactional
    public void change(ProductSPU productSPU, List<ProductSKU> addProductSKUList, List<ProductSKU> updateProductSKUList,
                       List<Long> delSkuids) throws Exception {
        List<ProductSKU> productSKUList = Lists.newArrayList(addProductSKUList);
        productSKUList.addAll(updateProductSKUList);
        check(productSPU, productSKUList);
        productSPUDao.update(productSPU);

        long spuid = productSPU.getId();
        if (addProductSKUList.size() > 0) {
            for (ProductSKU productSKU : addProductSKUList) {
                productSKU.setSpuid(spuid);
                productSKUDao.insert(productSKU);
            }
        }

        if (updateProductSKUList.size() > 0) {
            for (ProductSKU productSKU : updateProductSKUList) {
                productSKUDao.update(productSKU);
            }
        }

        if (delSkuids.size() > 0) {
            List<ProductSKU> delProductSKUList = productSKUDao.load(delSkuids);
            for (ProductSKU productSKU : delProductSKUList) {
                if (null != productSKU) {
                    productSKU.setStatus(ProductSKU.status_delete);
                    productSKUDao.update(productSKU);
                }
            }
        }
    }

    private void check(ProductSPU productSPU, List<ProductSKU> productSKUList) throws StoreSystemException {
        Map<Long, Object> properties = productSPU.getProperties();
        if (productSPU.getSubid() == 0) throw new StoreSystemException("SPU店铺不能为空");
        if (productSPU.getCid() == 0) throw new StoreSystemException("SPU类目不能为空");
        if (productSPU.getBid() == 0) throw new StoreSystemException("SPU品牌不能为空");

        int count = productSPUDao.getCount(productSPU.getType(), productSPU.getSubid(), productSPU.getPid(),
                productSPU.getCid(), productSPU.getBid(), productSPU.getSid());
        if (count > 0) throw new StoreSystemException("已添加此产品的SPU");

        long cid = productSPU.getCid();
        List<ProductPropertyName> names = productPropertyNameDao.getAllList(cid, ProductPropertyName.status_nomore);
        Set<Long> spuNames = Sets.newHashSet();
        Set<Long> skuNames = Sets.newHashSet();
        for (ProductPropertyName name : names) {
            if (name.getType() == ProductPropertyName.type_spu) spuNames.add(name.getId());
            else skuNames.add(name.getId());
        }

        for (long nameId : spuNames) {
            if (!properties.keySet().contains(nameId)) throw new StoreSystemException("SPU缺少关键属性");
        }

        for (ProductSKU productSKU : productSKUList) {
            properties = productSKU.getProperties();
            for (long nameId : skuNames) {
                if (!properties.keySet().contains(nameId)) throw new StoreSystemException("SKU缺少关键属性");
            }
        }
    }

    @Override
    @Transactional
    public void add(ProductSPU productSPU, List<ProductSKU> productSKUList, List<UserGradeCategoryDiscount> ugDiscountList) throws Exception {
        check(productSPU, productSKUList);
        productSPU = productSPUDao.insert(productSPU);
        if (null == productSPU) throw new StoreSystemException("SPU添加错误");
        long spuid = productSPU.getId();
        for (ProductSKU productSKU : productSKUList) {
            productSKU.setSpuid(spuid);
            productSKUDao.insert(productSKU);
        }
        userGradeCategoryDiscountService.addDiscount(ugDiscountList,spuid);

    }

    @Override
    public ClientProductSPU loadSPU(long id) throws Exception {
        ProductSPU productSPU = productSPUDao.load(id);
        ClientProductSPU clientProductSPU = transformClient(productSPU);
        List<ProductSKU> productSKUList = productSKUDao.getAllList(id, ProductSKU.status_nomore);
        List<ClientProductSKU> skuList = Lists.newArrayList();
        for (ProductSKU one : productSKUList) {
            ClientProductSKU clientProductSKU = new ClientProductSKU(one);
            skuList.add(clientProductSKU);
        }
        clientProductSPU.setSkuList(skuList);
        return clientProductSPU;
    }

    private List<ClientProductSPU> transformClients(List<ProductSPU> productSPUList) throws Exception {
        List<ClientProductSPU> res = Lists.newArrayList();
        if (productSPUList.size() == 0) return res;
        Set<Long> pids = Sets.newHashSet();
        Set<Long> cids = Sets.newHashSet();
        Set<Long> bids = Sets.newHashSet();
        Set<Long> sids = Sets.newHashSet();
        for (ProductSPU one : productSPUList) {
            pids.add(one.getPid());
            cids.add(one.getCid());
            bids.add(one.getBid());
            sids.add(one.getSid());
        }
        List<ProductProvider> providers = productProviderDao.load(Lists.newArrayList(pids));
        Map<Long, ProductProvider> providerMap = providerMapUtils.listToMap(providers, "id");
        List<ProductCategory> categories = productCategoryDao.load(Lists.newArrayList(cids));
        Map<Long, ProductCategory> categoryMap = categoryMapUtils.listToMap(categories, "id");
        List<ProductBrand> brands = productBrandDao.load(Lists.newArrayList(bids));
        Map<Long, ProductBrand> brandMap = brandMapUtils.listToMap(brands, "id");
        List<ProductSeries> seriesList = productSeriesDao.load(Lists.newArrayList(sids));
        Map<Long, ProductSeries> seriesMap = seriesMapUtils.listToMap(seriesList, "id");
        for (ProductSPU one : productSPUList) {
            ClientProductSPU client = new ClientProductSPU(one);
            ProductProvider provider = providerMap.get(client.getPid());
            if (null != provider) client.setProviderName(provider.getName());
            ProductCategory category = categoryMap.get(client.getCid());
            if (null != category) client.setCategoryName(category.getName());
            ProductBrand brand = brandMap.get(client.getBid());
            if (null != brand) client.setBrandName(brand.getName());
            ProductSeries series = seriesMap.get(client.getSid());
            if (null != series) client.setSeriesName(series.getName());
            res.add(client);
        }
        return res;
    }

    private ClientProductSPU transformClient(ProductSPU productSPU) throws Exception {
        ClientProductSPU clientProductSPU = new ClientProductSPU(productSPU);
        ProductProvider provider = productProviderDao.load(clientProductSPU.getPid());
        if (null != provider) clientProductSPU.setProviderName(provider.getName());
        ProductCategory category = productCategoryDao.load(clientProductSPU.getCid());
        if (null != category) clientProductSPU.setCategoryName(category.getName());
        ProductBrand brand = productBrandDao.load(clientProductSPU.getBid());
        if (null != brand) clientProductSPU.setBrandName(brand.getName());
        ProductSeries series = productSeriesDao.load(clientProductSPU.getSid());
        if (null != series) clientProductSPU.setSeriesName(series.getName());
        return clientProductSPU;
    }

    @Override
    public boolean delSKU(long id) throws Exception {
        ProductSKU productSKU = productSKUDao.load(id);
        if (null != productSKU) {
            productSKU.setStatus(ProductSKU.status_delete);
            return productSKUDao.update(productSKU);
        }
        return false;
    }

    @Override
    public boolean delSPU(long id) throws Exception {
        ProductSPU productSPU = productSPUDao.load(id);
        if (null != productSPU) {
            productSPU.setStatus(ProductSPU.status_delete);
            return productSPUDao.update(productSPU);
        }
        return false;
    }

    @Override
    public Pager getSPUBackPager(Pager pager, long subid, long cid, long pid, long bid, long sid, String name,int saleStatus) throws Exception {
        String sql = "SELECT * FROM `product_spu` where `status` = " + ProductSPU.status_nomore;
        String sqlCount = "SELECT COUNT(id) FROM `product_spu` where `status` = " + ProductSPU.status_nomore;
        String limit = " limit %d , %d ";
        if (subid > 0) {
            sql += " and subid = " + subid;
            sqlCount += " and subid = " + subid;
        }
        if (cid > 0) {
            sql += " and cid = " + cid;
            sqlCount += " and cid = " + cid;
        }
        if (pid > 0) {
            sql += " and pid = " + pid;
            sqlCount += " and pid = " + pid;
        }
        if (bid > 0) {
            sql += " and bid = " + bid;
            sqlCount += " and bid = " + bid;
        }
        if (sid > 0) {
            sql += " and sid = " + sid;
            sqlCount += " and sid = " + sid;
        }
        if (StringUtils.isNotBlank(name)) {
            sql += " and `name` like '%" + name + "%'";
            sqlCount += " and `name` like '%" + name + "%'";
        }
        if (saleStatus > -1) {
            sql += " and saleStatus = " + saleStatus;
            sqlCount += " and saleStatus = " + saleStatus;
        }
        sql = sql + " order  by `sort` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<ProductSPU> productSPUList = this.jdbcTemplate.query(sql, spuRowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        pager.setData(transformClients(productSPUList));
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public ClientProductSPU selectSPU(int type, long subid, long pid, long cid, long bid, long sid) throws Exception {
        int count = productSPUDao.getCount(type, subid, pid, cid, bid, sid);
        if (count == 0) throw new StoreSystemException("没有此类产品");
        List<ProductSPU> productSPUList = productSPUDao.getAllList(type, subid, pid, cid, bid, sid);
        ProductSPU productSPU = productSPUList.get(0);
        ClientProductSPU clientProductSPU = transformClient(productSPU);
        return clientProductSPU;
    }

    @Override
    public Pager getSaleSPUBackPager(Pager pager, long pSubid, long subid, long cid, long bid) throws Exception {
        String sql = "SELECT * FROM `product_spu` where `status` = " + ProductSPU.status_nomore + " and subid = " + pSubid;
        String sqlCount = "SELECT COUNT(id) FROM `product_spu` where `status` = " + ProductSPU.status_nomore + " and subid = " + pSubid;
        String limit = " limit %d , %d ";
        if (cid > 0) {
            sql += " and cid = " + cid;
            sqlCount += " and cid = " + cid;
        }
        if (bid > 0) {
            sql += " and bid = " + bid;
            sqlCount += " and bid = " + bid;
        }
        sql = sql + " order  by `sort` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<ProductSPU> productSPUList = this.jdbcTemplate.query(sql, spuRowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        List<ClientProductSPU> data = transformClients(productSPUList);
        for (ClientProductSPU one : data) {
            int num = 0;
            List<InventoryDetail> details = inventoryDetailDao.getAllListBySubAndSPU(subid, one.getId());
            for (InventoryDetail detail : details) {
                num += detail.getNum();
            }
            one.setCanUseNum(num);
        }
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public List<ClientProductSKU> getSaleSKUAllList(long subid, long spuid) throws Exception {
        List<ProductSKU> list = productSKUDao.getAllList(spuid, ProductSKU.status_nomore);
        List<ClientProductSKU> res = Lists.newArrayList();
        Set<Long> wids = Sets.newHashSet();
        Set<Long> sids = Sets.newHashSet();
        for (ProductSKU one : list) {
            ClientProductSKU client = new ClientProductSKU(one);
            int num = 0;
            List<InventoryDetail> allDetails = inventoryDetailDao.getAllListBySKU(one.getId());
            List<ClientInventoryDetail> details = Lists.newArrayList();
            List<ClientInventoryDetail> otherDetails = Lists.newArrayList();
            for (InventoryDetail detail : allDetails) {
                wids.add(detail.getWid());
                sids.add(detail.getSubid());
                ClientInventoryDetail clientInventoryDetail = new ClientInventoryDetail(detail);
                if (detail.getSubid() == subid) {
                    details.add(clientInventoryDetail);
                    num += detail.getNum();
                } else {
                    otherDetails.add(clientInventoryDetail);
                }
            }
            client.setDetails(details);
            client.setOtherDetails(otherDetails);
            client.setCanUseNum(num);
            res.add(client);
        }
        List<InventoryWarehouse> warehouses = inventoryWarehouseDao.load(Lists.newArrayList(wids));
        Map<Long, InventoryWarehouse> warehouseMap = warehouseMapUtils.listToMap(warehouses, "id");
        List<Subordinate> subordinates = subordinateDao.load(Lists.newArrayList(sids));
        Map<Long, Subordinate> subordinateMap = subordinateMapUtils.listToMap(subordinates, "id");

        for (ClientProductSKU one : res) {
            List<ClientInventoryDetail> details = one.getDetails();
            for (ClientInventoryDetail detail : details) {
                InventoryWarehouse warehouse = warehouseMap.get(detail.getWid());
                if (null != warehouse) detail.setWarehouseName(warehouse.getName());
                Subordinate subordinate = subordinateMap.get(detail.getSubid());
                if (null != subordinate) detail.setSubName(subordinate.getName());
            }
            List<ClientInventoryDetail> otherDetails = one.getOtherDetails();
            for (ClientInventoryDetail otherDetail : otherDetails) {
                InventoryWarehouse warehouse = warehouseMap.get(otherDetail.getWid());
                if (null != warehouse) otherDetail.setWarehouseName(warehouse.getName());
                Subordinate subordinate = subordinateMap.get(otherDetail.getSubid());
                if (null != subordinate) otherDetail.setSubName(subordinate.getName());
            }
        }
        return res;
    }

    @Override
    public boolean updateSaleStatus(long id, int open) throws Exception {
        ProductSPU productSPU = productSPUDao.load(id);
        productSPU.setSaleStatus(open);
        return  productSPUDao.update(productSPU);
    }

}
