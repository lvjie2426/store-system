package com.store.system.client;


import com.store.system.model.attendance.AttendanceLog;
import com.store.system.model.attendance.ChangeShift;
import com.store.system.model.attendance.PunchCardLog;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

@Data
public class ClientAttendanceLog extends AttendanceLog {

    public static final int attendanceType_normal = 0;//正常
    public static final int attendanceType_late = 1;//迟到
    public static final int attendanceType_leaveEarly = 2;//早退
    public static final int attendanceType_noCard=3;//缺卡(只打了一次卡)
    public static final int attendanceType_absent = 4;//旷工
    public static final int attendanceType_weiPaiban=5;//未排版
    public static final int attendanceType_leave=6;//请假
    public static final int attendanceType_unSet=7;//没有考勤数据


    private String dayStr;//当天日期 2018-01-01
    private String startStr;//上午打卡时间 08:00
    private String endStr;//下午打卡时间 08:00
    private String upStr;//上班时间
    private String downStr;//下班时间
    private String name;//用户姓名

    /**
     * 当日状态：缺勤，正常，旷工
     */
    private int dayType;
    /**
     * 第一次打卡状态：正常，迟到，没打卡
     */
    private int startType;
    /**
     * 第二次打卡状态：正常，早退，没打卡
     */
    private int endType;

    private List<PunchCardLog> cardLogs;


    public ClientAttendanceLog(AttendanceLog attendanceLog){
        try {
            BeanUtils.copyProperties(this, attendanceLog);
        } catch (Exception e) {
            throw new IllegalStateException("ClientAttendanceLog construction error!");
        }
    }

}


