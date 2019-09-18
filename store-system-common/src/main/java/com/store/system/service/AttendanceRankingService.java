package com.store.system.service;

import com.store.system.bean.ClientRankingFirst;
import com.store.system.bean.ClientWorkingHour;
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

    public List<AttendanceRanking> getSubListByDay(long sid, List<Long> subIds, long day) throws Exception;

    public List<AttendanceRanking> getSubListByMonth(long sid, List<Long> subIds, long month) throws Exception;

    public List<AttendanceRanking> getSubListByYear(long sid, List<Long> subIds, long year) throws Exception;

    public List<ClientRankingFirst> getSubFirstTimesListByMonth(long sid, List<Long> subIds, long month) throws Exception;

    public List<ClientRankingFirst> getSubFirstTimesListByYear(long sid, List<Long> subIds, long year) throws Exception;

    public List<ClientRankingFirst> getSubFirstTimesListByUpYear(long sid, List<Long> subIds, long year) throws Exception;

    public List<ClientRankingFirst> getSubFirstTimesListByDownYear(long sid, List<Long> subIds, long year) throws Exception;


    public List<ClientWorkingHour> getSubWorkingHourListByMonth(long sid, List<Long> subIds, long month)  throws Exception;

    public List<ClientWorkingHour> getSubWorkingHourListByYear(long sid, List<Long> subIds, long year) throws Exception;

    public List<ClientWorkingHour> getSubWorkingHourListByUpYear(long sid, List<Long> subIds, long year) throws Exception;

    public List<ClientWorkingHour> getSubWorkingHourListByDownYear(long sid, List<Long> subIds, long year) throws Exception;




}
