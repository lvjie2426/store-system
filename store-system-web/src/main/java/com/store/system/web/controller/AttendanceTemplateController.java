package com.store.system.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientAttendanceTemplate;
import com.store.system.client.ClientUser;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.User;
import com.store.system.model.attendance.AttendanceItem;
import com.store.system.model.attendance.AttendanceTemplate;
import com.store.system.service.AttendanceTemplateService;
import com.store.system.service.UserService;
import com.store.system.util.TimeUtils;
import com.store.system.util.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AttendanceTemplateController
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/8/13 16:03
 * @Version 1.0
 **/
@Controller
@RequestMapping("/attendanceTemplate")
public class AttendanceTemplateController extends BaseController {

    @Resource
    private AttendanceTemplateService attendanceTemplateService;
    @Resource
    private UserService userService;

    /***
    *http://127.0.0.1:30005/attendanceTemplate/add?token=32W0bSEA6qxq7l6pLlgPYA4BQd24GQsze9jvfHTr6yk%3D&name=xxx&sid=1&subId=5&uid=42&type=1&holidayType=1&turnMapJson={"1":{"start":600,"end":1200,"早班":{"start":600,"end":1200}}}
    * @Param: [request, response, attendanceTemplate, turnMapJson, model]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/9/18
    */
    @RequestMapping("/add")
    public ModelAndView addAttendanceTemplate(HttpServletRequest request, HttpServletResponse response,
                                              AttendanceTemplate attendanceTemplate,String dayJson, String turnMapJson, Model model) throws Exception {
        Map<Long,AttendanceItem> turnMap = Maps.newHashMap();
        List<Integer> days = Lists.newArrayList();
        try {
            if(StringUtils.isNotBlank(turnMapJson)) {
                turnMap = JsonUtils.fromJson(turnMapJson, new TypeReference<Map<Long, AttendanceItem>>() {});
            }
            if(StringUtils.isNotBlank(dayJson)) {
                days = JsonUtils.fromJson(dayJson, new TypeReference<List<Integer>>() {});
            }
            attendanceTemplate.setWorkWeekDay(days);
            attendanceTemplate.setTurnMap(turnMap);
            attendanceTemplate.setTurnStartDay(TimeUtils.getDayFormTime(attendanceTemplate.getTurnStartDay()));
            AttendanceTemplate template = attendanceTemplateService.add(attendanceTemplate);
            return this.viewNegotiating(request, response, new ResultClient(true, template));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
    * http://127.0.0.1:30005/attendanceTemplate/getAllList?token=32W0bSEA6qxq7l6pLlgPYA4BQd24GQsze9jvfHTr6yk%3D&subId=5
    * @Param: [request, response, subId]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/9/18
    */
    @RequestMapping("/getAllList")
    public ModelAndView getAllList(HttpServletRequest request, HttpServletResponse response,
                                        long subId) throws Exception {
        try {
            List<ClientAttendanceTemplate> res = attendanceTemplateService.getAllList(subId);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
    * 未设置排班的员工列表
    * @Param: [request, response, subId]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/9/18
    */
    @RequestMapping("/getAllUnSetList")
    public ModelAndView getAllUnSetList(HttpServletRequest request, HttpServletResponse response,
                                        long subId) throws Exception {
        try {
            List<ClientUser> res = userService.getAllUser(subId, User.userType_backendUser,0);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /**
     * 获取个人排班详细列表
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/getUserList")
    public ModelAndView getUserList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            User user= UserUtils.getUser(request);
            ClientAttendanceTemplate res = attendanceTemplateService.getUserList(user.getId());
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }
}
