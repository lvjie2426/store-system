package com.store.system.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.s7.baseFramework.jackson.JsonUtils;
import com.s7.baseFramework.model.pagination.Pager;
import com.s7.baseFramework.transform.TransformFieldSetUtils;
import com.s7.baseFramework.transform.TransformMapUtils;
import com.s7.ext.RowMapperHelp;
import com.store.system.bean.InventoryOutBillItem;
import com.store.system.client.ClientInventoryInBill;
import com.store.system.client.ClientInventoryOutBill;
import com.store.system.dao.InventoryDetailDao;
import com.store.system.dao.InventoryOutBillDao;
import com.store.system.dao.InventoryWarehouseDao;
import com.store.system.dao.UserDao;
import com.store.system.exception.GlassesException;
import com.store.system.model.*;
import com.store.system.service.InventoryOutBillService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class InventoryOutBillServiceImpl implements InventoryOutBillService {

    private ReentrantLock lock = new ReentrantLock();

    private TransformFieldSetUtils itemFieldSetUtils = new TransformFieldSetUtils(InventoryOutBillItem.class);

    private TransformMapUtils detailMapUtils = new TransformMapUtils(InventoryDetail.class);

    private TransformMapUtils userMapUtils = new TransformMapUtils(User.class);

    private TransformMapUtils warehouseMapUtils = new TransformMapUtils(InventoryWarehouse.class);

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
    private JdbcTemplate jdbcTemplate;

    @Override
    public InventoryOutBill add(InventoryOutBill inventoryOutBill) throws Exception {
        check(inventoryOutBill);
        return inventoryOutBillDao.insert(inventoryOutBill);
    }

    @Override
    public boolean update(InventoryOutBill inventoryOutBill) throws Exception {
        int status = inventoryOutBill.getStatus();
        if(status != InventoryOutBill.status_edit) throw new GlassesException("状态错误,不能修改");
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
    public void pass(long id, long checkUid) throws Exception {
        lock.lock();
        try {
            InventoryOutBill outBill = inventoryOutBillDao.load(id);
            if(null == outBill) throw new GlassesException("出库单为空");
            if(outBill.getStatus() != InventoryOutBill.status_wait_check) throw new GlassesException("出库单状态错误");
            String itemsJson = outBill.getItemsJson();
            List<InventoryOutBillItem> items = JsonUtils.fromJson(itemsJson, new TypeReference<List<InventoryOutBillItem>>() {});
            Set<Long> dids = itemFieldSetUtils.fieldList(items, "did");
            List<InventoryDetail> details = inventoryDetailDao.load(Lists.newArrayList(dids));
            Map<Long, InventoryDetail> detailMap = detailMapUtils.listToMap(details, "id");
            for(InventoryOutBillItem item : items) {
                InventoryDetail detail = detailMap.get(item.getDid());
                if(item.getNum() > detail.getNum()) throw new GlassesException("库存数量不足");
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
        if(null == outBill) throw new GlassesException("出库单为空");
        if(outBill.getStatus() != InventoryOutBill.status_wait_check) throw new GlassesException("出库单状态错误");
        outBill.setCheckUid(checkUid);
        outBill.setCheck(InventoryOutBill.check_no_pass);
        outBill.setStatus(InventoryOutBill.status_end);
        inventoryOutBillDao.update(outBill);
    }

    private void check(InventoryOutBill inventoryOutBill) throws Exception {
        long wid = inventoryOutBill.getWid();
        if(wid == 0) throw new GlassesException("仓库不能为空");
        if(inventoryOutBill.getOutUid() == 0) throw new GlassesException("出库人不能为空");
        if(inventoryOutBill.getCreateUid() == 0) throw new GlassesException("创建人不能为空");
        String itemsJson = inventoryOutBill.getItemsJson();
        List<InventoryOutBillItem> items = null;
        try {
            items = JsonUtils.fromJson(itemsJson, new TypeReference<List<InventoryOutBillItem>>() {});
        } catch (Exception e) {
            throw new GlassesException("出库单子项目格式错误");
        }
        Map<String, Integer> codeNumMap = Maps.newHashMap();
        for(InventoryOutBillItem item : items) {
            Integer num = codeNumMap.get(item.getCode());
            if(null == num) {
                codeNumMap.put(item.getCode(), 1);
            } else {
                codeNumMap.put(item.getCode(), num + 1);
            }
        }

        Set<Long> dids = itemFieldSetUtils.fieldList(items, "did");
        List<InventoryDetail> details = inventoryDetailDao.load(Lists.newArrayList(dids));
        Map<Long, InventoryDetail> detailMap = detailMapUtils.listToMap(details, "id");
        for(InventoryOutBillItem item : items) {
            if(item.getNum() <= 0) throw new GlassesException("出库单子项目出库数错误");
            if(codeNumMap.get(item.getCode()) > 1) throw new GlassesException("已经添加相同子项目进出库单");
            InventoryDetail detail = detailMap.get(item.getDid());
            if(null == detail) throw new GlassesException("库存明细为空");
            if(item.getNum() > detail.getNum()) throw new GlassesException("库存数量不足");
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
        int count = this.jdbcTemplate.queryForInt(sqlCount);
        List<ClientInventoryOutBill> data = transformClients(outBills);
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public Pager getCheckPager(Pager pager) throws Exception {
        String sql = "SELECT * FROM `inventory_out_bill` where `status` > " + InventoryOutBill.status_edit;
        String sqlCount = "SELECT COUNT(id) FROM `inventory_out_bill` where `status` > " + InventoryOutBill.status_edit;
        String limit = " limit %d , %d ";
        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<InventoryOutBill> outBills = this.jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForInt(sqlCount);
        List<ClientInventoryOutBill> data = transformClients(outBills);
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

    private List<ClientInventoryOutBill> transformClients(List<InventoryOutBill> outBills) throws Exception {
        List<ClientInventoryOutBill> res = Lists.newArrayList();
        Set<Long> uids = Sets.newHashSet();
        Set<Long> wids = Sets.newHashSet();
        for(InventoryOutBill outBill : outBills) {
            if(outBill.getWid() > 0) wids.add(outBill.getWid());
            if(outBill.getCreateUid() > 0) uids.add(outBill.getCreateUid());
            if(outBill.getOutUid() > 0) uids.add(outBill.getOutUid());
            if(outBill.getCheckUid() > 0) uids.add(outBill.getCheckUid());
        }
        List<User> users = userDao.load(Lists.newArrayList(uids));
        Map<Long, User> userMap = userMapUtils.listToMap(users, "id");
        List<InventoryWarehouse> warehouses = inventoryWarehouseDao.load(Lists.newArrayList(wids));
        Map<Long, InventoryWarehouse> warehouseMap = warehouseMapUtils.listToMap(warehouses, "id");
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
            res.add(client);
        }
        return res;
    }

}
