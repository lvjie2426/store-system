package com.store.system.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.transform.TransformFieldSetUtils;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.bean.InventoryCheckBillItem;
import com.store.system.client.ClientInventoryCheckBill;
import com.store.system.client.ClientInventoryCheckBillItem;
import com.store.system.dao.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.InventoryCheckBillService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class InventoryCheckBillServiceImpl implements InventoryCheckBillService {

    private TransformMapUtils detailMapUtils = new TransformMapUtils(InventoryDetail.class);

    private TransformMapUtils itemMapUtils = new TransformMapUtils(InventoryCheckBillItem.class);

    private TransformFieldSetUtils itemFieldSetUtils = new TransformFieldSetUtils(InventoryCheckBillItem.class);

    private TransformMapUtils userMapUtils = new TransformMapUtils(User.class);

    private TransformMapUtils warehouseMapUtils = new TransformMapUtils(InventoryWarehouse.class);

    private TransformFieldSetUtils spuFieldSetUtils = new TransformFieldSetUtils(ProductSPU.class);

    private TransformFieldSetUtils detailFieldSetUtils = new TransformFieldSetUtils(InventoryDetail.class);

    private TransformMapUtils spuMapUtils = new TransformMapUtils(ProductSPU.class);

    private TransformMapUtils skuMapUtils = new TransformMapUtils(ProductSKU.class);

    private TransformMapUtils categoryMapUtils = new TransformMapUtils(ProductCategory.class);

    private TransformMapUtils brandMapUtils = new TransformMapUtils(ProductBrand.class);

    private TransformMapUtils seriesMapUtils = new TransformMapUtils(ProductSeries.class);

    private TransformMapUtils subMapUtils = new TransformMapUtils(Subordinate.class);

    private RowMapperHelp rowMapper = new RowMapperHelp(InventoryCheckBill.class);

    @Resource
    private InventoryDetailDao inventoryDetailDao;

    @Resource
    private InventoryCheckBillDao inventoryCheckBillDao;

    @Resource
    private InventoryWarehouseDao inventoryWarehouseDao;

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
    private UserDao userDao;

    @Resource
    private SubordinateDao subordinateDao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    private void check(InventoryCheckBill inventoryCheckBill) throws Exception {
        long subid = inventoryCheckBill.getSubid();
        if(subid == 0) throw new StoreSystemException("店铺不能为空");
        long wid = inventoryCheckBill.getWid();
        if(wid == 0) throw new StoreSystemException("仓库不能为空");
        if(inventoryCheckBill.getInitUid() == 0) throw new StoreSystemException("发起人不能为空");
        if(inventoryCheckBill.getCreateUid() == 0) throw new StoreSystemException("创建人不能为空");
        List<InventoryCheckBillItem> items = inventoryCheckBill.getItems();
        Set<Long> dids = itemFieldSetUtils.fieldList(items, "did");
        List<InventoryDetail> details = inventoryDetailDao.load(Lists.newArrayList(dids));
        Map<Long, InventoryDetail> detailMap = detailMapUtils.listToMap(details, "id");
        for(InventoryCheckBillItem item : items) {
            InventoryDetail detail = detailMap.get(item.getDid());
            if(null == detail) throw new StoreSystemException("库存明细为空");
            if(detail.getWid() != wid) throw new StoreSystemException("盘点单子项目仓库错误");
            if(item.getCurrentNum() != detail.getNum()) throw new StoreSystemException("条目明细错误");
            if(item.getRealNum() != 0) throw new StoreSystemException("条目明细错误");
        }
    }

    @Override
    @Transactional
    public InventoryCheckBill add(InventoryCheckBill inventoryCheckBill, List<Long> list) throws Exception {
        check(inventoryCheckBill);
        for(Long li:list){
            inventoryCheckBill.setSubid(li);
            inventoryCheckBillDao.insert(inventoryCheckBill);
        }
        return inventoryCheckBill;
    }

    @Override
    public boolean update(InventoryCheckBill inventoryCheckBill) throws Exception {
        InventoryCheckBill dbCheckBill = inventoryCheckBillDao.load(inventoryCheckBill.getId());
        int status = dbCheckBill.getStatus();
        if(status != InventoryCheckBill.status_edit) throw new StoreSystemException("状态错误,不能修改");
        check(inventoryCheckBill);
        boolean res = inventoryCheckBillDao.update(inventoryCheckBill);
        return res;
    }

    @Override
    public boolean del(long id) throws Exception {
        InventoryCheckBill checkBill = inventoryCheckBillDao.load(id);
        if(null != checkBill && checkBill.getStatus() == InventoryCheckBill.status_edit) {
            return inventoryCheckBillDao.delete(id);
        }
        return false;
    }

    @Override
    public boolean submit(long id) throws Exception {
        InventoryCheckBill checkBill = inventoryCheckBillDao.load(id);
        if(null != checkBill && checkBill.getStatus() == InventoryCheckBill.status_edit) {
            checkBill.setStatus(InventoryCheckBill.status_wait_check);
            return inventoryCheckBillDao.update(checkBill);
        }
        return false;
    }

    @Override
    public boolean end(long id, long checkUid) throws Exception {
        InventoryCheckBill checkBill = inventoryCheckBillDao.load(id);
        if(null != checkBill && checkBill.getStatus() == InventoryCheckBill.status_wait_check) {
            checkBill.setStatus(InventoryCheckBill.status_end);
            checkBill.setCheckUid(checkUid);
            return inventoryCheckBillDao.update(checkBill);
        }
        return false;
    }

    @Override
    public boolean save(InventoryCheckBill inventoryCheckBill) throws Exception {
        InventoryCheckBill dbCheckBill = inventoryCheckBillDao.load(inventoryCheckBill.getId());
        int status = dbCheckBill.getStatus();
        if(status != InventoryCheckBill.status_wait_check) throw new StoreSystemException("状态错误,不能盘点");
        if(dbCheckBill.getWid() != inventoryCheckBill.getWid()) throw new StoreSystemException("仓库错误");
        if(dbCheckBill.getInitUid() != inventoryCheckBill.getInitUid()) throw new StoreSystemException("发起人错误");
        if(dbCheckBill.getCreateUid() != inventoryCheckBill.getCreateUid()) throw new StoreSystemException("创建人错误");
        List<InventoryCheckBillItem> items = inventoryCheckBill.getItems();
        List<InventoryCheckBillItem> dbItems = dbCheckBill.getItems();
        Map<Long, InventoryCheckBillItem> dbItemMap = itemMapUtils.listToMap(dbItems, "did");
        for(InventoryCheckBillItem item : items) {
            InventoryCheckBillItem dbItem = dbItemMap.get(item.getDid());
            if(item.getCurrentNum() != dbItem.getCurrentNum()) throw new StoreSystemException("子条目错误");
        }
        boolean res = inventoryCheckBillDao.update(inventoryCheckBill);
        return res;
    }

    @Override
    public Pager getCreatePager(Pager pager, long createUid) throws Exception {
        String sql = "SELECT * FROM `inventory_check_bill` where createUid = " + createUid;
        String sqlCount = "SELECT COUNT(id) FROM `inventory_check_bill` where createUid = " + createUid;
        String limit = " limit %d , %d ";
        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<InventoryCheckBill> checkBills = this.jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        List<ClientInventoryCheckBill> data = transformClients(checkBills);
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public Pager getCheckPager(Pager pager, long subid) throws Exception {
        String sql = "SELECT * FROM `inventory_check_bill` where subid = " + subid + " and `status` > " + InventoryCheckBill.status_edit;
        String sqlCount = "SELECT COUNT(id) FROM `inventory_check_bill` where subid = " + subid + " and `status` > " + InventoryCheckBill.status_edit;
        String limit = " limit %d , %d ";
        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<InventoryCheckBill> checkBills = this.jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        List<ClientInventoryCheckBill> data = transformClients(checkBills);
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public InventoryCheckBill getEndById(long id) throws Exception {
        return inventoryCheckBillDao.load(id);
    }

    private List<ClientInventoryCheckBill> transformClients(List<InventoryCheckBill> checkBills) throws Exception {
        List<ClientInventoryCheckBill> res = Lists.newArrayList();
        Set<Long> uids = Sets.newHashSet();
        Set<Long> wids = Sets.newHashSet();
        Set<Long> subids = Sets.newHashSet();
        for(InventoryCheckBill checkBill : checkBills) {
            if(checkBill.getWid() > 0) wids.add(checkBill.getWid());
            if(checkBill.getCreateUid() > 0) uids.add(checkBill.getCreateUid());
            if(checkBill.getInitUid() > 0) uids.add(checkBill.getInitUid());
            if(checkBill.getCheckUid() > 0) uids.add(checkBill.getCheckUid());
            if(checkBill.getSubid() > 0) subids.add(checkBill.getSubid());
        }
        List<User> users = userDao.load(Lists.newArrayList(uids));
        Map<Long, User> userMap = userMapUtils.listToMap(users, "id");
        List<InventoryWarehouse> warehouses = inventoryWarehouseDao.load(Lists.newArrayList(wids));
        Map<Long, InventoryWarehouse> warehouseMap = warehouseMapUtils.listToMap(warehouses, "id");
        List<Subordinate> subordinates = subordinateDao.load(Lists.newArrayList(subids));
        Map<Long, Subordinate> subordinateMap = subMapUtils.listToMap(subordinates, "id");
        for(InventoryCheckBill checkBill : checkBills) {
            ClientInventoryCheckBill client = new ClientInventoryCheckBill(checkBill);
            InventoryWarehouse warehouse = warehouseMap.get(client.getWid());
            if(null != warehouse) client.setWarehouseName(warehouse.getName());
            User user = userMap.get(client.getCreateUid());
            if(null != user) client.setCreateUserName(user.getName());
            user = userMap.get(client.getInitUid());
            if(null != user) client.setInitUserName(user.getName());
            user = userMap.get(client.getCheckUid());
            if(null != user) client.setCheckUserName(user.getName());
            Subordinate subordinate = subordinateMap.get(client.getSubid());
            if(null != subordinate) client.setSubName(subordinate.getName());
            List<InventoryCheckBillItem> items = checkBill.getItems();
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

            List<ClientInventoryCheckBillItem> clientItems = Lists.newArrayList();
            for(InventoryCheckBillItem item : items) {
                ClientInventoryCheckBillItem clientItem = new ClientInventoryCheckBillItem(item);
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
            client.setClientItems(clientItems);
            res.add(client);
        }
        return  res;
    }

}
