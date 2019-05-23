package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.client.ClientMission;
import com.store.system.dao.MissionDao;
import com.store.system.model.Mission;
import com.store.system.model.SubordinateMissionPool;
import com.store.system.model.UserMissionPool;
import com.store.system.service.MissionService;
import com.store.system.service.SubordinateMissionPoolService;
import com.store.system.service.UserMissionPoolService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Override
    public Mission insert(Mission mission) throws Exception {
        return missionDao.insert(mission);
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
                SubordinateMissionPool subordinateMissionPool = subordinateMissionPoolService.load(id,mission.getId());
                if(mission.getAmountType()==Mission.amountType_number){
                    allAmount+=subordinateMissionPool.getNumber();//总数量
                }else{
                    allAmount+=subordinateMissionPool.getPrice();//总价格
                }
                allProgress+=subordinateMissionPool.getProgress();//完成度
            }
        }else{
            //个人任务
            for(Long id:ids){
                UserMissionPool userMissionPool = userMissionPoolService.load(id,mission.getId());
                if(mission.getAmountType()==Mission.amountType_number){
                    allAmount+=userMissionPool.getNumber();//总数量
                }else{
                    allAmount+=userMissionPool.getPrice();//总价格
                }
                allProgress+=userMissionPool.getProgress();//完成度
            }
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
}
