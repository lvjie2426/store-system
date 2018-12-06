package com.store.system.service.impl;

import com.store.system.client.ClientInBill;
import com.store.system.client.ClientInBillItem;
import com.store.system.dao.InBillDao;
import com.store.system.dao.InBillItemDao;
import com.store.system.dao.UserDao;
import com.store.system.exception.GlassesException;
import com.store.system.model.InBill;
import com.store.system.model.InBillItem;
import com.store.system.model.User;
import com.store.system.service.InBillService;
import com.google.common.collect.Lists;
import com.s7.baseFramework.model.pagination.Pager;
import com.s7.ext.RowMapperHelp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class InBillServiceImpl implements InBillService {

    @Resource
    private UserDao userDao;

    @Resource
    private InBillDao inBillDao;

    @Resource
    private InBillItemDao inBillItemDao;

    private RowMapperHelp<InBill> rowMapper = new RowMapperHelp<>(InBill.class);

    @Resource
    protected JdbcTemplate jdbcTemplate;

    @Override
    public InBill add(InBill inBill) throws Exception {
        return inBillDao.insert(inBill);
    }

    @Override
    public boolean del(long id, long createUid) throws Exception {
        InBill inBill = inBillDao.load(id);
        if(null != inBill) {
            if(inBill.getCreateUid() != createUid) throw new GlassesException("入库单只能本人删除");
            if(inBill.getStatus() != InBill.status_edit) throw new GlassesException("状态错误不能删除");
            inBill.setStatus(InBill.status_delete);
            return inBillDao.update(inBill);
        }
        return false;
    }

    @Override
    public Pager getBackPager(Pager pager) throws Exception {
        pager = _getBackPager(pager, Lists.newArrayList(InBill.status_wait_check, InBill.status_end), 0);
        List<ClientInBill> clientInBills = transformClients(pager.getData());
        pager.setData(clientInBills);
        return pager;
    }

    @Override
    public Pager getBackWaitCheckPager(Pager pager) throws Exception {
        pager = _getBackPager(pager, Lists.newArrayList(InBill.status_wait_check), 0);
        List<ClientInBill> clientInBills = transformClients(pager.getData());
        pager.setData(clientInBills);
        return pager;
    }

    @Override
    public Pager getBackEndPager(Pager pager) throws Exception {
        pager = _getBackPager(pager, Lists.newArrayList(InBill.status_end), 0);
        List<ClientInBill> clientInBills = transformClients(pager.getData());
        pager.setData(clientInBills);
        return pager;
    }

    @Override
    public Pager getBackPagerByCreateUid(Pager pager, long createUid) throws Exception {
        pager = _getBackPager(pager, Lists.newArrayList(InBill.status_edit, InBill.status_wait_check, InBill.status_end), createUid);
        List<ClientInBill> clientInBills = transformClients(pager.getData());
        pager.setData(clientInBills);
        return pager;
    }

    @Override
    public Pager getBackEditPagerByCreateUid(Pager pager, long createUid) throws Exception {
        pager = _getBackPager(pager, Lists.newArrayList(InBill.status_edit), createUid);
        List<ClientInBill> clientInBills = transformClients(pager.getData());
        pager.setData(clientInBills);
        return pager;
    }

    @Override
    public Pager getBackWaitCheckPagerByCreateUid(Pager pager, long createUid) throws Exception {
        pager = _getBackPager(pager, Lists.newArrayList(InBill.status_wait_check), createUid);
        List<ClientInBill> clientInBills = transformClients(pager.getData());
        pager.setData(clientInBills);
        return pager;
    }

    @Override
    public Pager getBackEndPagerByCreateUid(Pager pager, long createUid) throws Exception {
        pager = _getBackPager(pager, Lists.newArrayList(InBill.status_end), createUid);
        List<ClientInBill> clientInBills = transformClients(pager.getData());
        pager.setData(clientInBills);
        return pager;
    }

    private List<ClientInBill> transformClients(List<InBill> inBills) throws Exception {
        List<ClientInBill> res = Lists.newArrayList();
        for(InBill inBill : inBills) {
            int itemCount = inBillItemDao.getCount(inBill.getId());
            List<InBillItem> items = inBillItemDao.getAllList(inBill.getId());
            int totalNum = 0;
            List<ClientInBillItem> clientInBillItems = Lists.newArrayList();
            for(InBillItem item : items) {
                ClientInBillItem clientInBillItem = new ClientInBillItem();
                clientInBillItem.setName(item.getName());
                clientInBillItem.setNum(item.getNum());
                clientInBillItems.add(clientInBillItem);
                totalNum += item.getNum();
            }
            User inUser = null;
            if(inBill.getInUid() > 0) inUser = userDao.load(inBill.getInUid());
            ClientInBill clientInBill = new ClientInBill(inBill);
            if(null != inUser) clientInBill.setInUserName(inUser.getName());
            clientInBill.setItemCount(itemCount);
            clientInBill.setItems(clientInBillItems);
            clientInBill.setTotalNum(totalNum);
            res.add(clientInBill);
        }
        return res;
    }

    private Pager _getBackPager(Pager pager, List<Integer> statusList, long createUid) throws Exception {
        String statusParam = StringUtils.join(statusList.toArray(), ",");

        String sql = "select * from in_bill where `status` in (" + statusParam + ") " ;
        String countSql = "select count(id) from in_bill where `status` in (" + statusParam + ") ";
        String limit = "  limit %d , %d ";
        if(createUid > 0){
            sql += " and createUid = " + createUid;
            countSql += " and createUid = " + createUid;
        }
        sql += " order by ctime desc ";
        sql = sql + String.format(limit,pager.getSize() * (pager.getPage()-1), pager.getSize());
        List<InBill> inBills = jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForInt(countSql);
        pager.setData(inBills);
        pager.setTotalCount(count);
        return pager;
    }

}
