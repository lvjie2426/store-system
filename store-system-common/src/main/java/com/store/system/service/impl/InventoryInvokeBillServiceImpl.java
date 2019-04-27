package com.store.system.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.bean.InventoryInBillItem;
import com.store.system.bean.InventoryInvokeBillItem;
import com.store.system.bean.InventoryOutBillItem;
import com.store.system.client.ClientInventoryInvokeBill;
import com.store.system.client.ClientInventoryInvokeBillItem;
import com.store.system.dao.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.InventoryInvokeBillService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class InventoryInvokeBillServiceImpl implements InventoryInvokeBillService {

    private ReentrantLock lock = new ReentrantLock();

    private TransformFieldSetUtils itemFieldSetUtils = new TransformFieldSetUtils(InventoryInvokeBillItem.class);

    private TransformFieldSetUtils detailFieldSetUtils = new TransformFieldSetUtils(InventoryDetail.class);

    private TransformFieldSetUtils spuFieldSetUtils = new TransformFieldSetUtils(ProductSPU.class);

    private TransformMapUtils detailMapUtils = new TransformMapUtils(InventoryDetail.class);

    private TransformMapUtils spuMapUtils = new TransformMapUtils(ProductSPU.class);

    private TransformMapUtils skuMapUtils = new TransformMapUtils(ProductSKU.class);

    private TransformMapUtils userMapUtils = new TransformMapUtils(User.class);

    private TransformMapUtils warehouseMapUtils = new TransformMapUtils(InventoryWarehouse.class);

    private TransformMapUtils subMapUtils = new TransformMapUtils(Subordinate.class);

    private TransformMapUtils categoryMapUtils = new TransformMapUtils(ProductCategory.class);

    private TransformMapUtils brandMapUtils = new TransformMapUtils(ProductBrand.class);

    private TransformMapUtils seriesMapUtils = new TransformMapUtils(ProductSeries.class);

    private RowMapperHelp<InventoryInvokeBill> rowMapper = new RowMapperHelp<>(InventoryInvokeBill.class);

    @Resource
    private InventoryInvokeBillDao inventoryInvokeBillDao;

    @Resource
    private InventoryDetailDao inventoryDetailDao;

    @Resource
    private InventoryOutBillDao inventoryOutBillDao;

    @Resource
    private InventoryInBillDao inventoryInBillDao;

    @Resource
    private ProductSKUDao productSKUDao;

    @Resource
    private UserDao userDao;

    @Resource
    private InventoryWarehouseDao inventoryWarehouseDao;

    @Resource
    private SubordinateDao subordinateDao;

    @Resource
    private ProductSPUDao productSPUDao;

    @Resource
    private ProductCategoryDao productCategoryDao;

    @Resource
    private ProductBrandDao productBrandDao;

    @Resource
    private ProductSeriesDao productSeriesDao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    private void check(InventoryInvokeBill inventoryInvokeBill) throws Exception {
        long inSubid = inventoryInvokeBill.getInSubid();
        if(inSubid == 0) throw new StoreSystemException("入库店铺不能为空");
        long outSubid = inventoryInvokeBill.getOutSubid();
        if(outSubid == 0) throw new StoreSystemException("出库店铺不能为空");
        long inWid = inventoryInvokeBill.getInWid();
        if(inWid == 0) throw new StoreSystemException("入库仓库不能为空");
        long outWid = inventoryInvokeBill.getOutWid();
        if(outWid == 0) throw new StoreSystemException("出库仓库不能为空");
        if(inventoryInvokeBill.getCreateUid() == 0) throw new StoreSystemException("创建人不能为空");
        if(inventoryInvokeBill.getInUid() == 0) throw new StoreSystemException("入库人不能为空");
        String itemsJson = inventoryInvokeBill.getItemsJson();
        List<InventoryInvokeBillItem> items = null;
        try {
            items = JsonUtils.fromJson(itemsJson, new TypeReference<List<InventoryInvokeBillItem>>() {});
        } catch (Exception e) {
            throw new StoreSystemException("调货单子项目格式错误");
        }
        Map<Long, Integer> didNumMap = Maps.newHashMap();
        for(InventoryInvokeBillItem item : items) {
            Integer num = didNumMap.get(item.getDid());
            if(null == num) {
                didNumMap.put(item.getDid(), 1);
            } else {
                didNumMap.put(item.getDid(), num + 1);
            }
        }
        Set<Long> dids = itemFieldSetUtils.fieldList(items, "did");
        List<InventoryDetail> details = inventoryDetailDao.load(Lists.newArrayList(dids));
        Map<Long, InventoryDetail> detailMap = detailMapUtils.listToMap(details, "id");
        for(InventoryInvokeBillItem item : items) {
            if(item.getNum() <= 0) throw new StoreSystemException("调货单子项目调货数错误");
            if(didNumMap.get(item.getDid()) > 1) throw new StoreSystemException("已经添加相同子项目进调货单");
            InventoryDetail detail = detailMap.get(item.getDid());
            if(null == detail) throw new StoreSystemException("库存明细为空");
            if(detail.getWid() != outWid) throw new StoreSystemException("调货单子项目仓库错误");
            if(item.getNum() > detail.getNum()) throw new StoreSystemException("库存数量不足");
        }
    }

    @Override
    public InventoryInvokeBill add(InventoryInvokeBill inventoryInvokeBill) throws Exception {
        check(inventoryInvokeBill);
        return inventoryInvokeBillDao.insert(inventoryInvokeBill);
    }

    @Override
    public boolean update(InventoryInvokeBill inventoryInvokeBill) throws Exception {
        InventoryInvokeBill dbInvokeBill = inventoryInvokeBillDao.load(inventoryInvokeBill.getId());
        int status = dbInvokeBill.getStatus();
        if(status != InventoryInvokeBill.status_edit) throw new StoreSystemException("状态错误,不能修改");
        check(inventoryInvokeBill);
        boolean res = inventoryInvokeBillDao.update(inventoryInvokeBill);
        return res;
    }

    @Override
    public boolean del(long id) throws Exception {
        InventoryInvokeBill invokeBill = inventoryInvokeBillDao.load(id);
        if(null != invokeBill && invokeBill.getStatus() == InventoryInvokeBill.status_edit) {
            return inventoryInvokeBillDao.delete(id);
        }
        return false;
    }

    @Override
    public boolean submit(long id) throws Exception {
        InventoryInvokeBill outBill = inventoryInvokeBillDao.load(id);
        if(null != outBill && outBill.getStatus() == InventoryInvokeBill.status_edit) {
            outBill.setStatus(InventoryInvokeBill.status_wait_check);
            return inventoryInvokeBillDao.update(outBill);
        }
        return false;
    }

    @Override
    public void pass(long id, long checkUid, long outUid) throws Exception {
        lock.lock();
        try {
            InventoryInvokeBill invokeBill = inventoryInvokeBillDao.load(id);
            if(null == invokeBill) throw new StoreSystemException("调货单为空");
            if(invokeBill.getStatus() != InventoryInvokeBill.status_wait_check) throw new StoreSystemException("调货单状态错误");
            String itemsJson = invokeBill.getItemsJson();
            List<InventoryInvokeBillItem> items = JsonUtils.fromJson(itemsJson, new TypeReference<List<InventoryInvokeBillItem>>() {});
            Set<Long> dids = itemFieldSetUtils.fieldList(items, "did");
            List<InventoryDetail> details = inventoryDetailDao.load(Lists.newArrayList(dids));
            Map<Long, InventoryDetail> detailMap = detailMapUtils.listToMap(details, "id");
            for(InventoryInvokeBillItem item : items) {
                InventoryDetail detail = detailMap.get(item.getDid());
                if(item.getNum() > detail.getNum()) throw new StoreSystemException("库存数量不足");
            }
            Set<Long> skuids = Sets.newHashSet();
            List<InventoryOutBillItem> outBillItems = Lists.newArrayList();
            for(InventoryInvokeBillItem item : items) {
                InventoryOutBillItem outBillItem = new InventoryOutBillItem();
                outBillItem.setDid(item.getDid());
                outBillItem.setNum(item.getNum());
                outBillItems.add(outBillItem);

                InventoryDetail detail = detailMap.get(item.getDid());
                detail.setNum(detail.getNum() - item.getNum());
                inventoryDetailDao.update(detail);

                skuids.add(detail.getP_skuid());
            }
            List<ProductSKU> skuList = productSKUDao.load(Lists.newArrayList(skuids));
            Map<Long, ProductSKU> skuMap = skuMapUtils.listToMap(skuList, "id");
            List<InventoryInBillItem> inBillItems = Lists.newArrayList();
            for(InventoryInvokeBillItem item : items) {
                InventoryDetail detail = detailMap.get(item.getDid());
                ProductSKU sku = skuMap.get(detail.getP_skuid());
                InventoryInBillItem inBillItem = new InventoryInBillItem();
                inBillItem.setSpuid(detail.getP_spuid());
                inBillItem.setCode(sku.getCode());
                inBillItem.setProperties(sku.getProperties());
                inBillItem.setCostPrice(sku.getCostPrice());
                inBillItem.setRetailPrice(sku.getRetailPrice());
                inBillItem.setIntegralPrice(sku.getIntegralPrice());
                inBillItem.setNum(item.getNum());
                inBillItems.add(inBillItem);
            }

            InventoryOutBill outBill = new InventoryOutBill();
            outBill.setWid(invokeBill.getOutWid());
            outBill.setSubid(invokeBill.getOutSubid());
            outBill.setCheckUid(checkUid);
            outBill.setCheck(InventoryOutBill.check_pass);
            outBill.setCreateUid(checkUid);
            outBill.setItemsJson(JsonUtils.toJson(outBillItems));
            outBill.setOutUid(outUid);
            outBill.setStatus(InventoryOutBill.status_end);
            inventoryOutBillDao.insert(outBill);

            InventoryInBill inBill = new InventoryInBill();
            inBill.setSubid(invokeBill.getInSubid());
            inBill.setWid(invokeBill.getInWid());
            inBill.setInUid(invokeBill.getInUid());
            inBill.setCreateUid(invokeBill.getInUid());
            inBill.setItemsJson(JsonUtils.toJson(inBillItems));
            inBill.setStatus(InventoryInBill.status_wait_check);
            inventoryInBillDao.insert(inBill);

            invokeBill.setCheckUid(checkUid);
            invokeBill.setOutUid(outUid);
            invokeBill.setCheck(InventoryInvokeBill.check_pass);
            invokeBill.setStatus(InventoryInvokeBill.status_end);
            inventoryInvokeBillDao.update(invokeBill);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void noPass(long id, long checkUid) throws Exception {
        InventoryInvokeBill invokeBill = inventoryInvokeBillDao.load(id);
        if(null == invokeBill) throw new StoreSystemException("调货单为空");
        if(invokeBill.getStatus() != InventoryInvokeBill.status_wait_check) throw new StoreSystemException("调货单状态错误");
        invokeBill.setCheckUid(checkUid);
        invokeBill.setCheck(InventoryOutBill.check_no_pass);
        invokeBill.setStatus(InventoryOutBill.status_end);
        inventoryInvokeBillDao.update(invokeBill);
    }

    @Override
    public Pager getCreatePager(Pager pager, long createUid) throws Exception {
        String sql = "SELECT * FROM `inventory_invoke_bill` where createUid = " + createUid;
        String sqlCount = "SELECT COUNT(id) FROM `inventory_invoke_bill` where createUid = " + createUid;
        String limit = " limit %d , %d ";
        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<InventoryInvokeBill> invokeBills = this.jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        List<ClientInventoryInvokeBill> data = transformClients(invokeBills);
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public Pager getCheckPager(Pager pager, long subid) throws Exception {
        String sql = "SELECT * FROM `inventory_invoke_bill` where outSubid = " + subid + " and `status` > " + InventoryInvokeBill.status_edit;
        String sqlCount = "SELECT COUNT(id) FROM `inventory_invoke_bill` where outSubid = " + subid + " and `status` > " + InventoryInvokeBill.status_edit;
        String limit = " limit %d , %d ";
        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<InventoryInvokeBill> invokeBills = this.jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        List<ClientInventoryInvokeBill> data = transformClients(invokeBills);
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

    private List<ClientInventoryInvokeBill> transformClients(List<InventoryInvokeBill> invokeBills) throws Exception {
        List<ClientInventoryInvokeBill> res = Lists.newArrayList();
        Set<Long> uids = Sets.newHashSet();
        Set<Long> wids = Sets.newHashSet();
        Set<Long> subids = Sets.newHashSet();
        for(InventoryInvokeBill invokeBill : invokeBills) {
            if(invokeBill.getInWid() > 0) wids.add(invokeBill.getInWid());
            if(invokeBill.getOutWid() > 0) wids.add(invokeBill.getOutWid());
            if(invokeBill.getCreateUid() > 0) uids.add(invokeBill.getCreateUid());
            if(invokeBill.getOutUid() > 0) uids.add(invokeBill.getOutUid());
            if(invokeBill.getCheckUid() > 0) uids.add(invokeBill.getCheckUid());
            if(invokeBill.getInUid() > 0) uids.add(invokeBill.getInUid());
            if(invokeBill.getInSubid() > 0) subids.add(invokeBill.getInSubid());
            if(invokeBill.getOutSubid() > 0) subids.add(invokeBill.getOutSubid());
        }
        List<User> users = userDao.load(Lists.newArrayList(uids));
        Map<Long, User> userMap = userMapUtils.listToMap(users, "id");
        List<Subordinate> subordinates = subordinateDao.load(Lists.newArrayList(subids));
        Map<Long, Subordinate> subordinateMap = subMapUtils.listToMap(subordinates, "id");
        List<InventoryWarehouse> warehouses = inventoryWarehouseDao.load(Lists.newArrayList(wids));
        Map<Long, InventoryWarehouse> warehouseMap = warehouseMapUtils.listToMap(warehouses, "id");
        for(InventoryInvokeBill invokeBill : invokeBills) {
            ClientInventoryInvokeBill client = new ClientInventoryInvokeBill(invokeBill);
            Subordinate subordinate = subordinateMap.get(client.getOutSubid());
            if(null != subordinate) client.setOutSubName(subordinate.getName());
            subordinate = subordinateMap.get(client.getInSubid());
            if(null != subordinate) client.setInSubName(subordinate.getName());
            InventoryWarehouse warehouse = warehouseMap.get(client.getInWid());
            if(null != warehouse) client.setInWarehouseName(warehouse.getName());
            warehouse = warehouseMap.get(client.getOutWid());
            if(null != warehouse) client.setOutWarehouseName(warehouse.getName());
            User user = userMap.get(client.getCreateUid());
            if(null != user) client.setCreateUserName(user.getName());
            user = userMap.get(client.getCheckUid());
            if(null != user) client.setCheckUserName(user.getName());
            user = userMap.get(client.getOutUid());
            if(null != user) client.setOutUserName(user.getName());
            user = userMap.get(client.getInUid());
            if(null != user) client.setInUserName(user.getName());
            List<InventoryInvokeBillItem> items = JsonUtils.fromJson(invokeBill.getItemsJson(), new TypeReference<List<InventoryInvokeBillItem>>() {});
            Set<Long> dids = itemFieldSetUtils.fieldList(items, "did");
            List<InventoryDetail> details = inventoryDetailDao.load(Lists.newArrayList(dids));
            Map<Long, InventoryDetail> detailMap = detailMapUtils.listToMap(details, "id");
            Set<Long> spuids = detailFieldSetUtils.fieldList(details, "p_spuid");
            List<ProductSPU> spuList = productSPUDao.load(Lists.newArrayList(spuids));
            Map<Long, ProductSPU> spuMap = spuMapUtils.listToMap(spuList, "id");
            Set<Long> skuids = detailFieldSetUtils.fieldList(details, "p_skuid");
            List<ProductSKU> skuList = productSKUDao.load(Lists.newArrayList(skuids));
            Map<Long, ProductSKU> skuMap = skuMapUtils.listToMap(skuList, "id");

            Set<Long> cids = spuFieldSetUtils.fieldList(spuList, "cid");
            List<ProductCategory> categories = productCategoryDao.load(Lists.newArrayList(cids));
            Map<Long, ProductCategory> categoryMap = categoryMapUtils.listToMap(categories, "id");
            Set<Long> bids = spuFieldSetUtils.fieldList(spuList, "bid");
            List<ProductBrand> brands = productBrandDao.load(Lists.newArrayList(bids));
            Map<Long, ProductBrand> brandMap = brandMapUtils.listToMap(brands, "id");
            Set<Long> sids = spuFieldSetUtils.fieldList(spuList, "sid");
            List<ProductSeries> seriesList = productSeriesDao.load(Lists.newArrayList(sids));
            Map<Long, ProductSeries> seriesMap = seriesMapUtils.listToMap(seriesList, "id");
            List<ClientInventoryInvokeBillItem> clientItems = Lists.newArrayList();
            for(InventoryInvokeBillItem item : items) {
                ClientInventoryInvokeBillItem clientItem = new ClientInventoryInvokeBillItem(item);
                InventoryDetail detail = detailMap.get(clientItem.getDid());
                if(null != detail) {
                    ProductSPU spu = spuMap.get(detail.getP_spuid());
                    if(null != spu) {
                        clientItem.setSpuid(spu.getId());
                        clientItem.setSpuName(spu.getName());
                        clientItem.setSpuCovers(spu.getCovers());
                        clientItem.setSpuIcon(spu.getIcon());
                        clientItem.setCid(spu.getCid());
                        clientItem.setBid(spu.getBid());
                        clientItem.setSid(spu.getSid());
                    }
                    ProductSKU sku = skuMap.get(detail.getP_skuid());
                    if(null != sku) {
                        clientItem.setCode(sku.getCode());
                        clientItem.setProperties(sku.getProperties());
                    }
                }
                ProductCategory category = categoryMap.get(clientItem.getCid());
                if(null != category) clientItem.setCategoryName(category.getName());
                ProductBrand brand = brandMap.get(clientItem.getBid());
                if(null != brand) clientItem.setBrandName(brand.getName());
                ProductSeries series = seriesMap.get(clientItem.getSid());
                if(null != series) clientItem.setSeriesName(series.getName());
                clientItems.add(clientItem);
            }
            client.setItems(clientItems);
            res.add(client);
        }
        return res;
    }

}
