package com.store.system.backend.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Coupon;
import com.store.system.service.CouponService;
import com.store.system.service.CouponService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName CouponController
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/28 17:57
 * @Version 1.0
 **/
@Controller
@RequestMapping("/coupon")
public class CouponController extends BaseController{

    @Resource
    private CouponService couponService;

    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response, Coupon coupon) throws Exception {
        try {
            Coupon res = couponService.add(coupon);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(HttpServletRequest request, HttpServletResponse response, long id) throws Exception {
        try {
            boolean res = couponService.delete(id);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response, Coupon coupon) throws Exception {
        try {
            boolean res = couponService.update(coupon);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/updateStatus")
    public ModelAndView updateStatus(HttpServletRequest request, HttpServletResponse response,
                                     long id, int status) throws Exception {
        try {
            boolean res = couponService.updateStatus(id, status);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(HttpServletRequest request, HttpServletResponse response, long psid) throws Exception {
        try {
            List<Coupon> res = couponService.getAllList(psid);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getIngList")
    public ModelAndView getIngList(HttpServletRequest request, HttpServletResponse response, long psid) throws Exception {
        try {
            List<Coupon> res = couponService.getIngList(psid);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getHistoryList")
    public ModelAndView getHistoryList(HttpServletRequest request, HttpServletResponse response, long psid) throws Exception {
        try {
            List<Coupon> res = couponService.getHistoryList(psid);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }
}
