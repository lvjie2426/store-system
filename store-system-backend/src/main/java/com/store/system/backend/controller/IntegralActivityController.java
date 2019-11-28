package com.store.system.backend.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.IntegralActivity;
import com.store.system.service.IntegralActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName IntegralActivityController
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 14:24
 * @Version 1.0
 **/
@Controller
@RequestMapping("/integral")
public class IntegralActivityController extends BaseController{

    @Resource
    private IntegralActivityService integralActivityService;

    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response, IntegralActivity integralActivity) throws Exception {
        try {
            IntegralActivity res = integralActivityService.add(integralActivity);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(HttpServletRequest request, HttpServletResponse response, long id) throws Exception {
        try {
            boolean res = integralActivityService.delete(id);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response, IntegralActivity integralActivity) throws Exception {
        try {
            boolean res = integralActivityService.update(integralActivity);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/updateStatus")
    public ModelAndView updateStatus(HttpServletRequest request, HttpServletResponse response,
                                     long id, int status) throws Exception {
        try {
            boolean res = integralActivityService.updateStatus(id, status);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(HttpServletRequest request, HttpServletResponse response, long psid) throws Exception {
        try {
            List<IntegralActivity> res = integralActivityService.getAllList(psid);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getIngList")
    public ModelAndView getIngList(HttpServletRequest request, HttpServletResponse response, long psid) throws Exception {
        try {
            List<IntegralActivity> res = integralActivityService.getIngList(psid);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getHistoryList")
    public ModelAndView getHistoryList(HttpServletRequest request, HttpServletResponse response, long psid) throws Exception {
        try {
            List<IntegralActivity> res = integralActivityService.getHistoryList(psid);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }
}
