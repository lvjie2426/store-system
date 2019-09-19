package com.store.system.service;

import com.store.system.client.ClientAttendanceRanking;
import com.store.system.client.ClientRankingFirst;
import com.store.system.client.ClientWorkingHour;
import com.store.system.model.attendance.AttendanceRanking;

import java.util.List;

/**
 * @ClassName AttendanceRankingService
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/16 10:39
 * @Version 1.0
 **/
public interface AttendanceRankingService {

    public AttendanceRanking add(AttendanceRanking attendanceRanking) throws Exception;

    public List<ClientAttendanceRanking> getSubListByDay(long sid, List<Long> subIds, long day) throws Exception;

    public List<ClientAttendanceRanking> getSubListByMonth(long sid, List<Long> subIds, long month) throws Exception;

    public List<ClientAttendanceRanking> getSubListByYear(long sid, List<Long> subIds, long year) throws Exception;

    public List<ClientRankingFirst> getSubFirstTimesListByMonth(long sid, List<Long> subIds, long month) throws Exception;

    public List<ClientRankingFirst> getSubFirstTimesListByYear(long sid, List<Long> subIds, long year) throws Exception;

    public List<ClientRankingFirst> getSubFirstTimesListByUpYear(long sid, List<Long> subIds, long year) throws Exception;

    public List<ClientRankingFirst> getSubFirstTimesListByDownYear(long sid, List<Long> subIds, long year) throws Exception;


    public List<ClientWorkingHour> getSubWorkingHourListByDay(long sid, List<Long> subIds, long day)  throws Exception;

    public List<ClientWorkingHour> getSubWorkingHourListByMonth(long sid, List<Long> subIds, long month)  throws Exception;

    public List<ClientWorkingHour> getSubWorkingHourListByYear(long sid, List<Long> subIds, long year) throws Exception;

    public List<ClientWorkingHour> getSubWorkingHourListByUpYear(long sid, List<Long> subIds, long year) throws Exception;

    public List<ClientWorkingHour> getSubWorkingHourListByDownYear(long sid, List<Long> subIds, long year) throws Exception;




}
