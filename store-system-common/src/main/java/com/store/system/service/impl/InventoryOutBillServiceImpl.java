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
import com.store.system.bean.InventoryOutBillItem;
import com.store.system.client.ClientInventoryOutBill;
import com.store.system.client.ClientInventoryOutBillItem;
import com.store.system.dao.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.InventoryOutBillService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class InventoryOutBillServiceImpl implements InventoryOutBillService {

    private ReentrantLock lock = new ReentrantLock();

    private TransformFieldSetUtils itemFieldSetUtils = new TransformFieldSetUtils(InventoryOutBillItem.class);

    private TransformFieldSetUtils spuFieldSetUtils = new TransformFieldSetUtils(ProductSPU.class);

    private TransformFieldSetUtils detailFieldSetUtils = new TransformFieldSetUtils(InventoryDetail.class);

    private TransformMapUtils spuMapUtils = new TransformMapUtils(ProductSPU.class);

    private TransformMapUtils skuMapUtils = new TransformMapUtils(ProductSKU.class);

    private TransformMapUtils categoryMapUtils = new TransformMapUtils(ProductCategory.class);

    private TransformMapUtils brandMapUtils = new TransformMapUtils(ProductBrand.class);

    private TransformMapUtils seriesMapUtils = new TransformMapUtils(ProductSeries.class);

    private TransformMapUtils detailMapUtils = new TransformMapUtils(InventoryDetail.class);

    private TransformMapUtils userMapUtils = new TransformMapUtils(User.class);

    private TransformMapUtils warehouseMapUtils = new TransformMapUtils(InventoryWarehouse.class);

    private TransformMapUtils subMapUtils = new TransformMapUtils(Subordinate.class);

    private TransformMapUtils nameMapUtils = new TransformMapUtils(ProductPropertyName.class);

    private TransformMapUtils valueMapUtils = new TransformMapUtils(ProductPropertyValue.class);

    private RowMapperHelp<InventoryOutBill> rowMapper = new RowMapperHelp<>(InventoryOutBill.class);



    @Resource
    private InventoryOutBillDao inventoryOutBillDao;

    @Resource
    private InventoryDetailDao inventoryDetailDao;

    @Resource
    private UserDao userDao;

    @Resource
    private InventoryWarehouseDao inventoryWarehouseDao;

    @Resource
    private ProductSPUDao productSPUDao;

    @Resource
    private ProductSKUDao productSKUDao;

    @Resource
    private ProductBrandDao productBrandDao;

    @Resource
    private ProductCategoryDao productCategoryDao;

    @Resource
    private ProductSeriesDao productSeriesDao;

    @Resource
    private SubordinateDao subordinateDao;

    @Resource
    private ProductPropertyNameDao productPropertyNameDao;

    @Resource
    private ProductPropertyValueDao productPropertyValueDao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public InventoryOutBill add(InventoryOutBill inventoryOutBill) throws Exception {
        check(inventoryOutBill);
        //查询
        Subordinate subordinate = subordinateDao.load(inventoryOutBill.getSubid());
        if(subordinate.getIsCheck()==Subordinate.isCheck_no){
            //todo 这里获取店长，设置成店长的id。
            inventoryOutBill.setCheckUid(0);
            inventoryOutBill.setCheck(InventoryOutBill.check_pass);
            inventoryOutBill.setStatus(InventoryOutBill.status_end);
        }
        return inventoryOutBillDao.insert(inventoryOutBill);
    }

    @Override
    public boolean update(InventoryOutBill inventoryOutBill) throws Exception {
        InventoryOutBill dbOutBill = inventoryOutBillDao.load(inventoryOutBill.getId());
        int status = dbOutBill.getStatus();
        if(status != InventoryOutBill.status_edit) throw new StoreSystemException("状态错误,不能修改");
        check(inventoryOutBill);
        boolean res = inventoryOutBillDao.update(inventoryOutBill);
        return res;
    }

    @Override
    public boolean del(long id) throws Exception {
        InventoryOutBill outBill = inventoryOutBillDao.load(id);
        if(null != outBill && outBill.getStatus() == InventoryOutBill.status_edit) {
            return inventoryOutBillDao.delete(id);
        }
        return false;
    }

    @Override
    public boolean submit(long id) throws Exception {
        InventoryOutBill outBill = inventoryOutBillDao.load(id);
        if(null != outBill && outBill.getStatus() == InventoryOutBill.status_edit) {
            outBill.setStatus(InventoryOutBill.status_wait_check);
            return inventoryOutBillDao.update(outBill);
        }
        return false;
    }

    @Override
    @Transactional
    public void pass(long id, long checkUid) throws Exception {
        lock.lock();
        try {
            InventoryOutBill outBill = inventoryOutBillDao.load(id);
            if(null == outBill) throw new StoreSystemException("出库单为空");
            if(outBill.getStatus() != InventoryOutBill.status_wait_check) throw new StoreSystemException("出库单状态错误");
            List<InventoryOutBillItem> items = outBill.getItems();
            Set<Long> dids = itemFieldSetUtils.fieldList(items, "did");
            List<InventoryDetail> details = inventoryDetailDao.load(Lists.newArrayList(dids));
            Map<Long, InventoryDetail> detailMap = detailMapUtils.listToMap(details, "id");
            for(InventoryOutBillItem item : items) {
                InventoryDetail detail = detailMap.get(item.getDid());
                if(item.getNum() > detail.getNum()) throw new StoreSystemException("库存数量不足");
            }
            for(InventoryOutBillItem item : items) {
                InventoryDetail detail = detailMap.get(item.getDid());
                detail.setNum(detail.getNum() - item.getNum());
                inventoryDetailDao.update(detail);
            }
            outBill.setCheckUid(checkUid);
            outBill.setCheck(InventoryOutBill.check_pass);
            outBill.setStatus(InventoryOutBill.status_end);
            inventoryOutBillDao.update(outBill);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void noPass(long id, long checkUid) throws Exception {
        InventoryOutBill outBill = inventoryOutBillDao.load(id);
        if(null == outBill) throw new StoreSystemException("出库单为空");
        if(outBill.getStatus() != InventoryOutBill.status_wait_check) throw new StoreSystemException("出库单状态错误");
        outBill.setCheckUid(checkUid);
        outBill.setCheck(InventoryOutBill.check_no_pass);
        outBill.setStatus(InventoryOutBill.status_end);
        inventoryOutBillDao.update(outBill);
    }

    private void check(InventoryOutBill inventoryOutBill) throws Exception {
        long subid = inventoryOutBill.getSubid();
        if(subid == 0) throw new StoreSystemException("店铺不能为空");
        long wid = inventoryOutBill.getWid();
        if(wid == 0) throw new StoreSystemException("仓库不能为空");
        if(inventoryOutBill.getOutUid() == 0) throw new StoreSystemException("出库人不能为空");
        if(inventoryOutBill.getCreateUid() == 0) throw new StoreSystemException("创建人不能为空");
        List<InventoryOutBillItem> items = inventoryOutBill.getItems();
        Map<Long, Integer> didNumMap = Maps.newHashMap();
        for(InventoryOutBillItem item : items) {
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
        for(InventoryOutBillItem item : items) {
            if(item.getNum() <= 0) throw new StoreSystemException("出库单子项目出库数错误");
            if(didNumMap.get(item.getDid()) > 1) throw new StoreSystemException("已经添加相同子项目进出库单");
            InventoryDetail detail = detailMap.get(item.getDid());
            if(null == detail) throw new StoreSystemException("库存明细为空");
            if(detail.getWid() != wid) throw new StoreSystemException("出库单子项目仓库错误");
            if(item.getNum() > detail.getNum()) throw new StoreSystemException("库存数量不足");
        }
    }

    @Override
    public Pager getCreatePager(Pager pager, long createUid) throws Exception {
        String sql = "SELECT * FROM `inventory_out_bill` where createUid = " + createUid;
        String sqlCount = "SELECT COUNT(id) FROM `inventory_out_bill` where createUid = " + createUid;
        String limit = " limit %d , %d ";
        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<InventoryOutBill> outBills = this.jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        List<ClientInventoryOutBill> data = transformClients(outBills);
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public Pager getCheckPager(Pager pager, long subid) throws Exception {
        String sql = "SELECT * FROM `inventory_out_bill` where subid = " + subid + " and `status` > " + InventoryOutBill.status_edit;
        String sqlCount = "SELECT COUNT(id) FROM `inventory_out_bill` where subid = " + subid + " and `status` > " + InventoryOutBill.status_edit;
        String limit = " limit %d , %d ";
        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<InventoryOutBill> outBills = this.jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        List<ClientInventoryOutBill> data = transformClients(outBills);
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public Pager getAllPager(Pager pager, long subid, int type) throws Exception {
        String sql = "SELECT * FROM `inventory_out_bill` where subid = " + subid + " and `type` = " + type;
        String sqlCount = "SELECT COUNT(id) FROM `inventory_out_bill` where subid = " + subid +" and `type` = " + type;
        String limit = " limit %d , %d ";
        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<InventoryOutBill> outBills = this.jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        List<ClientInventoryOutBill> data = transformClients(outBills);
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

    private List<ClientInventoryOutBill> transformClients(List<InventoryOutBill> outBills) throws Exception {
        List<ClientInventoryOutBill> res = Lists.newArrayList();
        Set<Long> uids = Sets.newHashSet();
        Set<Long> wids = Sets.newHashSet();
        Set<Long> subids = Sets.newHashSet();
        for(InventoryOutBill outBill : outBills) {
            if(outBill.getWid() > 0) wids.add(outBill.getWid());
            if(outBill.getCreateUid() > 0) uids.add(outBill.getCreateUid());
            if(outBill.getOutUid() > 0) uids.add(outBill.getOutUid());
            if(outBill.getCheckUid() > 0) uids.add(outBill.getCheckUid());
            if(outBill.getSubid() > 0) subids.add(outBill.getSubid());
        }
        List<User> users = userDao.load(Lists.newArrayList(uids));
        Map<Long, User> userMap = userMapUtils.listToMap(users, "id");
        List<InventoryWarehouse> warehouses = inventoryWarehouseDao.load(Lists.newArrayList(wids));
        Map<Long, InventoryWarehouse> warehouseMap = warehouseMapUtils.listToMap(warehouses, "id");
        List<Subordinate> subordinates = subordinateDao.load(Lists.newArrayList(subids));
        Map<Long, Subordinate> subordinateMap = subMapUtils.listToMap(subordinates, "id");
        for(InventoryOutBill outBill : outBills) {
            ClientInventoryOutBill client = new ClientInventoryOutBill(outBill);
            InventoryWarehouse warehouse = warehouseMap.get(client.getWid());
            if(null != warehouse) client.setWarehouseName(warehouse.getName());
            User user = userMap.get(client.getCreateUid());
            if(null != user) client.setCreateUserName(user.getName());
            user = userMap.get(client.getOutUid());
            if(null != user) client.setOutUserName(user.getName());
            user = userMap.get(client.getCheckUid());
            if(null != user) client.setCheckUserName(user.getName());
            Subordinate subordinate = subordinateMap.get(client.getSubid());
            if(null != subordinate) client.setSubName(subordinate.getName());
            List<InventoryOutBillItem> items = outBill.getItems();
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
            List<ClientInventoryOutBillItem> clientItems = Lists.newArrayList();
            for(InventoryOutBillItem item : items) {
                ClientInventoryOutBillItem clientItem = new ClientInventoryOutBillItem(item);
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

                        Map<Long,Object> map = sku.getProperties();
                        Map<Object,Object> map_value = Maps.newHashMap();
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
                                    if(nameMap.get(skuEntry.getKey()).getInput() == ProductPropertyName.input_no){
                                        map_value.put(nameMap.get(skuEntry.getKey()).getContent(), valueMap.get(Long.parseLong(skuEntry.getValue().toString())).getContent());
                                    }else{
                                        map_value.put(nameMap.get(skuEntry.getKey()).getContent(), skuEntry.getValue());
                                    }
                                }
                                clientItem.setP_properties_value(map_value);
                            } else {
                                for (Map.Entry<Long, Object> skuEntry : map.entrySet()) {
                                    map_value.put(nameMap.get(skuEntry.getKey()).getContent(), skuEntry.getValue());
                                }
                                clientItem.setP_properties_value(map_value);
                            }

                        }
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
            client.setClientItems(clientItems);
            res.add(client);
        }
        return res;
    }

}
