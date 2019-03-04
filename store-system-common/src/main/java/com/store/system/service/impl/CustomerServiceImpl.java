package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.client.ClientCustomer;
import com.store.system.dao.CustomerDao;
import com.store.system.dao.SubordinateDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Customer;
import com.store.system.model.Subordinate;
import com.store.system.service.CustomerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CustomerServiceImpl implements CustomerService {

    private RowMapperHelp<Customer> rowMapper = new RowMapperHelp<>(Customer.class);

    private TransformMapUtils subMapUtils = new TransformMapUtils(Subordinate.class);

    @Resource
    private CustomerDao customerDao;

    @Resource
    private SubordinateDao subordinateDao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    private void check(Customer customer) throws StoreSystemException {
        String name = customer.getName();
        if(StringUtils.isBlank(name)) throw new StoreSystemException("姓名不能为空");
        String phone = customer.getPhone();
        if(StringUtils.isBlank(phone)) throw new StoreSystemException("手机号不能为空");
    }

    @Override
    public Customer add(Customer customer) throws Exception {
        check(customer);
        long subid = customer.getSubid();
        Subordinate subordinate = subordinateDao.load(subid);
        if(subordinate.getPid() == 0) throw new StoreSystemException("公司ID错误");
        customer.setpSubid(subordinate.getPid());
        customer = customerDao.insert(customer);
        return customer;
    }

    @Override
    public boolean update(Customer customer) throws Exception {
        check(customer);
        long subid = customer.getSubid();
        Subordinate subordinate = subordinateDao.load(subid);
        if(subordinate.getPid() == 0) throw new StoreSystemException("公司ID错误");
        customer.setpSubid(subordinate.getPid());
        boolean res = customerDao.update(customer);
        return res;
    }

    @Override
    public boolean del(long id) throws Exception {
        Customer customer = customerDao.load(id);
        if(null != customer) {
            customer.setStatus(Customer.status_delete);
            return customerDao.update(customer);
        }
        return false;
    }

    private List<ClientCustomer> transformClients(List<Customer> customers) throws Exception {
        Set<Long> sids = Sets.newHashSet();
        for(Customer customer : customers) {
            if(customer.getpSubid() > 0) sids.add(customer.getpSubid());
            if(customer.getSubid() > 0) sids.add(customer.getSubid());
        }
        List<Subordinate> subordinates = subordinateDao.load(Lists.newArrayList(sids));
        Map<Long, Subordinate> subordinateMap = subMapUtils.listToMap(subordinates, "id");
        List<ClientCustomer> res = Lists.newArrayList();
        for(Customer customer : customers) {
            ClientCustomer clientCustomer = new ClientCustomer(customer);
            if(clientCustomer.getpSubid() > 0) {
                Subordinate subordinate = subordinateMap.get(clientCustomer.getpSubid());
                if(subordinate != null) clientCustomer.setpSubName(subordinate.getName());
            }
            if(clientCustomer.getSubid() > 0) {
                Subordinate subordinate = subordinateMap.get(clientCustomer.getSubid());
                if(subordinate != null) clientCustomer.setSubName(subordinate.getName());
            }
            res.add(clientCustomer);
        }
        return res;
    }

    @Override
    public Pager getBackPager(Pager pager, long pSubid) throws Exception {
        String sql = "SELECT * FROM `customer` where pSubid = " + pSubid + " and `status` = " + Customer.status_nomore;
        String sqlCount = "SELECT COUNT(id) FROM `customer` where pSubid = " + pSubid + " and `status` = " + Customer.status_nomore;
        String limit = " limit %d , %d ";
        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<Customer> customers = this.jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        List<ClientCustomer> data = transformClients(customers);
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public Pager getBackSubPager(Pager pager, long subid) throws Exception {
        String sql = "SELECT * FROM `customer` where subid = " + subid + " and `status` = " + Customer.status_nomore;
        String sqlCount = "SELECT COUNT(id) FROM `customer` where subid = " + subid + " and `status` = " + Customer.status_nomore;
        String limit = " limit %d , %d ";
        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<Customer> customers = this.jdbcTemplate.query(sql, rowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        List<ClientCustomer> data = transformClients(customers);
        pager.setData(data);
        pager.setTotalCount(count);
        return pager;
    }
}
