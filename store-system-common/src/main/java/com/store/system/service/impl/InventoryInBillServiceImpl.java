package com.store.system.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.model.pagination.PagerSession;
import com.quakoo.baseFramework.model.pagination.service.PagerRequestService;
import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.bean.InventoryInBillItem;
import com.store.system.client.ClientInventoryInBill;
import com.store.system.client.ClientInventoryInBillItem;
import com.store.system.dao.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.InventoryInBillService;
import com.store.system.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class InventoryInBillServiceImpl implements InventoryInBillService {

    private ReentrantLock lock = new ReentrantLock();

    private TransformFieldSetUtils itemFieldSetUtils = new TransformFieldSetUtils(InventoryInBillItem.class);

    private TransformFieldSetUtils skuFieldSetUtils = new TransformFieldSetUtils(ProductSKU.class);

    private TransformFieldSetUtils spuFieldSetUtils = new TransformFieldSetUtils(ProductSPU.class);

    private TransformMapUtils spuMapUtils = new TransformMapUtils(ProductSPU.class);

    private TransformMapUtils userMapUtils = new TransformMapUtils(User.class);

    private TransformMapUtils warehouseMapUtils = new TransformMapUtils(InventoryWarehouse.class);

    private TransformMapUtils categoryMapUtils = new TransformMapUtils(ProductCategory.class);

    private TransformMapUtils brandMapUtils = new TransformMapUtils(ProductBrand.class);

    private TransformMapUtils seriesMapUtils = new TransformMapUtils(ProductSeries.class);

    private TransformMapUtils subMapUtils = new TransformMapUtils(Subordinate.class);

    private TransformMapUtils nameMapUtils = new TransformMapUtils(ProductPropertyName.class);

    private TransformMapUtils valueMapUtils = new TransformMapUtils(ProductPropertyValue.class);

    private RowMapperHelp<InventoryInBill> rowMapper = new RowMapperHelp<>(InventoryInBill.class);

    @Resource
    private InventoryDetailDao inventoryDetailDao;

    @Resource
    private InventoryWarehouseDao inventoryWarehouseDao;

    @Resource
    private ProductPropertyNameDao productPropertyNameDao;

    @Resource
    private ProductPropertyValueDao productPropertyValueDao;

    @Resource
    private InventoryInBillDao inventoryInBillDao;

    @Resource
    private ProductSPUDao productSPUDao;

    @Resource
    private ProductSKUDao productSKUDao;

    @Resource
    private ProductCategoryDao productCategoryDao;

    @Resource
    private ProductBrandDao productBrandDao;

    @Resource
    private ProductSeriesDao productSeriesDao;

    @Resource
    private ProductService productService;

    @Resource
    private UserDao userDao;

    @Resource
    private SubordinateDao subordinateDao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public void noPass(long id, long checkUid) throws Exception {
        InventoryInBill inBill = inventoryInBillDao.load(id);
        if(null == inBill) throw new StoreSystemException("入库单为空");
        if(inBill.getStatus() != InventoryInBill.status_wait_check) throw new StoreSystemException("入库单状态错误");
        inBill.setCheckUid(checkUid);
        inBill.setCheck(InventoryInBill.check_no_pass);
        inBill.setStatus(InventoryInBill.status_end);
        inventoryInBillDao.update(inBill);
    }

    @Override
    @Transactional
    public void pass(long id, long checkUid) throws Exception {
        lock.lock();
        try {
            InventoryInBill inBill = inventoryInBillDao.load(id);
            if(null == inBill) throw new StoreSystemException("入库单为空");
            if(inBill.getStatus() != InventoryInBill.status_wait_check) throw new StoreSystemException("入库单状态错误");
            long subid = inBill.getSubid();
            long wid = inBill.getWid();
            List<InventoryInBillItem> items = inBill.getItems();
            Set<Long> spuids = itemFieldSetUtils.fieldList(items, "spuid");
            List<ProductSPU> productSPUList = productSPUDao.load(Lists.newArrayList(spuids));
            Map<Long, ProductSPU> spuMap = spuMapUtils.listToMap(productSPUList, "id");
            Map<Long, List<String>> skuCodesMap = Maps.newHashMap();
            Map<Long, List<ProductSKU>> skuMap = Maps.newHashMap();
            for(Map.Entry<Long, ProductSPU> entry : spuMap.entrySet()) {
                long spuid = entry.getKey();
                List<ProductSKU> skuList = productSKUDao.getAllList(spuid, ProductSKU.status_nomore);
                Set<String> codes = skuFieldSetUtils.fieldList(skuList, "code");
                skuCodesMap.put(spuid, Lists.newArrayList(codes));
                skuMap.put(spuid, skuList);
            }

            List<InventoryInBillItem> existsItems = Lists.newArrayList(); //已经存在的sku
            List<InventoryInBillItem> noExistsItems = Lists.newArrayList(); //不存在的sku
            for(InventoryInBillItem item : items) {
                String code = item.getCode();
                List<String> skuCodes = skuCodesMap.get(item.getSpuid());
                if(skuCodes.contains(code)) existsItems.add(item);
                else noExistsItems.add(item);
            }

            for(InventoryInBillItem item : noExistsItems) {
                ProductSKU productSKU = new ProductSKU();
                productSKU.setSpuid(item.getSpuid());
                productSKU.setCode(item.getCode());
                productSKU.setProperties(item.getProperties());
                productSKU.setRetailPrice(item.getRetailPrice());
                productSKU.setCostPrice(item.getCostPrice());
                productSKU.setIntegralPrice(item.getIntegralPrice());
                productSKU = productSKUDao.insert(productSKU); //SKU不存在先存sku
                if(null != productSKU) {
                    long skuid = productSKU.getId();
                    ProductSPU spu = spuMap.get(productSKU.getSpuid());
                    InventoryDetail detail = new InventoryDetail();
                    detail.setSubid(subid);
                    detail.setWid(wid);
                    detail.setP_cid(spu.getCid());
                    detail.setP_spuid(productSKU.getSpuid());
                    detail.setP_skuid(skuid);
                    detail.setNum(item.getNum());
                    inventoryDetailDao.insert(detail); //sku存完，再存库存明细
                }
            }

            for(InventoryInBillItem item : existsItems) {
                String code = item.getCode();
                int num = item.getNum();
                long skuid = 0;
                List<ProductSKU> skuList = skuMap.get(item.getSpuid());
                for(ProductSKU sku : skuList) {
                    if(sku.getCode().equals(code)) {
                        skuid = sku.getId();
                        break;
                    }
                }
                List<InventoryDetail> details = inventoryDetailDao.getAllListByWidAndSKU(wid, skuid);
                if(details.size() > 0) { //如果有这个库存明细，做更新
                    InventoryDetail detail = details.get(0);
                    detail.setNum(detail.getNum() + num);
                    inventoryDetailDao.update(detail);
                } else { //没有库存明细，做插入
                    ProductSPU spu = spuMap.get(item.getSpuid());
                    InventoryDetail detail = new InventoryDetail();
                    detail.setSubid(subid);
                    detail.setWid(wid);
                    detail.setP_cid(spu.getCid());
                    detail.setP_spuid(item.getSpuid());
                    detail.setP_skuid(skuid);
                    detail.setNum(num);
                    inventoryDetailDao.insert(detail);
                }
            }
            inBill.setCheckUid(checkUid);
            inBill.setCheck(InventoryInBill.check_pass);
            inBill.setStatus(InventoryInBill.status_end);
            inventoryInBillDao.update(inBill);
        } finally {
            lock.unlock();
        }
    }

    private void check(InventoryInBill inventoryInBill) throws Exception {
        long subid = inventoryInBill.getSubid();
        if(subid == 0) throw new StoreSystemException("店铺不能为空");
        long wid = inventoryInBill.getWid();
        if(wid == 0) throw new StoreSystemException("仓库不能为空");
        if(inventoryInBill.getInUid() == 0) throw new StoreSystemException("入库人不能为空");
        if(inventoryInBill.getCreateUid() == 0) throw new StoreSystemException("创建人不能为空");
        List<InventoryInBillItem> items = inventoryInBill.getItems();
        Map<String, Integer> codeNumMap = Maps.newHashMap();
        for(InventoryInBillItem item : items) {
            if(StringUtils.isBlank(item.getCode())) throw new StoreSystemException("入库单子项目编码不能为空");
            Integer num = codeNumMap.get(item.getCode());
            if(null == num) codeNumMap.put(item.getCode(), 1);
            else codeNumMap.put(item.getCode(), num + 1);
        }
        for(Map.Entry<String, Integer> entry : codeNumMap.entrySet()) {
            if(entry.getValue() > 1) throw new StoreSystemException("已经添加相同子项目进入库单");
        }
        Set<Long> spuids = itemFieldSetUtils.fieldList(items, "spuid");
        List<ProductSPU> productSPUList = productSPUDao.load(Lists.newArrayList(spuids));
        Map<Long, ProductSPU> spuMap = spuMapUtils.listToMap(productSPUList, "id");
        Map<Long, List<Long>> skuPropertyIdsMap = Maps.newHashMap();
        Map<Long, List<String>> skuCodesMap = Maps.newHashMap();
        for(Map.Entry<Long, ProductSPU> entry : spuMap.entrySet()) {
            long spuid = entry.getKey();
            long cid = entry.getValue().getCid();
            List<ProductPropertyName> names = productPropertyNameDao.getAllList(cid, ProductPropertyName.status_nomore);
            List<Long> skuPropertyIds = Lists.newArrayList();
            for(Iterator<ProductPropertyName> it = names.iterator(); it.hasNext();) {
                ProductPropertyName name = it.next();
                if(name.getType() == ProductPropertyName.type_sku) {
                    skuPropertyIds.add(name.getId());
                }
            }
            skuPropertyIdsMap.put(spuid, skuPropertyIds);

            List<ProductSKU> skuList = productSKUDao.getAllList(spuid, ProductSKU.status_nomore);
            Set<String> codes = skuFieldSetUtils.fieldList(skuList, "code");
            skuCodesMap.put(spuid, Lists.newArrayList(codes));
        }
        List<InventoryInBillItem> noExistsItems = Lists.newArrayList();
        for(InventoryInBillItem item : items) {
            String code = item.getCode();
            List<String> skuCodes = skuCodesMap.get(item.getSpuid());
            if(!skuCodes.contains(code)) noExistsItems.add(item);
        }
        for(InventoryInBillItem item : noExistsItems) {
            Map<Long, Object> properties = item.getProperties();
            List<Long> nameIds = skuPropertyIdsMap.get(item.getSpuid());
            for(long nameId : nameIds) {
                if(!properties.keySet().contains(nameId)) throw new StoreSystemException("SKU缺少关键属性");
            }
        }
    }

    @Override
    public InventoryInBill add(InventoryInBill inventoryInBill) throws Exception {
        check(inventoryInBill);

        //查询
        Subordinate subordinate = subordinateDao.load(inventoryInBill.getSubid());
        if(subordinate.getIsCheck()==Subordinate.isCheck_no){
            //todo 这里获取店长，设置成店长的id。
            inventoryInBill.setCheckUid(0);
            inventoryInBill.setCheck(InventoryInBill.check_pass);
            inventoryInBill.setStatus(InventoryInBill.status_end);
        }
        return inventoryInBillDao.insert(inventoryInBill);
    }



    @Override
    public boolean update(InventoryInBill inventoryInBill) throws Exception {
        InventoryInBill dbInBill = inventoryInBillDao.load(inventoryInBill.getId());
        int status = dbInBill.getStatus();
        if(status != InventoryInBill.status_edit) throw new StoreSystemException("状态错误,不能修改");
        check(inventoryInBill);
        boolean res = inventoryInBillDao.update(inventoryInBill);
        return res;
    }

    @Override
    public boolean del(long id) throws Exception {
        InventoryInBill inBill = inventoryInBillDao.load(id);
        if(null != inBill && inBill.getStatus() == InventoryInBill.status_edit) {
            return inventoryInBillDao.delete(id);
        }
        return false;
    }

    @Override
    public boolean submit(long id) throws Exception {
        InventoryInBill inBill = inventoryInBillDao.load(id);
        if(null != inBill && inBill.getStatus() == InventoryInBill.status_edit) {
            inBill.setStatus(InventoryInBill.status_wait_check);
            return inventoryInBillDao.update(inBill);
        }
        return false;
    }

    @Override
    public Pager getCreatePager(Pager pager, long createUid, long startTime, long endTime, int type) throws Exception {
        String sql = "SELECT * FROM `inventory_in_bill` where createUid = " + createUid;
        String sqlCount = "SELECT COUNT(id) FROM `inventory_in_bill` where createUid = " + createUid;
        String limit = " limit %d , %d ";
        if(startTime>0){
            sql = sql + " and `ctime` > " + startTime;
            sqlCount = sqlCount + " and `ctime` > " + startTime;
        }
        if(endTime>0){
            sql = sql + " and `ctime` < " + endTime;
            sqlCount = sqlCount + " and `ctime` < " + endTime;
        }
        if(type>-1){
            sql = sql + " and `type` = " + type;
            sqlCount = sqlCount + " and `type` = " + type;
        }
        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<InventoryInBill> inBills = this.jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        List<ClientInventoryInBill> data = transformClients(inBills);
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public Pager getCheckPager(Pager pager, long subid, long startTime, long endTime, int type) throws Exception {
        String sql = "SELECT * FROM `inventory_in_bill` where subid = " + subid + " and `status` > " + InventoryInBill.status_edit;
        String sqlCount = "SELECT COUNT(id) FROM `inventory_in_bill` where subid = " + subid + " and `status` > " + InventoryInBill.status_edit;
        String limit = " limit %d , %d ";
        if(startTime>0){
            sql = sql + " and `ctime` > " + startTime;
            sqlCount = sqlCount + " and `ctime` > " + startTime;
        }
        if(endTime>0){
            sql = sql + " and `ctime` < " + endTime;
            sqlCount = sqlCount + " and `ctime` < " + endTime;
        }
        if(type>-1){
            sql = sql + " and `type` = " + type;
            sqlCount = sqlCount + " and `type` = " + type;
        }
        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<InventoryInBill> inBills = this.jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        List<ClientInventoryInBill> data = transformClients(inBills);
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public Pager getCreateWebPager(final Pager pager, final long createUid) throws Exception {
        return new PagerRequestService<InventoryInBill>(pager, 0) {
            @Override
            public List<InventoryInBill> step1GetPageResult(String cursor, int size) throws Exception {
                return inventoryInBillDao.getCreatePageList(createUid,Double.parseDouble(cursor),size);
            }
            @Override
            public int step2GetTotalCount() throws Exception {
                return inventoryInBillDao.getCreateCount(createUid);
            }

            @Override
            public List<InventoryInBill> step3FilterResult(List<InventoryInBill> unTransformDatas, PagerSession session) throws Exception {
                return unTransformDatas;
            }

            @Override
            public List<?> step4TransformData(List<InventoryInBill> unTransformDatas, PagerSession session) throws Exception {

                return transformClients(unTransformDatas);
            }
        }.getPager();
    }


    @Override
    public Pager getCheckWebPager(final Pager pager, final long subid) throws Exception {
        return new PagerRequestService<InventoryInBill>(pager, 0) {
            @Override
            public List<InventoryInBill> step1GetPageResult(String cursor, int size) throws Exception {
                return inventoryInBillDao.getCheckPageList(subid,Double.parseDouble(cursor),size);
            }
            @Override
            public int step2GetTotalCount() throws Exception {
                return inventoryInBillDao.getCheckCount(subid);
            }

            @Override
            public List<InventoryInBill> step3FilterResult(List<InventoryInBill> unTransformDatas, PagerSession session) throws Exception {
                return unTransformDatas;
            }

            @Override
            public List<?> step4TransformData(List<InventoryInBill> unTransformDatas, PagerSession session) throws Exception {

                return transformClients(unTransformDatas);
            }
        }.getPager();
    }


    private List<ClientInventoryInBill> transformClients(List<InventoryInBill> inBills) throws Exception {
        List<ClientInventoryInBill> res = Lists.newArrayList();
        Set<Long> uids = Sets.newHashSet();
        Set<Long> wids = Sets.newHashSet();
        Set<Long> subids = Sets.newHashSet();
        for(InventoryInBill inBill : inBills) {
            if(inBill.getWid() > 0) wids.add(inBill.getWid());
            if(inBill.getCreateUid() > 0) uids.add(inBill.getCreateUid());
            if(inBill.getInUid() > 0) uids.add(inBill.getInUid());
            if(inBill.getCheckUid() > 0) uids.add(inBill.getCheckUid());
            if(inBill.getSubid() > 0) subids.add(inBill.getSubid());
        }
        List<User> users = userDao.load(Lists.newArrayList(uids));
        Map<Long, User> userMap = userMapUtils.listToMap(users, "id");
        List<InventoryWarehouse> warehouses = inventoryWarehouseDao.load(Lists.newArrayList(wids));
        Map<Long, InventoryWarehouse> warehouseMap = warehouseMapUtils.listToMap(warehouses, "id");
        List<Subordinate> subordinates = subordinateDao.load(Lists.newArrayList(subids));
        Map<Long, Subordinate> subordinateMap = subMapUtils.listToMap(subordinates, "id");
        for(InventoryInBill inBill : inBills) {
            ClientInventoryInBill client = new ClientInventoryInBill(inBill);
            InventoryWarehouse warehouse = warehouseMap.get(client.getWid());
            if(null != warehouse) client.setWarehouseName(warehouse.getName());
            User user = userMap.get(client.getCreateUid());
            if(null != user) client.setCreateUserName(user.getName());
            user = userMap.get(client.getInUid());
            if(null != user) client.setInUserName(user.getName());
            user = userMap.get(client.getCheckUid());
            if(null != user) client.setCheckUserName(user.getName());
            Subordinate subordinate = subordinateMap.get(client.getSubid());
            if(null != subordinate) client.setSubName(subordinate.getName());
            List<InventoryInBillItem> items = inBill.getItems();
            Set<Long> spuids = itemFieldSetUtils.fieldList(items, "spuid");
            List<ProductSPU> spuList = productSPUDao.load(Lists.newArrayList(spuids));
            Map<Long, ProductSPU> spuMap = spuMapUtils.listToMap(spuList, "id");

            Set<Long> cids = spuFieldSetUtils.fieldList(spuList, "cid");
            List<ProductCategory> categories = productCategoryDao.load(Lists.newArrayList(cids));
            Map<Long, ProductCategory> categoryMap = categoryMapUtils.listToMap(categories, "id");
            Set<Long> bids = spuFieldSetUtils.fieldList(spuList, "bid");
            List<ProductBrand> brands = productBrandDao.load(Lists.newArrayList(bids));
            Map<Long, ProductBrand> brandMap = brandMapUtils.listToMap(brands, "id");
            Set<Long> sids = spuFieldSetUtils.fieldList(spuList, "sid");
            List<ProductSeries> seriesList = productSeriesDao.load(Lists.newArrayList(sids));
            Map<Long, ProductSeries> seriesMap = seriesMapUtils.listToMap(seriesList, "id");
            List<ClientInventoryInBillItem> clientItems = Lists.newArrayList();
            for(InventoryInBillItem item : items) {
                ClientInventoryInBillItem clientItem = new ClientInventoryInBillItem(item);
                ProductSPU spu = spuMap.get(clientItem.getSpuid());
                if(null != spu) {
                    clientItem.setSpuName(spu.getName());
                    clientItem.setSpuCovers(spu.getCovers());
                    clientItem.setSpuIcon(spu.getIcon());
                    clientItem.setBid(spu.getBid());
                    clientItem.setCid(spu.getCid());
                    clientItem.setSid(spu.getSid());

                    Map<Object,Object> map_value = Maps.newHashMap();
                    map_value = productService.getProperties(item,clientItem,"p_properties_value");
                    clientItem.setP_properties_value(map_value);
                }
                ProductBrand brand = brandMap.get(clientItem.getBid());
                if(null != brand) clientItem.setBrandName(brand.getName());
                ProductSeries series = seriesMap.get(clientItem.getSid());
                if(null != series) clientItem.setSeriesName(series.getName());
                ProductCategory category = categoryMap.get(clientItem.getCid());
                if(null != category) clientItem.setCategoryName(category.getName());
                clientItems.add(clientItem);
            }
            client.setClientItems(clientItems);
            res.add(client);
        }
        return res;
    }

}
