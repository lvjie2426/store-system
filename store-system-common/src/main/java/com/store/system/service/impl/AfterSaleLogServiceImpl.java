package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.store.system.bean.AfterSale;
import com.store.system.client.ClientAfterSaleLog;
import com.store.system.dao.AfterSaleDetailDao;
import com.store.system.dao.AfterSaleLogDao;
import com.store.system.dao.BusinessOrderDao;
import com.store.system.dao.OrderDao;
import com.store.system.model.*;
import com.store.system.service.AfterSaleLogService;
import com.store.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName AfterSaleLogServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/10 18:28
 * @Version 1.0
 **/
@Service
public class AfterSaleLogServiceImpl implements AfterSaleLogService{

    @Resource
    private AfterSaleLogDao afterSaleLogDao;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private BusinessOrderDao businessOrderDao;
    @Resource
    private AfterSaleDetailDao afterSaleDetailDao;
    @Resource
    private UserService userService;

    private TransformMapUtils userMapUtils = new TransformMapUtils(User.class);
    private TransformMapUtils orderMapUtils = new TransformMapUtils(BusinessOrder.class);

    @Override
    @Transactional
    public AfterSaleLog add(AfterSale afterSale) throws Exception {
        BusinessOrder order = businessOrderDao.load(afterSale.getOid());
        AfterSaleLog afterSaleLog = new AfterSaleLog();
        afterSaleLog.setOid(order.getId());
        afterSaleLog.setOrderNo(order.getOrderNo());
        afterSaleLog.setUid(order.getUid());
        afterSaleLog.setSalesUid(order.getStaffId());
        afterSaleLog.setSubId(afterSale.getSubId());

        //增加一条售后Detail
        AfterSaleDetail afterSaleDetail = new AfterSaleDetail();

        List<AfterSaleLog> logs = afterSaleLogDao.getList(order.getSubId(),order.getId());
        if(logs.size()==0){
            //第一次进行售后时 增加Log
            afterSaleLog =  afterSaleLogDao.insert(afterSaleLog);
            afterSaleDetail.setAsId(afterSaleLog.getId());
        }else{
            afterSaleDetail.setAsId(logs.get(0).getId());
        }

        afterSaleDetail.setOid(order.getId());
        afterSaleDetail.setSku(afterSale.getSku());
        afterSaleDetail.setReason(afterSale.getReason());
        afterSaleDetail.setOptId(afterSale.getOptId());
        afterSaleDetailDao.insert(afterSaleDetail);


        return afterSaleLog;
    }


    @Override
    public Pager getBackPager(Pager pager, long subId, String userName, String phone) throws Exception {
        String sql = "SELECT a.* FROM `afterSale_log` as a  LEFT JOIN `user` as u ON a.uid=u.id WHERE 1=1 ";
        String sqlCount = "SELECT COUNT(*) FROM `afterSale_log` as a  LEFT JOIN `user` as u ON a.uid=u.id WHERE 1=1 ";
        String limit = " limit %d , %d ";

        if(subId>0){
            sql = sql + " and a.`subId` = " + subId;
            sqlCount = sqlCount + " and a.`subId` = " + subId;
        }
        if (StringUtils.isNotBlank(userName)) {
            sql = sql + " and u.`name` like ?";
            sqlCount = sqlCount + " and u.`name` like ?";
        }
        if (StringUtils.isNotBlank(phone)) {
            sql = sql + " and u.`phone` like ?";
            sqlCount = sqlCount + " and u.`phone` like ?";
        }

        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<AfterSaleLog> afterSaleLogs =null;
        int count=0;
        List<Object> objects=new ArrayList<>();
        if(StringUtils.isNotBlank(userName)){objects.add("%"+userName+"%");}
        if(StringUtils.isNotBlank(phone)){objects.add("%"+phone+"%");}
        if(objects.size()>0) {
            Object[] args = new Object[objects.size()];
            objects.toArray(args);
            afterSaleLogs = jdbcTemplate.query(sql, new HyperspaceBeanPropertyRowMapper<AfterSaleLog>(AfterSaleLog.class),args);
            count = this.jdbcTemplate.queryForObject(sqlCount,Integer.class,args);
        }else{
            afterSaleLogs = jdbcTemplate.query(sql, new HyperspaceBeanPropertyRowMapper<AfterSaleLog>(AfterSaleLog.class));
            count = this.jdbcTemplate.queryForObject(sqlCount,Integer.class);
        }
        pager.setData(transformClient(afterSaleLogs));
        pager.setTotalCount(count);
        return pager;
    }

    private List<ClientAfterSaleLog> transformClient(List<AfterSaleLog> afterSaleLogs) throws Exception{
        List<ClientAfterSaleLog> clientAfterSaleLogs = Lists.newArrayList();
        Set<Long> uids = Sets.newHashSet();
        Set<Long> oids = Sets.newHashSet();
        for(AfterSaleLog afterSaleLog: afterSaleLogs){
            if(afterSaleLog.getUid()>0) {
                uids.add(afterSaleLog.getUid());
                uids.add(afterSaleLog.getSalesUid());
                oids.add(afterSaleLog.getOid());
            }
        }

        List<User> users = userService.load(Lists.newArrayList(uids));
        Map<Long, User> userMap = userMapUtils.listToMap(users, "id");
        // TODO: 2019/7/24
        List<BusinessOrder> orders = businessOrderDao.load(Lists.newArrayList(oids));
        Map<Long, BusinessOrder> orderMap = orderMapUtils.listToMap(orders, "id");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm");
        for(AfterSaleLog afterSaleLog: afterSaleLogs){
            ClientAfterSaleLog client = new ClientAfterSaleLog();
            client.setId(afterSaleLog.getId());
            client.setOrderNo(afterSaleLog.getOrderNo());
            client.setUserName(userMap.get(afterSaleLog.getUid()).getName());
            client.setUserPhone(userMap.get(afterSaleLog.getUid()).getPhone());
            client.setSalesName(userMap.get(afterSaleLog.getSalesUid()).getName());
            List<AfterSaleDetail> details = afterSaleDetailDao.getAllList(afterSaleLog.getId());
            if(details.size()>0) {
                client.setResaon(details.get(0).getReason());
                client.setLastTime(sdf.format(details.get(0).getCtime()));
                client.setTimes(details.size());
            }
            client.setStatus(orderMap.get(afterSaleLog.getOid()).getStatus());
            clientAfterSaleLogs.add(client);
        }
        return clientAfterSaleLogs;
    }

}
