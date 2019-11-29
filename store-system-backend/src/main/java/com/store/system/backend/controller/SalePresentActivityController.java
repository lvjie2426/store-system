package com.store.system.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.webframework.BaseController;
import com.store.system.bean.SalePresentItem;
import com.store.system.client.ClientSalePresentActivity;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.SalePresentActivity;
import com.store.system.service.SalePresentActivityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName SalePresentActivityController
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 11:34
 * @Version 1.0
 **/
@Controller
@RequestMapping("/salePresent")
public class SalePresentActivityController extends BaseController {

    @Resource
    private SalePresentActivityService salePresentActivityService;

    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response, SalePresentActivity salePresentActivity,
                            String itemsJson) throws Exception {
        try {
            List<SalePresentItem> items = Lists.newArrayList();
            if(StringUtils.isNotBlank(itemsJson)) {
                items = JsonUtils.fromJson(itemsJson, new TypeReference<List<SalePresentItem>>() {});
            }
            salePresentActivity.setItems(items);
            SalePresentActivity res = salePresentActivityService.add(salePresentActivity);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(HttpServletRequest request, HttpServletResponse response, long id) throws Exception {
        try {
            boolean res = salePresentActivityService.delete(id);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response, SalePresentActivity salePresentActivity,
                               String itemsJson) throws Exception {
        try {
            List<SalePresentItem> items = Lists.newArrayList();
            if(StringUtils.isNotBlank(itemsJson)) {
                items = JsonUtils.fromJson(itemsJson, new TypeReference<List<SalePresentItem>>() {});
            }
            salePresentActivity.setItems(items);
            boolean res = salePresentActivityService.update(salePresentActivity);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/updateOpen")
    public ModelAndView updateOpen(HttpServletRequest request, HttpServletResponse response,
                                     long id, int open) throws Exception {
        try {
            boolean res = salePresentActivityService.updateOpen(id, open);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(HttpServletRequest request, HttpServletResponse response, long psid) throws Exception {
        try {
            List<SalePresentActivity> res = salePresentActivityService.getAllList(psid);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getIngList")
    public ModelAndView getIngList(HttpServletRequest request, HttpServletResponse response, long psid) throws Exception {
        try {
            List<ClientSalePresentActivity> res = salePresentActivityService.getIngList(psid);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getHistoryList")
    public ModelAndView getHistoryList(HttpServletRequest request, HttpServletResponse response, long psid) throws Exception {
        try {
            List<ClientSalePresentActivity> res = salePresentActivityService.getHistoryList(psid);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }
}
