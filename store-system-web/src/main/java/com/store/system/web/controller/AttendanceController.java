package com.store.system.web.controller;

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
import com.store.system.util.UserUtils;
import org.apache.commons.lang3.StringUtils;
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

@Controller
@RequestMapping("/attendance")
public class AttendanceController extends BaseController {

    @Autowired
    private AttendanceLogService attendanceLogService;
    @Resource
    private UserService userService;

    /***
     * 打卡
     * @Param: [request, response, time, wifeNumber, wifeName, punchCardPlace, mapData, model]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/9/17
     */
    @RequestMapping("/add")
    public ModelAndView addAttendance(HttpServletRequest request, HttpServletResponse response,
                                      long time, String wifeNumber, String wifeName, String punchCardPlace, String mapData, Model model) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            if (StringUtils.isNotBlank(wifeName) && StringUtils.isNotBlank(wifeNumber)) {
                attendanceLogService.insertAttendanceLog(user.getId(), user.getPsid(), user.getSid(), time, "",
                        PunchCardLog.punchCardType_wifi, 0, "", 0, wifeNumber, wifeName, "", "", "");
            } else if (StringUtils.isNotBlank(punchCardPlace) && StringUtils.isNotBlank(mapData)) {
                attendanceLogService.insertAttendanceLog(user.getId(), user.getPsid(), user.getSid(), time, "",
                        PunchCardLog.punchCardType_gps, 0, "", 0, "", "", punchCardPlace, mapData, "");
            }
            return this.viewNegotiating(request, response, new ResultClient());
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 获取某天的考勤详情
     *
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/loadAttendanceLog")
    public ModelAndView loadAttendanceLog(HttpServletRequest request, HttpServletResponse response,
                                          long day, Model model) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            ClientAttendanceLog clientAttendanceLog = attendanceLogService.loadAttendanceLog(user.getId(), day);
//            if(clientAttendanceLog.getStart()<0 &&
//                    clientAttendanceLog.getEnd()<0){
//                return this.viewNegotiating(request, response, new ResultClient(true,"尚未设置考勤规则！请联系管理员！"));
//            }
            return this.viewNegotiating(request, response, new ResultClient(clientAttendanceLog));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
     * 获取用户某月的考勤记录和统计
     * @Param: [request, response, month, model]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/9/17
     */
    @RequestMapping("/getUserMonth")
    public ModelAndView getUserMonth(HttpServletRequest request, HttpServletResponse response,
                                     long month, Model model) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            ClientAttendanceInfo clientAttendanceInfo = attendanceLogService.
                    getUserAttendanceByMonth(user.getPsid(), user.getSid(), user.getId(), month);
            return this.viewNegotiating(request, response, new ResultClient(clientAttendanceInfo));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

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
                                          @RequestParam(name = "week", required = false, defaultValue = "0") long week,
                                          @RequestParam(name = "day", required = false, defaultValue = "0") long day,
                                          long uid, int type, Model model) throws Exception {
        try {
            User user = userService.load(uid);
            List<ClientAttendanceLog> res = Lists.newArrayList();
            if (type == Constant.type_day_search) {
                res = attendanceLogService.getAllListByDay(user.getPsid(), user.getSid(), uid, day, true);
            } else if (type == Constant.type_week_search) {
                res = attendanceLogService.getAllListByWeek(user.getPsid(), user.getSid(), uid, week, true);
            } else if (type == Constant.type_month_search) {
                res = attendanceLogService.getAllListByMonth(user.getPsid(), user.getSid(), uid, month, true);
            }
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
     * 获取用户某个时间迟到的考勤日志
     * @Param: [request, response, month, week, day, uid, type, model]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/9/20
     */
    @RequestMapping("/getUserLateList")
    public ModelAndView getUserLateList(HttpServletRequest request, HttpServletResponse response,
                                        @RequestParam(name = "month", required = false, defaultValue = "0") long month,
                                        @RequestParam(name = "week", required = false, defaultValue = "0") long week,
                                        @RequestParam(name = "day", required = false, defaultValue = "0") long day,
                                        long uid, int type, Model model) throws Exception {
        try {
            User user = userService.load(uid);
            List<ClientAttendanceLog> res = Lists.newArrayList();
            if (type == Constant.type_day_search) {
                res = attendanceLogService.getAllListByDay(user.getPsid(), user.getSid(), uid, day, false);
            } else if (type == Constant.type_week_search) {
                res = attendanceLogService.getAllListByWeek(user.getPsid(), user.getSid(), uid, week, false);
            } else if (type == Constant.type_month_search) {
                res = attendanceLogService.getAllListByMonth(user.getPsid(), user.getSid(), uid, month, false);
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
     * 考勤汇总周统计
     * @Param: [request, response, subIds, week, model]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/9/17
     */
    @RequestMapping("/statisticsWeek")
    public ModelAndView statisticsWeek(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam(name = "subIds[]") List<Long> subIds, long week, int type, Model model) throws Exception {
        try {
            Map<Long, ClientAttendanceInfo> res = attendanceLogService.getUserStatisticsWeek(subIds, week, type);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
     * 考勤汇总日统计
     * @Param: [request, response, subIds, day, model]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/9/17
     */
    @RequestMapping("/statisticsDay")
    public ModelAndView statisticsDay(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam(name = "subIds[]") List<Long> subIds, long day, int type, Model model) throws Exception {
        try {
            Map<Long, ClientAttendanceInfo> res = attendanceLogService.getUserStatisticsDay(subIds, day, type);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
    * 获取多个门店某月、某周、某日不同状态的考勤统计
    * @Param: [request, response, month, week, day, subIds, type, timeType, model]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/9/20
    */
    @RequestMapping("/statistics")
    public ModelAndView statistics(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(name = "month", required = false, defaultValue = "0") long month,
                                   @RequestParam(name = "week", required = false, defaultValue = "0") long week,
                                   @RequestParam(name = "day", required = false, defaultValue = "0") long day,
                                   @RequestParam(name = "subIds[]") List<Long> subIds, int type, int timeType, Model model) throws Exception {
        try {
            Map<Long, ClientAttendanceInfo> res = Maps.newHashMap();
            if (timeType == Constant.type_day_search) {
                res = attendanceLogService.getUserStatisticsDay(subIds, day, type);
            } else if (timeType == Constant.type_week_search) {
                res = attendanceLogService.getUserStatisticsWeek(subIds, week, type);
            } else if (timeType == Constant.type_month_search) {
                res = attendanceLogService.getUserStatisticsMonth(subIds, month, type);
            }
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
     * 获取用户某月的需要补卡考勤
     * @Param: [request, response, month, model]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/9/17
     */
    @RequestMapping("/getUserBuMonth")
    public ModelAndView getUserBuMonth(HttpServletRequest request, HttpServletResponse response,
                                     long month, Model model) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            List<ClientAttendanceLog> list= attendanceLogService.
                    getUserAttendanceBuByMonth(user.getPsid(), user.getSid(), user.getId(), month);
            return this.viewNegotiating(request, response, new ResultClient(list));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

}
