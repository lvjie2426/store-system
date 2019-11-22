package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.quakoo.baseFramework.transform.TransformMapUtils;
import com.store.system.client.ClientAttendanceRanking;
import com.store.system.client.ClientRankingFirst;
import com.store.system.client.ClientWorkingHour;
import com.store.system.bean.SimpleUser;
import com.store.system.dao.AttendanceLogDao;
import com.store.system.dao.AttendanceRankingDao;
import com.store.system.dao.SubordinateDao;
import com.store.system.dao.WorkOverTimeDao;
import com.store.system.model.Subordinate;
import com.store.system.model.User;
import com.store.system.model.attendance.AttendanceLog;
import com.store.system.model.attendance.AttendanceRanking;
import com.store.system.model.attendance.WorkOverTime;
import com.store.system.service.AttendanceRankingService;
import com.store.system.service.UserService;
import com.store.system.util.ArithUtils;
import com.store.system.util.TimeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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
    @Resource
    private AttendanceLogDao attendanceLogDao;
    @Resource
    private SubordinateDao subordinateDao;
    @Resource
    private WorkOverTimeDao workOverTimeDao;


    private TransformMapUtils userMapUtils = new TransformMapUtils(User.class);
    private TransformMapUtils subMapUtils = new TransformMapUtils(Subordinate.class);


    @Override
    public AttendanceRanking add(AttendanceRanking attendanceRanking) throws Exception {
        AttendanceRanking dbInfo = attendanceRankingDao.load(attendanceRanking);
        if(dbInfo==null) {
            attendanceRanking = attendanceRankingDao.insert(attendanceRanking);
        }
        return attendanceRanking;
    }

    @Override
    public List<ClientAttendanceRanking> getSubListByDay(long sid, List<Long> subIds, long day) throws Exception {
        List<AttendanceRanking> res = Lists.newArrayList();
        for(Long subId:subIds){
            List<AttendanceRanking> rankings = attendanceRankingDao.getSubListByDay(sid,subId,day);
            res.addAll(rankings);
        }
        return transformClient(res);
    }

    @Override
    public List<ClientAttendanceRanking> getSubListByMonth(long sid, List<Long> subIds, long month) throws Exception {
        List<AttendanceRanking> res = Lists.newArrayList();
        for(Long subId:subIds){
            List<AttendanceRanking> rankings = attendanceRankingDao.getSubListByMonth(sid,subId,month);
            res.addAll(rankings);
        }
        return transformClient(res);
    }

    @Override
    public List<ClientAttendanceRanking> getSubListByYear(long sid, List<Long> subIds, long year) throws Exception {
        List<AttendanceRanking> res = Lists.newArrayList();
        for(Long subId:subIds){
            List<AttendanceRanking> rankings = attendanceRankingDao.getSubListByYear(sid,subId,year);
            res.addAll(rankings);
        }
        return transformClient(res);
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

    public List<ClientWorkingHour> getSubWorkingHourListByDay(long sid, List<Long> subIds, long day) throws Exception {
        List<AttendanceLog> attendanceLogs = Lists.newArrayList();
        for(Long subId:subIds){
            attendanceLogs.addAll(attendanceLogDao.getAllListBySubDay(subId,day));
        }
        Map<Long,List<AttendanceLog>> map = transformMap(attendanceLogs);
        return transformWorkingClient(map, 1,day);
    }

    public List<ClientWorkingHour> getSubWorkingHourListByMonth(long sid, List<Long> subIds, long month) throws Exception {
        List<AttendanceLog> attendanceLogs = Lists.newArrayList();
        for(Long subId:subIds){
            attendanceLogs.addAll(attendanceLogDao.getAllListBySubMonth(subId,month));
        }
        Map<Long,List<AttendanceLog>> map = transformMap(attendanceLogs);
        return transformWorkingClient(map,2,month);
    }

    @Override
    public List<ClientWorkingHour> getSubWorkingHourListByYear(long sid, List<Long> subIds, long year) throws Exception {
        List<AttendanceLog> attendanceLogs = Lists.newArrayList();
        for(Long subId:subIds){
            attendanceLogs.addAll(attendanceLogDao.getAllListBySubYear(subId,year));
        }
        Map<Long,List<AttendanceLog>> map = transformMap(attendanceLogs);
        return transformWorkingClient(map,3,year);
    }

    @Override
    public List<ClientWorkingHour> getSubWorkingHourListByUpYear(long sid, List<Long> subIds, long year) throws Exception {
        List<Long> days = Lists.newArrayList();
        days.addAll(TimeUtils.getMonthFullDay((int) year,1));
        days.addAll(TimeUtils.getMonthFullDay((int) year,2));
        days.addAll(TimeUtils.getMonthFullDay((int) year,3));
        days.addAll(TimeUtils.getMonthFullDay((int) year,4));
        days.addAll(TimeUtils.getMonthFullDay((int) year,5));
        days.addAll(TimeUtils.getMonthFullDay((int) year,6));
        Map<Long,List<AttendanceLog>> map = getSubWorkingHourList(sid,subIds,days);
        return transformWorkingClient(map,4,year);
    }

    @Override
    public List<ClientWorkingHour> getSubWorkingHourListByDownYear(long sid, List<Long> subIds, long year) throws Exception {
        List<Long> days = Lists.newArrayList();
        days.addAll(TimeUtils.getMonthFullDay((int) year,7));
        days.addAll(TimeUtils.getMonthFullDay((int) year,8));
        days.addAll(TimeUtils.getMonthFullDay((int) year,9));
        days.addAll(TimeUtils.getMonthFullDay((int) year,10));
        days.addAll(TimeUtils.getMonthFullDay((int) year,11));
        days.addAll(TimeUtils.getMonthFullDay((int) year,12));
        Map<Long,List<AttendanceLog>> map = getSubWorkingHourList(sid,subIds,days);
        return transformWorkingClient(map,5,year);
    }

    private Map<Long, List<AttendanceLog>> transformMap(List<AttendanceLog> attendanceLogs) {
        Map<Long, List<AttendanceLog>> map = Maps.newHashMap();
        for (AttendanceLog attendanceLog : attendanceLogs) {
            List<AttendanceLog> logs = map.get(attendanceLog.getUid());
            if (logs == null) {
                logs = Lists.newArrayList();
                map.put(attendanceLog.getUid(), logs);
            }
            logs.add(attendanceLog);
        }
        return map;
    }

    private Map<Long,List<AttendanceLog>> getSubWorkingHourList(long sid, List<Long> subIds, List<Long> days) {
        Map<Long,List<AttendanceLog>> map = Maps.newHashMap();
        List<AttendanceLog> attendanceLogs = Lists.newArrayList();
        for(Long day:days){
            for(Long subId:subIds) {
                List<AttendanceLog> logs = attendanceLogDao.getAllListBySubDay(subId, day);
                attendanceLogs.addAll(logs);
            }
        }
        map = transformMap(attendanceLogs);
        return map;
    }

    private List<ClientWorkingHour> transformWorkingClient(Map<Long,List<AttendanceLog>> map,int type,long date) throws Exception {
        List<ClientWorkingHour> res = Lists.newArrayList();
        for (Map.Entry<Long, List<AttendanceLog>> entry : map.entrySet()) {
            double hours = 0;
            int days = 0;
            ClientWorkingHour client = new ClientWorkingHour();
            User user = userService.load(entry.getKey());
            client.setUser(new SimpleUser(user));
            if(user!=null) {
                for (AttendanceLog log : entry.getValue()) {
                    //排除今天的工时
                    if (log.getEndTime() > 0) {
                        double time = ArithUtils.div((double) (log.getEndTime() - log.getStartTime()), (double) 1000 * 60 * 60, 1);
                        hours = ArithUtils.add(hours, time);
                        if (time > 0) {
                            int day = 1;
                            days += day;
                        }
                    }
                }
                //加上时间区间内的加班时间
                int workHours = 0;
                List<WorkOverTime> workOverTimes = new ArrayList<>();
                if (type == 1) {
                    //天
                    workOverTimes.addAll(workOverTimeDao.getAllListByDay(user.getId(), WorkOverTime.status_success, date));

                } else if (type == 2) {
                    //月
                    workOverTimes.addAll(workOverTimeDao.getAllListByMonth(user.getId(), WorkOverTime.status_success, date));

                } else if (type == 3) {
                    // 年
                    workOverTimes.addAll(workOverTimeDao.getAllListByYear(user.getId(), WorkOverTime.status_success, date));
                } else if (type == 4) {
                    //上半年
                    List<Long> dates = Lists.newArrayList();
                    dates.addAll(TimeUtils.getMonthFullDay((int) date, 1));
                    dates.addAll(TimeUtils.getMonthFullDay((int) date, 2));
                    dates.addAll(TimeUtils.getMonthFullDay((int) date, 3));
                    dates.addAll(TimeUtils.getMonthFullDay((int) date, 4));
                    dates.addAll(TimeUtils.getMonthFullDay((int) date, 5));
                    dates.addAll(TimeUtils.getMonthFullDay((int) date, 6));
                    for (Long da : dates) {
                        workOverTimes.addAll(workOverTimeDao.getAllListByYear(user.getId(), WorkOverTime.status_success, da));
                    }

                } else {
                    //下半年
                    List<Long> dates = Lists.newArrayList();
                    dates.addAll(TimeUtils.getMonthFullDay((int) date, 7));
                    dates.addAll(TimeUtils.getMonthFullDay((int) date, 8));
                    dates.addAll(TimeUtils.getMonthFullDay((int) date, 9));
                    dates.addAll(TimeUtils.getMonthFullDay((int) date, 10));
                    dates.addAll(TimeUtils.getMonthFullDay((int) date, 11));
                    dates.addAll(TimeUtils.getMonthFullDay((int) date, 12));
                    for (Long da : dates) {
                        workOverTimes.addAll(workOverTimeDao.getAllListByYear(user.getId(), WorkOverTime.status_success, da));
                    }

                }

                for (WorkOverTime workOverTime : workOverTimes) {
                    workHours += workOverTime.getWorkTime();
                }
                client.setHours(hours + workHours);
                client.setDays(days);
                res.add(client);
            }
        }
        sortWorking(res);
        return res;
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

    private List<ClientAttendanceRanking> transformClient(List<AttendanceRanking> list) throws Exception{
        List<ClientAttendanceRanking> res = Lists.newArrayList();
        Set<Long> uids= Sets.newHashSet();
        Set<Long> subIds = Sets.newHashSet();
        for(AttendanceRanking ranking:list){
            uids.add(ranking.getUid());
            subIds.add(ranking.getSubId());
        }
        List<User> users = userService.load(Lists.newArrayList(uids));
        Map<Long, User> userMap = userMapUtils.listToMap(users, "id");
        List<Subordinate> subordinates = subordinateDao.load(Lists.newArrayList(subIds));
        Map<Long, Subordinate> subordinateMap = subMapUtils.listToMap(subordinates, "id");
        for(AttendanceRanking ranking:list){
            ClientAttendanceRanking client = new ClientAttendanceRanking(ranking);
            client.setUser(new SimpleUser(userMap.get(ranking.getUid())));
            client.setSubName(subordinateMap.get(ranking.getSubId()).getName());
            res.add(client);
        }
        sort(res);
        return res;
    }

    private void sort(List<ClientAttendanceRanking> list){
        Collections.sort(list, new Comparator<ClientAttendanceRanking>(){
            public int compare(ClientAttendanceRanking p1, ClientAttendanceRanking p2) {
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

    private void sortWorking(List<ClientWorkingHour> list){
        Collections.sort(list, new Comparator<ClientWorkingHour>(){
            public int compare(ClientWorkingHour p1, ClientWorkingHour p2) {
                if(p1.getDays() < p2.getDays() && p1.getHours() < p2.getHours()){
                    return 1;
                }
                if(p1.getDays() == p2.getDays() && p1.getHours() == p2.getHours()){
                    return 0;
                }
                return -1;
            }
        });
    }
}
