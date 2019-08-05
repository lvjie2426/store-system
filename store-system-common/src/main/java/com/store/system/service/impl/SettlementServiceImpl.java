package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.store.system.client.ClientSettlement;
import com.store.system.dao.SettlementDao;
import com.store.system.dao.SubordinateDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Settlement;
import com.store.system.model.Subordinate;
import com.store.system.model.User;
import com.store.system.service.SettlementService;
import com.store.system.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName SettlementServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/7/8 16:07
 * @Version 1.0
 **/
@Service
public class SettlementServiceImpl implements SettlementService {

    @Resource
    private SettlementDao settlementDao;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private SubordinateDao subordinateDao;
    @Resource
    private UserService userService;



    private TransformMapUtils subordinateMapUtils = new TransformMapUtils(Subordinate.class);
    private TransformMapUtils userMapUtils = new TransformMapUtils(User.class);

    private void check(Settlement settlement) throws StoreSystemException {
        if (settlement.getSubId() == 0) throw new StoreSystemException("门店不能为空");
        if (settlement.getOptId() == 0) throw new StoreSystemException("结算人不能为空");
        if (settlement.getSale() < 0) throw new StoreSystemException("销售额不能为空");
        if (settlement.getCash() < 0) throw new StoreSystemException("现金不能为空");
        if (settlement.getAli() < 0) throw new StoreSystemException("阿里支付金额不能为空");
        if (settlement.getWx() < 0) throw new StoreSystemException("微信支付金额不能为空");
        if (settlement.getOther() < 0) throw new StoreSystemException("其他不能为空");
        if (settlement.getTotal() < 0) throw new StoreSystemException("总金额不能为空");
        if (settlement.getObligate() < 0) throw new StoreSystemException("预留金不能为空");
        if (settlement.getPayMoney() < 0) throw new StoreSystemException("上缴现金不能为空");
        if (settlement.getNum() <= 0) throw new StoreSystemException("现金单数不能为空");
        if (settlement.getEndTime() <= 0) throw new StoreSystemException("结算截止时间不能为空");
    }

    @Override
    public Settlement insert(Settlement settlement) throws Exception {
        check(settlement);
//        if(settlement.getEndTime()-settlement.getStartTime()<=3600000){
//            throw new StoreSystemException("请注意:距离上次结算时间还不到1小时！");
//        }
        settlement = settlementDao.insert(settlement);
        return settlement;
    }

    @Override
    public ClientSettlement loadClient(long subId) throws Exception {
        List<Settlement> settlements = settlementDao.getAllList(subId);
        Settlement settlement=new Settlement();
        if(settlements.size()>0){
            settlement = settlements.get(0);
        }
        return transformClient(settlement);
    }

    @Override
    public Pager getPager(Pager pager, long subId) throws Exception{
        String sql = "SELECT  *  FROM `settlement`   where  1=1 ";
        String sqlCount = "SELECT  COUNT(*)  FROM `settlement` where 1=1";
        String limit = "  limit %d , %d ";
        if(subId>0){
            sql = sql + " and subId = " + subId;
            sqlCount = sqlCount + " and subId = " + subId;
        }
        sql = sql + " order  by ctime desc";
        int count = 0;
        sql = sql + String.format(limit, pager.getSize() * (pager.getPage() - 1), pager.getSize());
        List<Settlement> settlements = jdbcTemplate.query(sql,new HyperspaceBeanPropertyRowMapper<Settlement>(Settlement.class));
        count=this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        pager.setData(transformClients(settlements));
        pager.setTotalCount(count);
        return pager;
    }


    private List<ClientSettlement> transformClients(List<Settlement> settlements) throws Exception {
        List<ClientSettlement> res = Lists.newArrayList();
        if (settlements.size() == 0) return res;
        Set<Long> subIds = Sets.newHashSet();
        Set<Long> uids = Sets.newHashSet();
        for (Settlement one : settlements) {
            subIds.add(one.getSubId());
            uids.add(one.getOptId());
        }
        List<Subordinate> subordinates = subordinateDao.load(Lists.newArrayList(subIds));
        Map<Long, Subordinate> subordinateMap = subordinateMapUtils.listToMap(subordinates, "id");
        List<User> users = userService.load(Lists.newArrayList(uids));
        Map<Long, User> userMap = userMapUtils.listToMap(users, "id");

        for (Settlement one : settlements) {
            ClientSettlement client = new ClientSettlement(one);
            User user = userMap.get(client.getOptId());
            Subordinate subordinate = subordinateMap.get(client.getSubId());
            if (null != subordinate && null != user) {
                client.setSubName(subordinate.getName());
                client.setUserName(user.getName());
            }
            res.add(client);
        }
        return res;
    }

    private ClientSettlement transformClient(Settlement settlement) throws Exception {
        ClientSettlement client = new ClientSettlement(settlement);
        Subordinate subordinate = subordinateDao.load(settlement.getSubId());
        if (null != subordinate) {
            client.setSubName(subordinate.getName());
        }
        User user = userService.load(settlement.getOptId());
        if (null != user) {
            client.setUserName(user.getName());
        }
        Settlement last = null;
        List<Settlement> lists = settlementDao.getAllList(settlement.getSubId());
        if (lists.size() > 0) {
            if (lists.size() == 1) {
                last = lists.get(0);
            } else {
                last = lists.get(1);
            }
            if (null != last) {
                client.setLast(last);
                SimpleDateFormat sdf = new SimpleDateFormat("MMdd HH:mm");
                String startTime = sdf.format(last.getStartTime());
                String endTime = sdf.format(last.getEndTime());
                client.setLastStartTime(startTime);
                client.setLastEndTime(endTime);
            }
        }
        return client;
    }
}
