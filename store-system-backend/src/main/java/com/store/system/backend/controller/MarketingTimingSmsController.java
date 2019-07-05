package com.store.system.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.MarketingTimingSms;
import com.store.system.service.MarketingTimingSmsService;
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

@Controller
@RequestMapping("/marketingtimingsms")
public class MarketingTimingSmsController extends BaseController {

    @Resource
    private MarketingTimingSmsService marketingTimingSmsService;

    @RequestMapping("/add")
    public ModelAndView add(MarketingTimingSms marketingTimingSms, String tagsJson, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            List<String> tags = Lists.newArrayList();
            if(StringUtils.isNotBlank(tagsJson)) {
                tags = JsonUtils.fromJson(tagsJson, new TypeReference<List<String>>() {});
            }
            marketingTimingSms.setTags(tags);
            marketingTimingSms = marketingTimingSmsService.add(marketingTimingSms);
            return this.viewNegotiating(request,response, new ResultClient(marketingTimingSms));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(MarketingTimingSms marketingTimingSms, HttpServletRequest request, HttpServletResponse response, Model model,
                               String tagsJson) throws Exception {
        try {
            List<String> tags = Lists.newArrayList();
            if(StringUtils.isNotBlank(tagsJson)) {
                tags = JsonUtils.fromJson(tagsJson, new TypeReference<List<String>>() {});
            }
            marketingTimingSms.setTags(tags);
            boolean res = marketingTimingSmsService.update(marketingTimingSms);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(@RequestParam(value = "id") long id,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = marketingTimingSmsService.del(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getCheckPager")
    public ModelAndView getCheckPager(@RequestParam(value = "subid") long subid,
                                      Pager pager, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            pager = marketingTimingSmsService.getBackPager(pager, subid);
            return this.viewNegotiating(request,response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

}
