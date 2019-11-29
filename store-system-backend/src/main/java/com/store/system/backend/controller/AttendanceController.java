package com.store.system.backend.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientAttendanceInfo;
import com.store.system.client.ClientAttendanceLog;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.User;
import com.store.system.model.attendance.AttendanceLog;
import com.store.system.model.attendance.PunchCardLog;
import com.store.system.service.AttendanceLogService;
import com.store.system.service.UserService;
import com.store.system.util.Constant;
import com.store.system.util.TimeUtils;
import com.store.system.util.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.CollaboratingWorkbooksEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/attendance")
public class AttendanceController extends BaseController {

    @Autowired
    private AttendanceLogService attendanceLogService;
    @Resource
    private UserService userService;


    /***
     * 获取用户某个时间正常的考勤日志
     * @Param: [request, response, month, week, day, uid, type, model]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/9/20
     */
    @RequestMapping("/getUserNormalList")
    public ModelAndView getUserNormalList(HttpServletRequest request, HttpServletResponse response,
                                          @RequestParam(name = "month", required = false, defaultValue = "0") long month,
                                          @RequestParam(name = "days[]", required = false) List<Long> days,
                                          @RequestParam(name = "day", required = false, defaultValue = "0") long day,
                                          long uid, int type, Model model) throws Exception {
        try {
            User user = userService.load(uid);
            List<ClientAttendanceLog> res = Lists.newArrayList();
            if (type == Constant.type_day_search) {
                res = attendanceLogService.getAllListByDay(user.getPsid(), user.getSid(), uid, day, true);
            } else if (type == Constant.type_week_search) {
                res = attendanceLogService.getAllListByDay(user.getPsid(), user.getSid(), uid, days, true);
            } else if (type == Constant.type_month_search) {
                res = attendanceLogService.getAllListByMonth(user.getPsid(), user.getSid(), uid, month, true);
            }
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
     * 修改考勤的打卡开始时间和打卡结束时间
     * @Param: [request, response, attendanceLog, model]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/9/17
     */
    @RequestMapping("/update")
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response,
                               AttendanceLog attendanceLog, Model model) throws Exception {
        try {
            boolean res = attendanceLogService.update(attendanceLog);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
     * 考勤汇总月统计
     * @Param: [request, response, subIds, month, model]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/9/17
     */
    @RequestMapping("/statisticsMonth")
    public ModelAndView statisticsMonth(HttpServletRequest request, HttpServletResponse response,
                                        @RequestParam(name = "subIds[]") List<Long> subIds, long month, int type, Model model) throws Exception {
        try {
            Map<Long, ClientAttendanceInfo> res = attendanceLogService.getUserStatisticsMonth(subIds, month, type);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }


    /***
     * 获取门店某月 考勤统计  获取门店下全部员工的考勤记录
     * @Param: [request, response, month, week, day, subIds, type, timeType, model]
     * @return: org.springframework.web.servlet.ModelAndView
     */
    @RequestMapping("/statistics")
    public ModelAndView statistics(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(name = "startTime", defaultValue = "0") long startTime,
                                   @RequestParam(name = "endTime", defaultValue = "0") long endTime,
                                   @RequestParam(name = "subIds") long subId, Model model) throws Exception {
        try {
            List<User> users = userService.getAllUserBySid(subId);
            List<Long> ids = users.stream().map(User::getId).collect(Collectors.toList());
            Map<Long,ClientAttendanceInfo> res= attendanceLogService.getUserStatisticsAttendance(users.get(0).getPsid(), ids, startTime, endTime);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
     * 获取个人考勤记录详细记录
     * @Param: [request, response, month, week, day, subIds, type, timeType, model]
     * @return: org.springframework.web.servlet.ModelAndView
     */
    @RequestMapping("/getAllByUid")
    public ModelAndView getAllByUid(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(name = "startTime", defaultValue = "0") long startTime,
                                   @RequestParam(name = "endTime", defaultValue = "0") long endTime,
                                   @RequestParam(name = "uid") long uid,
                                    Model model) throws Exception {
        try {
            List<ClientAttendanceLog> res= attendanceLogService.getUserAttendanceLog(uid,startTime, endTime);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
     * 获取前n天 后-n天的日期 0为当日
     */
    @RequestMapping("/getDay")
    public ModelAndView getDay(HttpServletRequest request, HttpServletResponse response,
                               int num) throws Exception {
        try {
            return this.viewNegotiating(request, response, new ResultClient(TimeUtils.getDay(num)));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
     * 获取前n周 后-n周的日期 0为当前周
     */
    @RequestMapping("/getWeek")
    public ModelAndView getWeek(HttpServletRequest request, HttpServletResponse response,
                                int num) throws Exception {
        try {
            return this.viewNegotiating(request, response, new ResultClient(TimeUtils.getWeekToDays(num)));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

}
