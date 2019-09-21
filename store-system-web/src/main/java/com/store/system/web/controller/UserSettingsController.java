package com.store.system.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientSubordinate;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.User;
import com.store.system.model.attendance.PunchCardPlace;
import com.store.system.model.attendance.SubSettings;
import com.store.system.model.attendance.UserSettings;
import com.store.system.model.attendance.WirelessNetwork;
import com.store.system.service.SubSettingsService;
import com.store.system.service.SubordinateService;
import com.store.system.service.UserSettingsService;
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

/**
 * create by: zhangmeng
 * description: TODO
 * create time: 2019/09/20 0020 18:23:38
 *
 * @Param: null
 * @return
 */
@Controller
@RequestMapping("/userSettings")
public class UserSettingsController extends BaseController {

    @Resource
    private UserSettingsService userSettingsService;
    @Resource
    private SubordinateService subordinateService;

    /***
    * 用户详情设置
    * @Param: [request, response, model, subId]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/9/18
    */
    @RequestMapping("/load")
    public ModelAndView load(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            UserSettings userSettings = userSettingsService.load(user.getId());
            return this.viewNegotiating(request, response, new ResultClient(userSettings));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
    * 修改设置
    * @Param: [request, response, model, subSettings]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/9/18
    */
    @RequestMapping("/update")
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response, Model model,
                               UserSettings userSettings) throws Exception {
        try {

            userSettingsService.update(userSettings);
            return this.viewNegotiating(request, response, new ResultClient(true));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }
}
