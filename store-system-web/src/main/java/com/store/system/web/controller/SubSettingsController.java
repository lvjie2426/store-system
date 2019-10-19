package com.store.system.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.webframework.BaseController;
import com.store.system.bean.InventoryInBillItem;
import com.store.system.client.ClientSubordinate;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.attendance.PunchCardPlace;
import com.store.system.model.attendance.SubSettings;
import com.store.system.model.attendance.WirelessNetwork;
import com.store.system.service.SubSettingsService;
import com.store.system.service.SubordinateService;
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
    @Resource
    private SubordinateService subordinateService;

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
            ClientSubordinate subordinate = subordinateService.load(subId);
            long pSubid = subordinate.getPid();
            if(pSubid == 0) throw new StoreSystemException("公司为空！");
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
                               SubSettings subSettings,String placeJson,String netJson) throws Exception {
        try {
            List<PunchCardPlace> placeList = Lists.newArrayList();
            if(StringUtils.isNotBlank(placeJson)) {
                placeList = JsonUtils.fromJson(placeJson, new TypeReference<List<PunchCardPlace>>() {});
            }
            subSettings.setPunchCardPlaces(placeList);
            List<WirelessNetwork> netList = Lists.newArrayList();
            if(StringUtils.isNotBlank(netJson)) {
                netList = JsonUtils.fromJson(netJson, new TypeReference<List<WirelessNetwork>>() {});
            }
            subSettings.setWirelessNetworks(netList);
            subSettingsService.update(subSettings);
            return this.viewNegotiating(request, response, new ResultClient(true));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }
}
