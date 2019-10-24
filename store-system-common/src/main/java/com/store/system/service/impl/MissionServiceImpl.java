package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.ext.RowMapperHelp;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.store.system.client.ClientMission;
import com.store.system.client.Personal;
import com.store.system.dao.*;
import com.store.system.model.*;
import com.store.system.service.*;
import com.store.system.util.DateUtils;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MissionServiceImpl implements MissionService {

    @Resource
    private MissionDao missionDao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private UserMissionPoolService userMissionPoolService;

    @Resource
    private SubordinateMissionPoolService subordinateMissionPoolService;

    @Resource
    private SubordinateMissionPoolDao subordinateMissionPoolDao;

    @Resource
    private UserMissionPoolDao userMissionPoolDao;

    @Resource
    private UserDao userDao;
    @Resource
    private SubordinateDao subordinateDao;

    @Override
    @Transactional
    public Mission insert(Mission mission) throws Exception {
        mission = missionDao.insert(mission);
        if (mission.getType() == Mission.type_tem) {
            //团队任务
            List<Long> sids = mission.getExecutor();
            for (Long id : sids) {
                SubordinateMissionPool subordinateMissionPool = new SubordinateMissionPool();
                subordinateMissionPool.setMid(mission.getId());
                subordinateMissionPool.setSid(id);
                subordinateMissionPoolDao.insert(subordinateMissionPool);
            }
        } else if (mission.getType() == Mission.type_user) {
            //个人任务
            List<Long> uids = mission.getExecutor();
            for (Long id : uids) {
                UserMissionPool userMissionPool = new UserMissionPool();
                userMissionPool.setMid(mission.getId());
                userMissionPool.setUid(id);
                userMissionPoolDao.insert(userMissionPool);
            }
        }
        return mission;
    }

    @Override
    public boolean update(Mission mission) throws Exception {
        Mission dbMission = missionDao.load(mission.getId());
        //修改执行任务对象
        if (!isLists(mission.getExecutor(), dbMission.getExecutor()) && mission.getExecutor().size() > 0) {
            dbMission.setExecutor(mission.getExecutor());
        }
        if (StringUtils.isNotBlank(mission.getName()) && mission.getName() != dbMission.getName()) {
            dbMission.setName(mission.getName());
        }
        if (mission.getAmount() > 0 && mission.getAmount() != dbMission.getAmount()) {
            dbMission.setAmount(mission.getAmount());
        }
        //修改任务类型
        if (mission.getAmountType() > 0 && mission.getAmountType() != dbMission.getAmountType()) {
            dbMission.setAmountType(mission.getAmountType());
        }
        if (mission.getStartTime() > 0 && mission.getStartTime() != dbMission.getStartTime()) {
            dbMission.setStartTime(mission.getStartTime());
        }
        if (mission.getEndTime() > 0 && mission.getEndTime() != dbMission.getEndTime()) {
            dbMission.setEndTime(mission.getEndTime());
        }
        if (mission.getType() > 0 && mission.getType() != dbMission.getType()) {
            dbMission.setType(mission.getType());
        }
        if (mission.getTarget() > 0 && mission.getTarget() != dbMission.getTarget()) {
            dbMission.setTarget(mission.getTarget());
        }
        if (!isLists(mission.getSkuIds(), dbMission.getSkuIds()) && mission.getSkuIds().size() > 0) {
            dbMission.setSkuIds(mission.getSkuIds());
        }
        boolean flag = missionDao.update(mission);
        if (flag) {
            List<Long> executor = dbMission.getExecutor();
            //新添加的执行任务对象  添加中间表
            if (dbMission.getType() == Mission.type_tem) {
                if (executor.size() > 0) {
                    //循环结束没有添加中间表的进行增加操作
                    for (Long id : executor) {
                        SubordinateMissionPool missionPool = new SubordinateMissionPool();
                        missionPool.setSid(id);
                        missionPool.setMid(dbMission.getId());
                        SubordinateMissionPool dbInfo = subordinateMissionPoolDao.load(missionPool);
                        if (dbInfo == null) {
                            subordinateMissionPoolDao.insert(missionPool);
                        }
                    }
                }
            } else if (dbMission.getType() == Mission.type_user) {
                if (executor.size() > 0) {
                    //循环结束没有添加中间表的进行增加操作
                    for (Long id : executor) {
                        UserMissionPool missionPool = new UserMissionPool();
                        missionPool.setUid(id);
                        missionPool.setMid(dbMission.getId());
                        userMissionPoolDao.insert(missionPool);
                        UserMissionPool dbInfo = userMissionPoolDao.load(missionPool);
                        if (dbInfo == null) {
                            userMissionPoolDao.insert(missionPool);
                        }
                    }
                }
            }
        }
        return flag;
    }

    @Override
    public boolean del(long id) throws Exception {
        Mission mission = missionDao.load(id);
        if (mission != null) {
            mission.setStatus(Mission.status_no);//删除状态
            return missionDao.update(mission);
        }
        return false;
    }

    @Override
    public Pager getByPager(Pager pager, long sid) throws Exception {
        String sql = "SELECT * FROM `mission` where sid = " + sid + " AND status = " + Mission.status_yes;
        String sqlCount = "SELECT COUNT(id) FROM `mission` where sid = " + sid + " AND status = " + Mission.status_yes;
        String limit = " limit %d , %d ";
        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        int count = 0;
        List<Mission> missions = jdbcTemplate.query(sql, new RowMapperHelp<Mission>(Mission.class));
        count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        pager.setData(transformClient(missions));
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public Map<String, Object> getAllMission(long sid) throws Exception {
        Map<String, Object> map = Maps.newHashMap();
        String sql = "SELECT * FROM `mission` where sid = " + sid + " AND status = " + Mission.status_yes;
        sql = sql + " order  by `ctime` desc";
        List<Mission> missions = jdbcTemplate.query(sql, new RowMapperHelp<Mission>(Mission.class));
        int money = 0;//总奖励额
        List<ClientMission> clientList = transformClient(missions);
        for (ClientMission client : clientList) {
            money += client.getAllAmount();
        }
        map.put("list", clientList);
        map.put("money", money);
        return map;
    }

    @Override

    public Map<String, Object> getAllMissionApp(long sid, Date date) throws Exception {
        long end = DateUtils.getMonthEnd(date);
        long start = DateUtils.getMonthBegin(date);
        Map<String, Object> map = Maps.newHashMap();
        String sql = "SELECT * FROM `mission` where sid = " + sid + " AND status = " + Mission.status_yes;
        sql = sql + " and `ctime` <" + end + " and `ctime` >" + start;
        sql = sql + " order  by `ctime` desc";
        List<Mission> missions = jdbcTemplate.query(sql, new RowMapperHelp<Mission>(Mission.class));
        int money = 0;//总奖励额
        List<ClientMission> clientList = transformClient(missions);
        for (ClientMission client : clientList) {
            money += client.getAllAmount();
        }
        map.put("list", clientList);
        map.put("money", money);
        return map;
    }

    //检查当前订单满足那些任务的完成条件并返回任务
    @Override
    public List<Mission> checkMission(long skuId, long sid, long subid, long uid) throws Exception {
        String sql = "SELECT * FROM `mission` where sid = " + sid + " and `endTime` > " + System.currentTimeMillis();
        sql = sql + " and `startTime` <" + System.currentTimeMillis() + " and missionStatus = " + Mission.missionStatus_nofinish;
        sql = sql + " order  by `ctime` desc";
        List<Mission> missions = this.jdbcTemplate.query(sql, new HyperspaceBeanPropertyRowMapper<Mission>(Mission.class));
        List<Mission> res = Lists.newArrayList();
        if (missions.size() > 0) {
            for (Mission mission : missions) {
                List<Long> ids = mission.getExecutor();
                List<Long> skus = mission.getSkuIds();
                if (mission.getType() == Mission.type_tem) {
                    //团队任务:当前任务包含店铺ID并且包含skuID，保存该任务。
                    if (ids.contains(subid) || skus.contains(skuId)) {
                        res.add(mission);
                    }
                } else if (mission.getType() == Mission.type_user) {
                    if (ids.contains(uid) || skus.contains(skuId)) {
                        res.add(mission);
                    }
                }
            }
        }
        return res;
    }

    @Override
    public Pager getWebByPager(Pager pager, long psid, int missionStatus, int type, int isNow) throws Exception {
        String sql = "SELECT * FROM `mission` where sid = " + psid + " AND status = " + Mission.status_yes;
        String sqlCount = "SELECT COUNT(id) FROM `mission` where sid = " + psid + " AND status = " + Mission.status_yes;
        String limit = " limit %d , %d ";

        if (missionStatus > 0) {
            //完成/未完成
            sql = sql + " and `missionStatus` = " + missionStatus;
            sqlCount = sqlCount + " and `missionStatus` = " + missionStatus;

        }
        if (type > 0) {
            // 团队/个人
            sql = sql + " and `type` = " + type;
            sqlCount = sqlCount + " and `type` = " + type;

        }
        if (isNow == 0) {
            //任务进行中
            sql = sql + " and `endTime` > " + System.currentTimeMillis() + " and `startTime` <" + System.currentTimeMillis();
            sqlCount = sqlCount + " and `endTime` > " + System.currentTimeMillis() + " and `startTime` <" + System.currentTimeMillis();
        }
        if (isNow == 1) {
            //任务已经结束
            sql = sql + " and `endTime` < " + System.currentTimeMillis() + " and `startTime` <" + System.currentTimeMillis();
            sqlCount = sqlCount + " and `endTime` < " + System.currentTimeMillis() + " and `startTime` <" + System.currentTimeMillis();
        }


        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        int count = 0;
        List<Mission> missions = jdbcTemplate.query(sql, new RowMapperHelp<Mission>(Mission.class));
        count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        pager.setData(transformClient(missions));
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public Pager getUserWebByPager(Pager pager, long psid, int missionStatus, int type, int isNow, User user) throws Exception {
        String sql = "SELECT * FROM `mission` where sid = " + psid + " AND status = " + Mission.status_yes;
        String sqlCount = "SELECT COUNT(id) FROM `mission` where sid = " + psid + " AND status = " + Mission.status_yes;
        String limit = " limit %d , %d ";
        if (missionStatus > 0) {
            //完成/未完成
            sql = sql + " and `missionStatus` = " + missionStatus;
            sqlCount = sqlCount + " and `missionStatus` = " + missionStatus;
        }
        if (type > 0) {
            // 团队/个人
            sql = sql + " and `type` = " + type;
            sqlCount = sqlCount + " and `type` = " + type;
        }
        if (isNow == 0) {
            //任务进行中
            sql = sql + " and `endTime` > " + System.currentTimeMillis() + " and `startTime` <" + System.currentTimeMillis();
            sqlCount = sqlCount + " and `endTime` > " + System.currentTimeMillis() + " and `startTime` <" + System.currentTimeMillis();
        }
        if (isNow == 1) {
            //任务已经结束
            sql = sql + " and `endTime` < " + System.currentTimeMillis() + " and `startTime` <" + System.currentTimeMillis();
            sqlCount = sqlCount + " and `endTime` < " + System.currentTimeMillis() + " and `startTime` <" + System.currentTimeMillis();
        }

        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        List<Mission> missions = jdbcTemplate.query(sql, new RowMapperHelp<Mission>(Mission.class));
        List<ClientMission> clientMissionPoolList = new ArrayList<>();
        for (Mission mission : missions) {
            UserMissionPool userMissionPool = new UserMissionPool();
            userMissionPool.setMid(mission.getId());
            userMissionPool.setUid(user.getId());
            UserMissionPool userMissionPoolDaoList = userMissionPoolDao.load(userMissionPool);
            if (userMissionPoolDaoList != null) {
                ClientMission clientMission = new ClientMission(mission);
                clientMission.setAllAmount(userMissionPoolDaoList.getNumber());
                clientMission.setAllProgress(userMissionPoolDaoList.getProgress());
                clientMissionPoolList.add(clientMission);
            }

            SubordinateMissionPool subordinateMissionPool = new SubordinateMissionPool();
            subordinateMissionPool.setMid(mission.getId());
            subordinateMissionPool.setSid(user.getSid());
            SubordinateMissionPool subordinateMissionPoolDaoList = subordinateMissionPoolDao.load(subordinateMissionPool);
            if (subordinateMissionPoolDaoList != null) {
                ClientMission clientMission = new ClientMission(mission);
                clientMission.setAllAmount(subordinateMissionPoolDaoList.getNumber());
                clientMission.setAllProgress(subordinateMissionPoolDaoList.getProgress());
                clientMissionPoolList.add(clientMission);
            }
        }
        pager.setTotalCount(clientMissionPoolList.size());
        pager.setData(clientMissionPoolList);
        return pager;
    }

    private ClientMission transformClient(Mission mission) throws Exception {
        ClientMission clientMission = new ClientMission(mission);
        int allProgress = 0;
        int allAmount = 0;
        List<Personal> list = new ArrayList<>();

        if (mission.getType() == Mission.type_tem) {
            //团队任务 需要先查询门店下的所有sku 然后进行统计
//            List<ProductSKU> ids = productService.getSkuBySubid(mission.getSid(),ProductSPU.type_common);
            for (Long id : mission.getExecutor()) {
                SubordinateMissionPool subordinateMissionPool = new SubordinateMissionPool();
                subordinateMissionPool.setSid(id);
                subordinateMissionPool.setMid(mission.getId());
                subordinateMissionPool = subordinateMissionPoolDao.load(subordinateMissionPool);
                Personal personal = new Personal();
                if (subordinateMissionPool != null) {
                    Subordinate subordinate = subordinateDao.load(subordinateMissionPool.getSid());
                    personal.setSName(subordinate != null ? subordinate.getName() : "");
                    if (mission.getAmountType() == Mission.amountType_number) {
                        allAmount += subordinateMissionPool.getNumber();//总数量
                        personal.setNumber(subordinateMissionPool.getNumber());
                    } else {
                        personal.setPrice(subordinateMissionPool.getPrice());
                        allAmount += subordinateMissionPool.getPrice();//总价格
                    }
                }
            }
            allProgress += getProgress(allAmount, mission.getTarget());//完成度 当前完成数量/目标数量
        } else {
            //个人任务

            for (Long id : clientMission.getExecutor()) {

                UserMissionPool userMissionPool = new UserMissionPool();
                userMissionPool.setMid(mission.getId());
                userMissionPool.setUid(id);
                userMissionPool = userMissionPoolDao.load(userMissionPool);
                Personal personal = new Personal();
                if (userMissionPool != null) {
                    if (mission.getAmountType() == Mission.amountType_number) {
                        personal.setNumber(userMissionPool.getNumber());
                        allAmount += userMissionPool.getNumber();//总数量
                    } else {
                        personal.setPrice(userMissionPool.getPrice());
                        allAmount += userMissionPool.getPrice();//总价格
                    }
                }
                // 插入个人完成情况
                User load = userDao.load(id);
                personal.setSid(load != null ? load.getSid() : 0);
                personal.setUName(load != null ? load.getName() : "");
                if (load != null) {
                    Subordinate subordinate = subordinateDao.load(load.getSid());
                    personal.setSName(subordinate != null ? subordinate.getName() : "");
                }
                list.add(personal);
            }
            allProgress += getProgress(allAmount, mission.getTarget());//完成度 当前完成数量/目标数量
        }
        // 更改结构 按照店铺分类
        Map map = listToMap(list);
        clientMission.setPersonalList(map);
        clientMission.setAllProgress(allProgress);  //完成度
        clientMission.setAllAmount(allAmount);      //完成量
        return clientMission;
    }

    private Map listToMap(List<Personal> list) {
        Map<Object, List<Personal>> map = new HashMap();
        for (Personal li : list) {
            if (map.containsKey(li.getSid())) {
                map.get(li.getSid()).add(li);
            } else {
                List<Personal> listP = new ArrayList<>();
                listP.add(li);
                map.put(li.getSid(), listP);
            }
        }
        return map;
    }

    private List<ClientMission> transformClient(List<Mission> mission) throws Exception {
        List<ClientMission> clientMission = Lists.newArrayList();
        for (Mission miss : mission) {
            ClientMission client = transformClient(miss);
            clientMission.add(client);
        }
        return clientMission;
    }

    //计算完成度
    private int getProgress(int now, int target) throws Exception {
        float a = now;//当前完成目标
        float b = target;//总目标
        return (int) (a / b * 100);
    }

    //两个List是否相等
    private <T> boolean isLists(List<T> l1, List<T> l2) throws Exception {
        if (l1 == l2) {
            return true;
        }
        if (l1 == null || l2 == null) {
            return false;
        }
        if (l1.size() != l2.size()) {
            return false;
        }
        for (T t : l1) {
            if (!l1.contains(t)) {
                return false;
            }
        }
        for (T t : l2) {
            if (!l1.contains(t)) {
                return false;
            }
        }
        return true;
    }
}
