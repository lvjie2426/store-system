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
import com.store.system.bean.InventoryCheckBillItem;
import com.store.system.client.*;
import com.store.system.dao.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.InventoryCheckBillService;
import com.store.system.service.ProductService;
import com.store.system.service.SubordinateService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class InventoryCheckBillServiceImpl implements InventoryCheckBillService {

    private TransformFieldSetUtils sidFieldSetUtils = new TransformFieldSetUtils(Subordinate.class);

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
    private SubordinateService subordinateService;

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
    private ProductService productService;

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
//            if(item.getRealNum() != 0) throw new StoreSystemException("条目明细错误");
        }
    }

    @Override
    @Transactional
    public InventoryCheckBill add(InventoryCheckBill inventoryCheckBill, List<Long> list) throws Exception {
        check(inventoryCheckBill);
        if(list.size()==0){
            throw new StoreSystemException("门店不可以为空！");
        }
        for(Long li:list){
            inventoryCheckBill.setSubid(li);
            inventoryCheckBill = inventoryCheckBillDao.insert(inventoryCheckBill);
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
    public List<ClientInventoryCheckBill> load(long id) throws Exception {
        InventoryCheckBill checkBill = inventoryCheckBillDao.load(id);
        return transformClients(Lists.newArrayList(checkBill));
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

    @Override
    public Pager getWebCheckPager(Pager pager, final long psid, final long subid) throws Exception {
        return new PagerRequestService<InventoryCheckBill>(pager,0) {
            @Override
            public List<InventoryCheckBill> step1GetPageResult(String cursor, int size) throws Exception {
                return inventoryCheckBillDao.getPageList(subid, InventoryCheckBill.status_wait_check, Double.parseDouble(cursor), size);
            }

            @Override
            public int step2GetTotalCount() throws Exception {
                return 0;
            }

            @Override
            public List<InventoryCheckBill> step3FilterResult(List<InventoryCheckBill> list, PagerSession pagerSession) throws Exception {
                return list;
            }

            @Override
            public List<?> step4TransformData(List<InventoryCheckBill> list, PagerSession pagerSession) throws Exception {
                return transformClients(list);
            }
        }.getPager();
    }

    @Override
    public Pager getWebCreatePager(Pager pager, final long createUid) throws Exception {
        return new PagerRequestService<InventoryCheckBill>(pager,0) {
            @Override
            public List<InventoryCheckBill> step1GetPageResult(String cursor, int size) throws Exception {
                return  inventoryCheckBillDao.getCreatePageList(createUid,InventoryCheckBill.status_wait_check,Double.parseDouble(cursor),size);
            }

            @Override
            public int step2GetTotalCount() throws Exception {
                return 0;
            }

            @Override
            public List<InventoryCheckBill> step3FilterResult(List<InventoryCheckBill> list, PagerSession pagerSession) throws Exception {
                return list;
            }

            @Override
            public List<?> step4TransformData(List<InventoryCheckBill> list, PagerSession pagerSession) throws Exception {
                return transformClients(list);
            }
        }.getPager();
    }

    @Override
    public List<InventoryCheckBill> getListByStatus(long psid, long subid, int status) throws Exception {
        List<InventoryCheckBill> bills = Lists.newArrayList();
        if (psid > 0 && subid == 0) {
            List<ClientSubordinate> subordinates = subordinateService.getTwoLevelAllList(psid);
            Set<Long> sids = sidFieldSetUtils.fieldList(subordinates, "id");
            for (Long sid : sids) {
                bills.addAll(inventoryCheckBillDao.getAllList(sid, status));
            }
        } else if (psid > 0 && subid > 0) {
            bills.addAll(inventoryCheckBillDao.getAllList(subid, status));
        }
        return bills;
    }

    private List<ClientInventoryCheckBill> transformClient(List<InventoryCheckBill> list) {
        List<ClientInventoryCheckBill> lists =new ArrayList<>();
        for(InventoryCheckBill inventoryCheckBill:list){
            ClientInventoryCheckBill clientInventoryCheckBill=new ClientInventoryCheckBill(inventoryCheckBill);
            if(inventoryCheckBill.getStatus()!=InventoryCheckBill.status_edit){
                User load = userDao.load(inventoryCheckBill.getInitUid());
                if(load!=null){
                    clientInventoryCheckBill.setInitUserName(load.getUserName());
                }
                lists.add(clientInventoryCheckBill);
            }
        }
        return lists;
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
                        clientItem.setEyeType(sku.getEyeType());
                        clientItem.setProperties(sku.getProperties());
                        Map<Object,Object> map_value = Maps.newHashMap();
                        map_value = productService.getProperties(sku,clientItem,"k_properties_value");
                        clientItem.setK_properties_value(map_value);
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
