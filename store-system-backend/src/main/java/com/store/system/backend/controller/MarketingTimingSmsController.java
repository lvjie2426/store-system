package com.store.system.backend.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.MarketingTimingSms;
import com.store.system.service.MarketingTimingSmsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/marketingtimingsms")
public class MarketingTimingSmsController extends BaseController {

    @Resource
    private MarketingTimingSmsService marketingTimingSmsService;

    @RequestMapping("/add")
    public ModelAndView add(MarketingTimingSms marketingTimingSms, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            marketingTimingSms = marketingTimingSmsService.add(marketingTimingSms);
            return this.viewNegotiating(request,response, new ResultClient(marketingTimingSms));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(MarketingTimingSms marketingTimingSms, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
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
