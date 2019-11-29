package com.store.system.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.webframework.BaseController;
import com.store.system.bean.ComboItem;
import com.store.system.client.ClientComboActivity;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ComboActivity;
import com.store.system.service.ComboActivityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName ComboActivityController
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/29 14:34
 * @Version 1.0
 **/
@Controller
@RequestMapping("/combo")
public class ComboActivityController extends BaseController {

    @Resource
    private ComboActivityService comboActivityService;

    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response, ComboActivity comboActivity,
                            String skuIdsJson, String itemsJson) throws Exception {
        try {
            List<Long> skuIds = Lists.newArrayList();
            if(StringUtils.isNotBlank(skuIdsJson)) {
                skuIds = JsonUtils.fromJson(skuIdsJson, new TypeReference<List<Long>>() {});
            }
            comboActivity.setSkuIds(skuIds);
            List<ComboItem> comboItems = Lists.newArrayList();
            if(StringUtils.isNotBlank(itemsJson)) {
                comboItems = JsonUtils.fromJson(itemsJson, new TypeReference<List<ComboItem>>() {});
            }
            comboActivity.setItems(comboItems);
            ComboActivity res = comboActivityService.add(comboActivity);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(HttpServletRequest request, HttpServletResponse response, long id) throws Exception {
        try {
            boolean res = comboActivityService.delete(id);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response, ComboActivity comboActivity,
                               String skuIdsJson, String itemsJson) throws Exception {
        try {
            List<Long> skuIds = Lists.newArrayList();
            if(StringUtils.isNotBlank(skuIdsJson)) {
                skuIds = JsonUtils.fromJson(skuIdsJson, new TypeReference<List<Long>>() {});
            }
            comboActivity.setSkuIds(skuIds);
            List<ComboItem> comboItems = Lists.newArrayList();
            if(StringUtils.isNotBlank(itemsJson)) {
                comboItems = JsonUtils.fromJson(itemsJson, new TypeReference<List<ComboItem>>() {});
            }
            comboActivity.setItems(comboItems);
            boolean res = comboActivityService.update(comboActivity);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/updateStatus")
    public ModelAndView updateStatus(HttpServletRequest request, HttpServletResponse response,
                                     long id, int status) throws Exception {
        try {
            boolean res = comboActivityService.updateStatus(id, status);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(HttpServletRequest request, HttpServletResponse response, long psid) throws Exception {
        try {
            List<ClientComboActivity> res = comboActivityService.getAllList(psid);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }


}
