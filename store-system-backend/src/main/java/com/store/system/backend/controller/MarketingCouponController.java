package com.store.system.backend.controller;

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

    @RequestMapping("/add")
    public ModelAndView add(MarketingCoupon marketingCoupon, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            marketingCoupon = marketingCouponService.add(marketingCoupon);
            return this.viewNegotiating(request,response, new ResultClient(marketingCoupon));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/updateOpen")
    public ModelAndView updateOpen(@RequestParam(value = "id") long id,
                                   @RequestParam(value = "open") int open, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = marketingCouponService.updateOpen(id, open);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/updateSort")
    public ModelAndView updateSort(@RequestParam(value = "id") long id,
                                   @RequestParam(value = "sort") long sort, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = marketingCouponService.updateSort(id, sort);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(@RequestParam(value = "id") long id,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = marketingCouponService.del(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(@RequestParam(value = "subid") long subid,Pager pager,
                                   HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            pager = marketingCouponService.getBackPager(pager,subid);
            return this.viewNegotiating(request,response,  new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

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

    //编辑抵用券
    @RequestMapping("/updateMarketing")
    public ModelAndView updateMarketing(HttpServletRequest request,HttpServletResponse response,
                                        MarketingCoupon marketingCoupon)throws Exception{
        try {
            boolean res =  marketingCouponService.updateMarketing(marketingCoupon);
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    /***
    * 计算优惠金额
    * @Param: [request, response, model, mcId, money, num]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/7/20
    */
    @RequestMapping("/calculateMoney")
    public ModelAndView calculateMoney(HttpServletRequest request, HttpServletResponse response, Model model,
                                       @RequestParam(value = "mcId") long mcId,
                                       @RequestParam(value = "money", defaultValue = "0") int money,
                                       @RequestParam(value = "num", defaultValue = "0") int num) throws Exception {
        try {
            int res = marketingCouponService.calculateMoney(mcId, num, money);
            return this.viewNegotiating(request,response,  new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

}
