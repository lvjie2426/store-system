package com.store.system.web.controller;

import com.google.common.collect.Lists;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientAttendanceRanking;
import com.store.system.client.ClientRankingFirst;
import com.store.system.client.ClientWorkingHour;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.attendance.AttendanceRanking;
import com.store.system.service.AttendanceRankingService;
import com.store.system.util.Constant;
import com.store.system.util.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.List;

/**
 * @ClassName AttendanceRankingController
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/16 11:11
 * @Version 1.0
 **/
@Controller
@RequestMapping("/ranking")
public class AttendanceRankingController extends BaseController {

    @Resource
    private AttendanceRankingService attendanceRankingService;

    /*** 
     * 今日，昨日，某日，排行榜
     * @Param: [request, response, sid, subIds, day, type]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/9/19
     */
    @RequestMapping("/getSubList")
    public ModelAndView getSubList(HttpServletRequest request, HttpServletResponse response,
                                   long sid, @RequestParam(name = "subIds[]") List<Long> subIds,
                                   @RequestParam(required = false, value = "day", defaultValue = "0") long day,
                                   int type) throws Exception {
        try {
            List<ClientAttendanceRanking> res = Lists.newArrayList();
            long today = TimeUtils.getDayFormTime(System.currentTimeMillis());
            long currentMonth = TimeUtils.getMonthFormTime(System.currentTimeMillis());
            long currentYear = TimeUtils.getYearFormTime(System.currentTimeMillis());
            if (type == Constant.type_today) {
                res = attendanceRankingService.getSubListByDay(sid, subIds, today);
            } else if (type == Constant.type_yesterday) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);
                long yesterday = TimeUtils.getDayFormTime(cal.getTimeInMillis());
                res = attendanceRankingService.getSubListByDay(sid, subIds, yesterday);
            } else if (type == Constant.type_day_search) {
                res = attendanceRankingService.getSubListByDay(sid, subIds, day);
            } else if (type == Constant.type_month) {
                res = attendanceRankingService.getSubListByMonth(sid, subIds, currentMonth);
            } else if (type == Constant.type_year_search) {
                res = attendanceRankingService.getSubListByYear(sid, subIds, currentYear);
            }
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
     * 早起鸟排行榜
     * @Param: [request, response]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/9/16
     */
    @RequestMapping("/getSubFirstTimesList")
    public ModelAndView getSubFirstTimesList(HttpServletRequest request, HttpServletResponse response,
                                             long sid, @RequestParam(name = "subIds[]") List<Long> subIds,
                                             @RequestParam(required = false, value = "year", defaultValue = "0") long year,
                                             @RequestParam(required = false, value = "month", defaultValue = "0") long month,
                                             int type) throws Exception {
        try {
            List<ClientRankingFirst> res = Lists.newArrayList();
            long currentMonth = TimeUtils.getMonthFormTime(System.currentTimeMillis());
            long currentYear = TimeUtils.getYearFormTime(System.currentTimeMillis());
            if (type == Constant.type_month) {
                res = attendanceRankingService.getSubFirstTimesListByMonth(sid, subIds, currentMonth);
            } else if (type == Constant.type_month_search) {
                res = attendanceRankingService.getSubFirstTimesListByMonth(sid, subIds, month);
            } else if (type == Constant.type_year_search) {
                res = attendanceRankingService.getSubFirstTimesListByYear(sid, subIds, year);
            } else if (type == Constant.type_halfUpYear) {
                res = attendanceRankingService.getSubFirstTimesListByUpYear(sid, subIds, currentYear);
            } else if (type == Constant.type_halfDownYear) {
                res = attendanceRankingService.getSubFirstTimesListByDownYear(sid, subIds, currentYear);
            }
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
     * 工时排行榜
     * @Param: [request, response, sid, subIds, year, month, type]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/9/19
     */
    @RequestMapping("/getSubWorkingHourList")
    public ModelAndView getSubWorkingHourList(HttpServletRequest request, HttpServletResponse response,
                                              long sid, @RequestParam(name = "subIds[]") List<Long> subIds,
                                              @RequestParam(required = false, value = "day", defaultValue = "0") long day,
                                              @RequestParam(required = false, value = "month", defaultValue = "0") long month,
                                              @RequestParam(required = false, value = "year", defaultValue = "0") long year,
                                              int type) throws Exception {
        try {
            List<ClientWorkingHour> res = Lists.newArrayList();
            long currentYear = TimeUtils.getYearFormTime(System.currentTimeMillis());
            if (type == Constant.type_day_search) {
                res = attendanceRankingService.getSubWorkingHourListByDay(sid, subIds, day);
            } else if (type == Constant.type_month_search) {
                res = attendanceRankingService.getSubWorkingHourListByMonth(sid, subIds, month);
            } else if (type == Constant.type_year_search) {
                res = attendanceRankingService.getSubWorkingHourListByYear(sid, subIds, year);
            } else if (type == Constant.type_halfUpYear) {
                res = attendanceRankingService.getSubWorkingHourListByUpYear(sid, subIds, currentYear);
            } else if (type == Constant.type_halfDownYear) {
                res = attendanceRankingService.getSubWorkingHourListByDownYear(sid, subIds, currentYear);
            }
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

}
