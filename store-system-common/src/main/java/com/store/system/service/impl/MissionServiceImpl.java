package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.client.ClientMission;
import com.store.system.dao.MissionDao;
import com.store.system.dao.SubordinateMissionPoolDao;
import com.store.system.dao.UserMissionPoolDao;
import com.store.system.model.Mission;
import com.store.system.model.Subordinate;
import com.store.system.model.SubordinateMissionPool;
import com.store.system.model.UserMissionPool;
import com.store.system.service.MissionService;
import com.store.system.service.SubordinateMissionPoolService;
import com.store.system.service.UserMissionPoolService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class MissionServiceImpl  implements MissionService {

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

    @Override
    @Transactional
    public Mission insert(Mission mission) throws Exception {
        mission = missionDao.insert(mission);
        if(mission.getType()==Mission.type_tem){
            //团队任务
            List<Long> sids = mission.getExecutor();
            for(Long id: sids){
                SubordinateMissionPool subordinateMissionPool = new SubordinateMissionPool();
                subordinateMissionPool.setMid(mission.getId());
                subordinateMissionPool.setSid(id);
                subordinateMissionPoolDao.insert(subordinateMissionPool);
            }
        }else if(mission.getType()==Mission.type_user){
            //个人任务
            List<Long> uids = mission.getExecutor();
            for(Long id :uids){
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
        return missionDao.update(mission);
    }

    @Override
    public boolean del(long id) throws Exception {
        return missionDao.delete(id);
    }
    @Override
    public Pager getByPager(Pager pager,long sid) throws Exception {
        String sql = "SELECT * FROM `mission` where sid = " + sid ;
        String sqlCount = "SELECT COUNT(id) FROM `mission` where sid = " + sid ;
        String limit = " limit %d , %d ";
        sql = sql + " order  by `ctime` desc";
        sql = sql + String.format(limit, (pager.getPage() - 1) * pager.getSize(), pager.getSize());
        int count = 0;
        List<Mission> missions = this.jdbcTemplate.query(sql,new RowMapperHelp<Mission>(Mission.class));
        count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        pager.setData(transformClient(missions));
        pager.setTotalCount(count);
        return pager;
    }
    //检查当前订单满足那些任务的完成条件并返回任务
    @Override
    public List<Mission> checkMission(long skuId,long sid,long subid,long uid) throws Exception {
        String sql = "SELECT * FROM `mission` where sid = " + sid ;
        sql = sql + " order  by `ctime` desc";
        List<Mission> missions = this.jdbcTemplate.query(sql,new RowMapperHelp<Mission>(Mission.class));
        List<Mission> res = Lists.newArrayList();
        if(missions.size()>0){
            for(Mission mission: missions){
                List<Long> ids = mission.getExecutor();
                List<Long> skus = mission.getSkuIds();
                if(mission.getType()==Mission.type_tem){
                    //团队任务:当前任务包含店铺ID并且包含skuID，保存该任务。
                    if(ids.contains(subid)&&skus.contains(skuId)){
                        res.add(mission);
                    }
                }else if(mission.getType()==Mission.type_user){
                    if(ids.contains(uid)&&skus.contains(skuId)){
                        res.add(mission);
                    }
                }
            }
            return res;
        }
        return null;
    }

    private ClientMission transformClient(Mission mission) throws Exception{
        ClientMission clientMission = new ClientMission(mission);
        int allProgress = 0;
        int allAmount = 0;
        List<Long> ids = clientMission.getExecutor();
        if(mission.getType()==Mission.type_tem){
            //团队任务
            for(Long id:ids){
                SubordinateMissionPool subordinateMissionPool = subordinateMissionPoolService.load(mission.getId(),id);
                if(subordinateMissionPool!=null){
                    if(mission.getAmountType()==Mission.amountType_number){
                        allAmount+=subordinateMissionPool.getNumber();//总数量
                    }else{
                        allAmount+=subordinateMissionPool.getPrice();//总价格
                    }
                }
            }
            allProgress += getProgress(allAmount,mission.getTarget());//完成度 当前完成数量/目标数量
        }else{
            //个人任务
            for(Long id:ids){
                UserMissionPool userMissionPool = userMissionPoolService.load(mission.getId(),id);
                if(userMissionPool!=null){
                    if(mission.getAmountType()==Mission.amountType_number){
                        allAmount+=userMissionPool.getNumber();//总数量
                    }else{
                        allAmount+=userMissionPool.getPrice();//总价格
                    }
                }
            }
            allProgress += getProgress(allAmount,mission.getTarget());//完成度 当前完成数量/目标数量
        }
        clientMission.setAllProgress(allProgress);  //完成度
        clientMission.setAllAmount(allAmount);      //完成量
        return clientMission;
    }
    private List<ClientMission> transformClient(List<Mission> mission) throws Exception{
        List<ClientMission> clientMission = Lists.newArrayList();
        for(Mission miss: mission){
            ClientMission client = transformClient(miss);
            clientMission.add(client);
        }
        return clientMission;
    }
    //计算完成度
    private int getProgress(int now,int target)throws Exception{
        float a = now;//当前完成目标
        float b = target;//总目标
        return (int) (a/b*100);
    }
}
