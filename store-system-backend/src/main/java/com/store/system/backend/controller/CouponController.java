package com.store.system.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.webframework.BaseController;
import com.store.system.bean.InventoryInBillItem;
import com.store.system.client.ClientCoupon;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Coupon;
import com.store.system.service.CouponService;
import com.store.system.service.CouponService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response, Coupon coupon,
                            String skuIdsJson) throws Exception {
        try {
            List<Long> skuIds = Lists.newArrayList();
            if(StringUtils.isNotBlank(skuIdsJson)) {
                skuIds = JsonUtils.fromJson(skuIdsJson, new TypeReference<List<Long>>() {});
            }
            coupon.setSkuIds(skuIds);
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
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response, Coupon coupon,
                               String skuIdsJson) throws Exception {
        try {
            List<Long> skuIds = Lists.newArrayList();
            if(StringUtils.isNotBlank(skuIdsJson)) {
                skuIds = JsonUtils.fromJson(skuIdsJson, new TypeReference<List<Long>>() {});
            }
            coupon.setSkuIds(skuIds);
            boolean res = couponService.update(coupon);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/updateOpen")
    public ModelAndView updateOpen(HttpServletRequest request, HttpServletResponse response,
                                     long id, int open) throws Exception {
        try {
            boolean res = couponService.updateOpen(id, open);
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
            List<ClientCoupon> res = couponService.getIngList(psid);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getHistoryList")
    public ModelAndView getHistoryList(HttpServletRequest request, HttpServletResponse response, long psid) throws Exception {
        try {
            List<ClientCoupon> res = couponService.getHistoryList(psid);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }
}
