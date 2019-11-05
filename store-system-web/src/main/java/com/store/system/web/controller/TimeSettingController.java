package com.store.system.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.webframework.BaseController;
import com.store.system.bean.InventoryInBillItem;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.User;
import com.store.system.model.attendance.TimeSetting;
import com.store.system.service.TimeSettingService;
import com.store.system.util.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName TimeSettingController
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/5 17:47
 * @Version 1.0
 **/
@Controller
@RequestMapping("/timeSetting")
public class TimeSettingController extends BaseController{

    @Resource
    private TimeSettingService timeSettingService;

    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response, Model model, TimeSetting timeSetting) throws Exception {
        try {
            timeSetting = timeSettingService.add(timeSetting);
            return this.viewNegotiating(request, response, new ResultClient(timeSetting));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/addList")
    public ModelAndView addList(HttpServletRequest request, HttpServletResponse response, Model model,
                                @RequestParam(value = "timeSettingJson") String timeSettingJson) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            List<TimeSetting> timeSettings = Lists.newArrayList();
            if(StringUtils.isNotBlank(timeSettingJson)) {
                timeSettings = JsonUtils.fromJson(timeSettingJson, new TypeReference<List<TimeSetting>>() {});
            }
            timeSettingService.addList(timeSettings,user.getPsid());
            return this.viewNegotiating(request, response, new ResultClient(true));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }


    @RequestMapping("/del")
    public ModelAndView del(HttpServletRequest request, HttpServletResponse response, Model model, long id) throws Exception {
        try {
            boolean res = timeSettingService.del(id);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response, Model model,
                               @RequestParam(value = "timeSettingJson") String timeSettingJson) throws Exception {
        try {
            List<TimeSetting> timeSettings = Lists.newArrayList();
            if(StringUtils.isNotBlank(timeSettingJson)) {
                timeSettings = JsonUtils.fromJson(timeSettingJson, new TypeReference<List<TimeSetting>>() {});
            }
            for(TimeSetting timeSetting:timeSettings) {
                timeSettingService.update(timeSetting);
            }
            return this.viewNegotiating(request, response, new ResultClient(true));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(HttpServletRequest request, HttpServletResponse response, Model model, long sid) throws Exception {
        try {
            List<TimeSetting> res = timeSettingService.getAllList(sid);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

}
