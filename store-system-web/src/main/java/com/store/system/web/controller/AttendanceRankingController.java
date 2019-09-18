package com.store.system.web.controller;

import com.google.common.collect.Lists;
import com.quakoo.webframework.BaseController;
import com.store.system.bean.ClientRankingFirst;
import com.store.system.bean.ClientWorkingHour;
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
public class AttendanceRankingController extends BaseController{

    @Resource
    private AttendanceRankingService attendanceRankingService;

    /***
    * 某天排行榜
    * @Param: [request, response, day]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/9/16
    */
    @RequestMapping("/getSubListByDay")
    public ModelAndView getSubListByDay(HttpServletRequest request, HttpServletResponse response,
                                        long sid, @RequestParam(name = "subIds[]") List<Long> subIds, long day) throws Exception {
        try {
            List<AttendanceRanking> rankingList = attendanceRankingService.getSubListByDay(sid, subIds, day);
            return this.viewNegotiating(request, response, new ResultClient(rankingList));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
    * 今日排行榜
    * @Param: [request, response]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/9/16
    */
    @RequestMapping("/getSubListToday")
    public ModelAndView getSubListToday(HttpServletRequest request, HttpServletResponse response,
                                        long sid, @RequestParam(name = "subIds[]") List<Long> subIds) throws Exception {
        try {
            long day = TimeUtils.getDayFormTime(System.currentTimeMillis());
            List<AttendanceRanking> rankingList = attendanceRankingService.getSubListByDay(sid, subIds, day);
            return this.viewNegotiating(request, response, new ResultClient(rankingList));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
    * 昨日排行榜
    * @Param: [request, response]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/9/16
    */
    @RequestMapping("/getSubListYesterday")
    public ModelAndView getSubListYesterday(HttpServletRequest request, HttpServletResponse response,
                                            long sid, @RequestParam(name = "subIds[]") List<Long> subIds) throws Exception {
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            long day = TimeUtils.getDayFormTime(cal.getTimeInMillis());
            List<AttendanceRanking> rankingList = attendanceRankingService.getSubListByDay(sid, subIds, day);
            return this.viewNegotiating(request, response, new ResultClient(rankingList));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
    * 按月查询排行榜
    * @Param: [request, response]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/9/16
    */
    @RequestMapping("/getSubListMonth")
    public ModelAndView getSubListMonth(HttpServletRequest request, HttpServletResponse response,
                                        long sid, @RequestParam(name = "subIds[]") List<Long> subIds) throws Exception {
        try {
            long month = TimeUtils.getMonthFormTime(System.currentTimeMillis());
            List<AttendanceRanking> rankingList = attendanceRankingService.getSubListByMonth(sid, subIds, month);
            return this.viewNegotiating(request, response, new ResultClient(rankingList));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
    * 按年查询排行榜
    * @Param: [request, response]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/9/16
    */
    @RequestMapping("/getSubListYear")
    public ModelAndView getSubListYear(HttpServletRequest request, HttpServletResponse response,
                                       long sid, @RequestParam(name = "subIds[]") List<Long> subIds) throws Exception {
        try {
            long year = TimeUtils.getYearFormTime(System.currentTimeMillis());
            List<AttendanceRanking> rankingList = attendanceRankingService.getSubListByYear(sid, subIds, year);
            return this.viewNegotiating(request, response, new ResultClient(rankingList));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
    * (员工端)早起鸟排行榜
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
            if(type == Constant.type_month){
                res = attendanceRankingService.getSubFirstTimesListByMonth(sid, subIds, currentMonth);
            }else if(type == Constant.type_month_search){
                res = attendanceRankingService.getSubFirstTimesListByMonth(sid, subIds, month);
            }else if(type == Constant.type_year_search){
                res = attendanceRankingService.getSubFirstTimesListByYear(sid, subIds, year);
            }else if(type == Constant.type_halfUpYear){
                res = attendanceRankingService.getSubFirstTimesListByUpYear(sid, subIds, currentYear);
            }else if(type == Constant.type_halfDownYear){
                res = attendanceRankingService.getSubFirstTimesListByDownYear(sid, subIds, currentYear);
            }
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }


    @RequestMapping("/getSubWorkingHourList")
    public ModelAndView getSubWorkingHourList(HttpServletRequest request, HttpServletResponse response,
                                             long sid, @RequestParam(name = "subIds[]") List<Long> subIds,
                                             @RequestParam(required = false, value = "year", defaultValue = "0") long year,
                                             @RequestParam(required = false, value = "month", defaultValue = "0") long month,
                                             int type) throws Exception {
        try {
            List<ClientWorkingHour> res = Lists.newArrayList();
            long currentMonth = TimeUtils.getMonthFormTime(System.currentTimeMillis());
            long currentYear = TimeUtils.getYearFormTime(System.currentTimeMillis());
            if(type == Constant.type_month){
                res = attendanceRankingService.getSubWorkingHourListByMonth(sid, subIds, currentMonth);
            }else if(type == Constant.type_month_search){
                res = attendanceRankingService.getSubWorkingHourListByMonth(sid, subIds, month);
            }else if(type == Constant.type_year_search){
                res = attendanceRankingService.getSubWorkingHourListByYear(sid, subIds, year);
            }else if(type == Constant.type_halfUpYear){
                res = attendanceRankingService.getSubWorkingHourListByUpYear(sid, subIds, currentYear);
            }else if(type == Constant.type_halfDownYear){
                res = attendanceRankingService.getSubWorkingHourListByDownYear(sid, subIds, currentYear);
            }
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

}
