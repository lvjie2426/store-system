package com.store.system.client;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class ClientAttendanceInfo implements Serializable{

    private String name;

    private String icon;
    /**
     * 应出勤数
     */
    private int totalNum;

    /**
     * 迟到次数
     */
    private int lateNum;
    /**
     * 早退次数
     */
    private int leaveEarlyNum;


    /**
     * 实际出勤数
     */
    private int attendanceNum;

    /**
     * 缺卡次数
     */
    private int noCardNum;

    /**
     * 旷工次数
     */
    private int absentNum;

    /**
     * 未排班次数
     */
    private int weiPaibanNum;

    /**
     * 请假次数
     */
    private int leaveNum;

    /**
     * 未设置次数
     */
    private int unsetNum;

    /**
     * 出勤率
     */
    private String attendanceRate="0.0";

    /**
     * 日期
     */
    private String time;

    private List<ClientAttendanceLog> attendanceLogs;

}
