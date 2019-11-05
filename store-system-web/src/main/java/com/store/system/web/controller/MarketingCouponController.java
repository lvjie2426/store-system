package com.store.system.web.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.MarketingCoupon;
import com.store.system.service.MarketingCouponService;
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
@RequestMapping("/marketingcoupon")
public class MarketingCouponController extends BaseController {

    @Resource
    private MarketingCouponService marketingCouponService;


    @RequestMapping("/getCanUseList")
    public ModelAndView getCanUseList(@RequestParam(value = "subid") long subid,
                                      @RequestParam(value = "money", defaultValue = "0") int money,
                                      @RequestParam(value = "num", defaultValue = "0") int num,
                                   HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            long time = System.currentTimeMillis();
            List<MarketingCoupon> coupons = marketingCouponService.getCanUseList(subid, money, num, time);
            return this.viewNegotiating(request,response, new ResultClient(true, coupons));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }


}
