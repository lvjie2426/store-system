package com.store.system.web.job;

import com.quakoo.baseFramework.redis.JedisX;
import com.store.system.client.ClientSubordinate;
import com.store.system.dao.AttendanceLogDao;
import com.store.system.dao.AttendanceTemplateDao;
import com.store.system.model.Subordinate;
import com.store.system.model.User;
import com.store.system.model.attendance.AttendanceItem;
import com.store.system.model.attendance.AttendanceLog;
import com.store.system.model.attendance.AttendanceTemplate;
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
import java.util.Date;
import java.util.List;

@Component
public class CreateAttendanceLogJob implements InitializingBean {

    @Resource
    private UserService userService;

    @Resource
    private SubordinateService subordinateService;

    @Resource
    private AttendanceLogDao attendanceLogDao;

    @Resource
    private AttendanceLogService attendanceLogService;

    @Resource
    private AttendanceTemplateDao attendanceTemplateDao;

    @Autowired(required = true)
    @Qualifier("cachePool")
    protected JedisX cache;

    Logger logger= LoggerFactory.getLogger(CreateAttendanceLogJob.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        Thread thread = new Thread(new CreateAttendanceLogThread());
        thread.start();
    }

    class CreateAttendanceLogThread implements Runnable {

        @Override
        public void run() {
            for (; ; ) {
                logger.info("createAttendanceLogJob job start:");
                int num = 0;
                int error = 0;

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
/*                String today=sdf.format(date);
                if(cache.setStringIfNotExist("createStatisticsask_"+today, 606024, "1")<=0){
                    continue;
                }*/
                try {
                    List<Subordinate> subordinates = subordinateService.getAllParentSubordinate();
                    for (Subordinate subordinate : subordinates) {
                        List<ClientSubordinate> subs =subordinateService.getTwoLevelAllList(subordinate.getId());
                        for(Subordinate sub:subs) {
                            List<User> users = userService.getAllUserBySid(sub.getId());
                            for (User user : users) {
                                try {
                                    createLog(user, sub, date.getTime());
                                    num = num + 1;
                                } catch (Exception e) {
                                    error = error + 1;
                                    logger.error("", e);
                                }
                            }
                        }

                    }

                } catch (Exception e) {
                    logger.error("", e);
                } finally {
                    try {
                        Thread.sleep(1000 * 60 * 60);
                    } catch (Exception e1) {
                    }
                }
                logger.info("createAttendanceLogJob job end success:" + num + ",fail:" + error);

            }
        }
    }

    private void createLog(User user,Subordinate subordinate,long time)throws Exception{
        long uid=0;
        long aid=0;
        long psid=subordinate.getPid();
        long sid=subordinate.getId();
        if(user!=null){
            aid=user.getAid();
            uid=user.getId();
        }
        AttendanceLog attendanceLog=new AttendanceLog();
        attendanceLog.setDay(TimeUtils.getDayFormTime(time));
        attendanceLog.setUid(uid);
        attendanceLog.setSid(psid);
        attendanceLog.setSubId(sid);
        AttendanceLog dblog=attendanceLogDao.load(attendanceLog);
        if(dblog!=null){return;}
        //考勤如果记录没有 ，那么需要获取他的模板。
        AttendanceTemplate attendanceTemplate=null;
        if(aid>0){
            attendanceTemplate=attendanceTemplateDao.load(aid);
            AttendanceItem attendanceItem=attendanceLogService.getAttendanceItem(TimeUtils.getDayFormTime(time),attendanceTemplate);
            if(attendanceTemplate != null) {
                attendanceLog.setStart(attendanceItem.getStart());
                attendanceLog.setEnd(attendanceItem.getEnd());
                attendanceLog.setFlexTime(attendanceTemplate.getFlextime());
            }
        }
        attendanceLog.setMonth(TimeUtils.getMonthFormTime(time));
        attendanceLog.setWeek(TimeUtils.getWeekFormTime(time));
        attendanceLog.setYear(TimeUtils.getYearFormTime(time));
        attendanceLogDao.insert(attendanceLog);

    }
}
