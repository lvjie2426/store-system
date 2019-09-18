package com.store.system.web.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.attendance.SubSettings;
import com.store.system.service.SubSettingsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName SubSettingsController
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/9/18 18:25
 * @Version 1.0
 **/
@Controller
@RequestMapping("/subSettings")
public class SubSettingsController extends BaseController {

    @Resource
    private SubSettingsService subSettingsService;

    /***
    * 获取门店设置详情
    * @Param: [request, response, model, subId]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/9/18
    */
    @RequestMapping("/load")
    public ModelAndView load(HttpServletRequest request, HttpServletResponse response, Model model,
                             long subId) throws Exception {
        try {
            SubSettings subSettings = subSettingsService.load(subId);
            return this.viewNegotiating(request, response, new ResultClient(subSettings));
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
                               SubSettings subSettings) throws Exception {
        try {
            subSettingsService.add(subSettings);
            return this.viewNegotiating(request, response, new ResultClient(true));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }
}
