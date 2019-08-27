package com.store.system.service;


import com.quakoo.baseFramework.model.pagination.Pager;
import com.store.system.client.ClientMission;
import com.store.system.model.Mission;
import com.store.system.model.User;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface MissionService {

    public Mission insert(Mission mission)throws Exception;

    public boolean update(Mission mission)throws Exception;

    public boolean del(long id)throws Exception;

    public Pager getByPager(Pager pager,long sid)throws Exception;

    public Map<String,Object> getAllMission(long sid)throws Exception;

    public Map<String,Object> getAllMissionApp(long sid,Date date)throws Exception;

    //检查当前订单满足那些任务的完成条件并返回任务
    public List<Mission> checkMission(long skuId, long sid, long subid, long uid)throws Exception;

    public Pager getWebByPager(Pager pager, long psid, int missionStatus, int type, int isNow)throws Exception;

    public Pager getUserWebByPager(Pager pager, long psid, int missionStatus, int type, int isNow, User user)throws Exception;
}
