package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.store.system.bean.ClientRankingFirst;
import com.store.system.bean.SimpleUser;
import com.store.system.dao.AttendanceRankingDao;
import com.store.system.model.User;
import com.store.system.model.attendance.AttendanceRanking;
import com.store.system.service.AttendanceRankingService;
import com.store.system.service.UserService;
import com.store.system.util.TimeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AttendanceRankingServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/16 10:41
 * @Version 1.0
 **/
@Service
public class AttendanceRankingServiceImpl implements AttendanceRankingService {

    @Resource
    private AttendanceRankingDao attendanceRankingDao;
    @Resource
    private UserService userService;



    @Override
    public AttendanceRanking add(AttendanceRanking attendanceRanking) throws Exception {
        AttendanceRanking dbInfo = attendanceRankingDao.load(attendanceRanking);
        if(dbInfo==null) {
            attendanceRanking = attendanceRankingDao.insert(attendanceRanking);
        }
        return attendanceRanking;
    }

    @Override
    public List<AttendanceRanking> getSubListByDay(long sid, List<Long> subIds, long day) throws Exception {
        List<AttendanceRanking> res = Lists.newArrayList();
        for(Long subId:subIds){
            List<AttendanceRanking> rankings = attendanceRankingDao.getSubListByDay(sid,subId,day);
            res.addAll(rankings);
        }
        sort(res);
        return res;
    }

    @Override
    public List<AttendanceRanking> getSubListByMonth(long sid, List<Long> subIds, long month) throws Exception {
        List<AttendanceRanking> res = Lists.newArrayList();
        for(Long subId:subIds){
            List<AttendanceRanking> rankings = attendanceRankingDao.getSubListByMonth(sid,subId,month);
            res.addAll(rankings);
        }
        sort(res);
        return res;
    }

    @Override
    public List<AttendanceRanking> getSubListByYear(long sid, List<Long> subIds, long year) throws Exception {
        List<AttendanceRanking> res = Lists.newArrayList();
        for(Long subId:subIds){
            List<AttendanceRanking> rankings = attendanceRankingDao.getSubListByYear(sid,subId,year);
            res.addAll(rankings);
        }
        sort(res);
        return res;
    }

    @Override
    public List<ClientRankingFirst> getSubFirstTimesListByMonth(long sid, List<Long> subIds, long month) throws Exception {
        String str = String.valueOf(month);
        int year = Integer.parseInt(str.substring(0,4));
        int currentMonth = Integer.parseInt(str.substring(4,6));
        List<Long> days = TimeUtils.getMonthFullDay(year,currentMonth);
        Map<Long,List<AttendanceRanking>> rankingMap = getSubFirstTimesList(sid,subIds,days);
        return transformClient(rankingMap);
    }

    @Override
    public List<ClientRankingFirst> getSubFirstTimesListByYear(long sid, List<Long> subIds, long year) throws Exception {
        List<Long> days = TimeUtils.getYearFullDay((int) year);
        Map<Long,List<AttendanceRanking>> rankingMap = getSubFirstTimesList(sid,subIds,days);
        return transformClient(rankingMap);
    }

    @Override
    public List<ClientRankingFirst> getSubFirstTimesListByUpYear(long sid, List<Long> subIds, long year) throws Exception {
        List<Long> days = Lists.newArrayList();
        days.addAll(TimeUtils.getMonthFullDay((int) year,1));
        days.addAll(TimeUtils.getMonthFullDay((int) year,2));
        days.addAll(TimeUtils.getMonthFullDay((int) year,3));
        days.addAll(TimeUtils.getMonthFullDay((int) year,4));
        days.addAll(TimeUtils.getMonthFullDay((int) year,5));
        days.addAll(TimeUtils.getMonthFullDay((int) year,6));
        Map<Long,List<AttendanceRanking>> rankingMap = getSubFirstTimesList(sid,subIds,days);
        return transformClient(rankingMap);
    }

    @Override
    public List<ClientRankingFirst> getSubFirstTimesListByDownYear(long sid, List<Long> subIds, long year) throws Exception {
        List<Long> days = Lists.newArrayList();
        days.addAll(TimeUtils.getMonthFullDay((int) year,7));
        days.addAll(TimeUtils.getMonthFullDay((int) year,8));
        days.addAll(TimeUtils.getMonthFullDay((int) year,9));
        days.addAll(TimeUtils.getMonthFullDay((int) year,10));
        days.addAll(TimeUtils.getMonthFullDay((int) year,11));
        days.addAll(TimeUtils.getMonthFullDay((int) year,12));
        Map<Long,List<AttendanceRanking>> rankingMap = getSubFirstTimesList(sid,subIds,days);
        return transformClient(rankingMap);
    }

    private Map<Long,List<AttendanceRanking>> getSubFirstTimesList(long sid, List<Long> subIds, List<Long> days) {
        Map<Long,List<AttendanceRanking>> rankingMap = Maps.newHashMap();
        for(Long day:days){
            for(Long subId:subIds) {
                List<AttendanceRanking> rankingList = attendanceRankingDao.getSubListByDay(sid, subId, day);
                if (rankingList.size() > 0) {
                    AttendanceRanking info = rankingList.get(0);
                    List<AttendanceRanking> rankings = rankingMap.get(info.getUid());
                    if (rankings == null) {
                        rankings = Lists.newArrayList();
                        rankingMap.put(info.getUid(), rankings);
                    }
                    rankings.add(info);
                }
            }
        }
        return rankingMap;
    }

    private List<ClientRankingFirst> transformClient(Map<Long,List<AttendanceRanking>> map) throws Exception{
        List<ClientRankingFirst> res = Lists.newArrayList();
        for(Map.Entry<Long,List<AttendanceRanking>> entry:map.entrySet()){
            ClientRankingFirst info = new ClientRankingFirst();
            User user = userService.load(entry.getKey());
            SimpleUser simpleUser = new SimpleUser(user);
            info.setUser(simpleUser);
            info.setTimes(entry.getValue().size());
            res.add(info);
        }
        sortClient(res);
        return res;
    }

    private void sort(List<AttendanceRanking> list){
        Collections.sort(list, new Comparator<AttendanceRanking>(){
            public int compare(AttendanceRanking p1, AttendanceRanking p2) {
                if(p1.getLeadTime() < p2.getLeadTime()){
                    return 1;
                }
                if(p1.getLeadTime() == p2.getLeadTime()){
                    return 0;
                }
                return -1;
            }
        });
    }

    private void sortClient(List<ClientRankingFirst> list){
        Collections.sort(list, new Comparator<ClientRankingFirst>(){
            public int compare(ClientRankingFirst p1, ClientRankingFirst p2) {
                if(p1.getTimes() < p2.getTimes()){
                    return 1;
                }
                if(p1.getTimes() == p2.getTimes()){
                    return 0;
                }
                return -1;
            }
        });
    }

}
