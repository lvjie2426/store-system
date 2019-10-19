package com.store.system.web.job;

import com.quakoo.baseFramework.redis.JedisX;
import com.store.system.client.ClientAttendanceInfo;
import com.store.system.client.ClientSubordinate;
import com.store.system.dao.SubordinateAttendanceStatisticsDao;
import com.store.system.dao.UserAttendanceStatisticsDao;
import com.store.system.model.Subordinate;
import com.store.system.model.User;
import com.store.system.model.attendance.SubordinateAttendanceStatistics;
import com.store.system.model.attendance.UserAttendanceStatistics;
import com.store.system.service.AttendanceLogService;
import com.store.system.service.SubordinateService;
import com.store.system.service.UserService;
import com.store.system.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class CreateStatisticsAttendanceJob implements InitializingBean {

    Logger logger= LoggerFactory.getLogger(CreateStatisticsAttendanceJob.class);
    @Resource
    private UserService userService;

    @Resource
    private SubordinateService subordinateService;

    @Resource
    private AttendanceLogService attendanceLogService;

    @Resource
    private SubordinateAttendanceStatisticsDao subordinateAttendanceStatisticsDao;

    @Resource
    private UserAttendanceStatisticsDao userAttendanceStatisticsDao;

    @Autowired(required = true)

    @Qualifier("cachePool")
    protected JedisX cache;


    @Override
    public void afterPropertiesSet() throws Exception {
        Thread thread = new Thread(new CreateStatisticsAttendanceThread());
        thread.start();

    }

    class CreateStatisticsAttendanceThread implements Runnable {

        @Override
        public void run() {
            for (; ; ) {
                logger.info("createStatisticsAttendanceJob job start:");

                Date date = new Date();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
/*                String today=sdf.format(date);
                if(cache.setStringIfNotExist("createStatisticsask_"+today, 606024, "1")<=0){
                    continue;
                }*/
                long  day = Long.parseLong(sdf.format(date));
                try {
                        List<Subordinate> subordinates = subordinateService.getAllParentSubordinate();
                        for (Subordinate subordinate : subordinates) {
                            List<ClientSubordinate> subs =subordinateService.getTwoLevelAllList(subordinate.getId());
                            for(Subordinate sub:subs) {
                                createSubordinateStatisticsAttendance(sub.getId(), day);
                                List<User> users = userService.getAllUserBySid(sub.getId());
                                for (User user : users) {
                                    createUserStatisticsAttendance(user.getId(), date.getTime());
                                }
                            }

                        }
                }catch (Exception e){
                    logger.error("",e);
                }finally {
                    try {
                        Thread.sleep(1000 * 60*71);
                    } catch (Exception e1) {
                    }
                }
                logger.info("createStatisticsAttendanceJob job end ");
            }
        }
    }

    public void createSubordinateStatisticsAttendance(long sid, long day) throws Exception {
        ClientAttendanceInfo clientAttendanceInfo = attendanceLogService.getAllListBySubordinateDay(sid, day);
        SubordinateAttendanceStatistics subordinateAttendanceStatistics = new SubordinateAttendanceStatistics();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        subordinateAttendanceStatistics.setSid(sid);
        subordinateAttendanceStatistics.setDay(day);
        subordinateAttendanceStatistics.setWeek(TimeUtils.getWeekFormTime(TimeUtils.getTimeFormDay(day)));
        subordinateAttendanceStatistics.setMonth(Long.parseLong(sdf.format(TimeUtils.getTimeFormDay(day))));
        SubordinateAttendanceStatistics dbInfo = subordinateAttendanceStatisticsDao.load(subordinateAttendanceStatistics);
        boolean update = true;
        if (dbInfo == null) {
            update = false;
            dbInfo = subordinateAttendanceStatistics;
        }
        dbInfo.setAbsentNum(clientAttendanceInfo.getAbsentNum());
        dbInfo.setAttendanceNum(clientAttendanceInfo.getAttendanceNum());
        dbInfo.setAttendanceRate(Double.parseDouble(clientAttendanceInfo.getAttendanceRate()));
        dbInfo.setLateNum(clientAttendanceInfo.getLateNum());
        dbInfo.setLeaveEarlyNum(clientAttendanceInfo.getLeaveEarlyNum());
        dbInfo.setLeaveNum(clientAttendanceInfo.getLeaveNum());
        dbInfo.setNoCardNum(clientAttendanceInfo.getNoCardNum());
        dbInfo.setTotalNum(clientAttendanceInfo.getTotalNum());
        dbInfo.setUnsetNum(clientAttendanceInfo.getUnsetNum());
        dbInfo.setWeiPaibanNum(clientAttendanceInfo.getWeiPaibanNum());
        if (update) {
            subordinateAttendanceStatisticsDao.update(dbInfo);
        } else {
            subordinateAttendanceStatisticsDao.insert(dbInfo);
        }
    }


    //统计用户月份考勤(每次添加修改的时候 就统计一下当前月的)
    public void createUserStatisticsAttendance(long uid, long time) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        long startTime = calendar.getTime().getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        long endTime = calendar.getTime().getTime();
        ClientAttendanceInfo clientAttendanceInfo = attendanceLogService.getUserAttendanceLogs("", uid, startTime, endTime);

        UserAttendanceStatistics userStatisticsAttendance = new UserAttendanceStatistics();
        userStatisticsAttendance.setUid(uid);
        userStatisticsAttendance.setMonth(TimeUtils.getMonthFormTime(time));
        UserAttendanceStatistics dbInfo = userAttendanceStatisticsDao.load(userStatisticsAttendance);
        boolean update = true;
        if (dbInfo == null) {
            update = false;
            dbInfo = userStatisticsAttendance;
        }
        dbInfo.setAbsentNum(clientAttendanceInfo.getAbsentNum());
        dbInfo.setAttendanceNum(clientAttendanceInfo.getAttendanceNum());
        dbInfo.setAttendanceRate(Double.parseDouble(clientAttendanceInfo.getAttendanceRate()));
        dbInfo.setLateNum(clientAttendanceInfo.getLateNum());
        dbInfo.setLeaveEarlyNum(clientAttendanceInfo.getLeaveEarlyNum());
        dbInfo.setLeaveNum(clientAttendanceInfo.getLeaveNum());
        dbInfo.setNoCardNum(clientAttendanceInfo.getNoCardNum());
        dbInfo.setTotalNum(clientAttendanceInfo.getTotalNum());
        dbInfo.setUnsetNum(clientAttendanceInfo.getUnsetNum());
        dbInfo.setWeiPaibanNum(clientAttendanceInfo.getWeiPaibanNum());
        if (update) {
            userAttendanceStatisticsDao.update(dbInfo);
        } else {
            userAttendanceStatisticsDao.insert(dbInfo);
        }
    }
}
