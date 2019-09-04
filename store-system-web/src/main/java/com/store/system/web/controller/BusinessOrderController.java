package com.store.system.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.bean.CalculateOrder;
import com.store.system.client.ClientBusinessOrder;
import com.store.system.client.ClientSettlementOrder;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.BusinessOrderService;
import com.store.system.service.DictionaryService;
import com.store.system.service.SubordinateService;
import com.store.system.util.*;
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
import java.util.Map;

/**
 * @ClassName BusinessOrderController
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/7/23 16:59
 * @Version 1.0
 **/
@Controller
@RequestMapping("/businessOrder")
public class BusinessOrderController extends BaseController{

    @Resource
    private BusinessOrderService businessOrderService;
    @Resource
    private SubordinateService subordinateService;
    @Resource
    private DictionaryService dictionaryService;

    /***
    * 创建订单(临时订单)
    * @Param: [request, response, businessOrder, skuJson, surchargesJson]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/7/24
    */
    @RequestMapping("/saveOrder")
    public ModelAndView saveOrder(HttpServletRequest request, HttpServletResponse response,
                                  BusinessOrder businessOrder, String skuJson, String surchargesJson) throws Exception {
        try {
            List<Surcharge> surcharges = Lists.newArrayList();
            List<OrderSku> skuList = Lists.newArrayList();
            if (StringUtils.isNotBlank(surchargesJson)) {
                surcharges = JsonUtils.fromJson(surchargesJson, new TypeReference<List<Surcharge>>() {});
                businessOrder.setSurcharges(surcharges);
            }
            if (StringUtils.isNotBlank(skuJson)) {
                skuList = JsonUtils.fromJson(skuJson, new TypeReference<List<OrderSku>>() {});
                businessOrder.setSkuList(skuList);
            }
            businessOrder.setOrderNo(CodeUtil.getCode());
            ClientBusinessOrder res = businessOrderService.add(businessOrder);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }

    }


    /***
     * 获取门店历史消费记录
     * @Param: [request, response, subid]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/7/24
     */
    @RequestMapping("/getOrderBySubid")
    public ModelAndView getOrderBySubid(HttpServletRequest request, HttpServletResponse response,
                                        long subid) throws Exception {

        try {
            Subordinate subordinate = subordinateService.load(subid);
            if (null == subordinate || subordinate.getPid() == 0) throw new StoreSystemException("分店ID错误");
            List<ClientBusinessOrder> orderList = businessOrderService.getAllList(subid);
            return this.viewNegotiating(request, response, new ResultClient(orderList));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }

    }


    /*** 
    * 查询订单
    * @Param: [request, response, subId, name, phone, orderNo, pager]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/7/24
    */ 
    @RequestMapping("/searchOrders")
    public ModelAndView searchOrders(HttpServletRequest request, HttpServletResponse response,
                                     long subId, String name, String phone, String orderNo,Pager pager) throws Exception {
        try {
            pager = businessOrderService.getBackPager(pager,subId,name,phone,orderNo);
            return this.viewNegotiating(request, response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }

    }

    @RequestMapping("/loadOrder")
    public ModelAndView loadOrder(HttpServletRequest request, HttpServletResponse response,
                                  long id) throws Exception {
        try {
            Map<String, Object> res = businessOrderService.loadOrder(id);
            return this.viewNegotiating(request, response, res);
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/updateOrder")
    public ModelAndView updateOrder(HttpServletRequest request, HttpServletResponse response,
                                    BusinessOrder businessOrder) throws Exception {
        try {
            boolean res = businessOrderService.update(businessOrder);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/updateMakeStatus")
    public ModelAndView updateMakeStatus(HttpServletRequest request, HttpServletResponse response,
                                    long id, int makeStatus) throws Exception {
        try {
            boolean res = businessOrderService.updateMakeStatus(id,makeStatus);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
    * 计算订单金额
    * @Param: [request, response, businessOrder, skuJson, surchargesJson]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/7/26
    */
    @RequestMapping("/currentCalculate")
    public ModelAndView countPrice(HttpServletRequest request, HttpServletResponse response,
                                   BusinessOrder businessOrder, String skuJson, String surchargesJson) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            List<Surcharge> surcharges = Lists.newArrayList();
            List<OrderSku> skuList = Lists.newArrayList();
            if (StringUtils.isNotBlank(surchargesJson)) {
                surcharges = JsonUtils.fromJson(surchargesJson, new TypeReference<List<Surcharge>>() {});
                businessOrder.setSurcharges(surcharges);
            }
            if (StringUtils.isNotBlank(skuJson)) {
                skuList = JsonUtils.fromJson(skuJson, new TypeReference<List<OrderSku>>() {});
                businessOrder.setSkuList(skuList);
            }
            businessOrder.setSubId(user.getSid());
            ResultClient res = businessOrderService.currentCalculate(businessOrder);
            return this.viewNegotiating(request, response, res);
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }

    }

    /***
     * 获取附加费
     * @Param: [request, response]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/7/18
     */
    @RequestMapping("/getSurcharge")
    public ModelAndView getSurcharge(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String[] res = dictionaryService.getStrings(DictionaryUtils.surcharge,null);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
     * 计算时间段内订单的收入
     * @Param: [request, response, startTime, endTime, subId]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/7/8
     */
    @RequestMapping("/calculateOrders")
    public ModelAndView calculateOrders(HttpServletRequest request, HttpServletResponse response,
                                        @RequestParam(required = false, value = "startTime", defaultValue = "0") long startTime,
                                        @RequestParam(required = false, value = "endTime", defaultValue = "0") long endTime,
                                        long subId) throws Exception {
        try {
            CalculateOrder res = businessOrderService.calculateBusinessOrder(subId, startTime, endTime);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }

    }

    /***
    * 支付结算
    * @Param: [request, response, boId, cash, stored, otherStored, score]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/8/8
    */
    @RequestMapping("/settlementPay")
    public ModelAndView settlementPay(HttpServletRequest request, HttpServletResponse response,
                                        @RequestParam(required = true) long boId,
                                        @RequestParam(required = false, value = "cash", defaultValue = "0") int cash,
                                        @RequestParam(required = false, value = "stored", defaultValue = "0") int stored,
                                        @RequestParam(required = false, value = "otherStored", defaultValue = "0") int otherStored) throws Exception {
        try {
            ClientSettlementOrder res = businessOrderService.settlementPay(boId,cash,stored,otherStored);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
     * 订单结算
     * @Param: [request, response, boId, cash, stored, otherStored, score, makeStatus]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/8/8
     */
    @RequestMapping("/settlementOrder")
    public ModelAndView settlementOrder(HttpServletRequest request, HttpServletResponse response,
                                        @RequestParam(required = true) long boId,
                                        @RequestParam(required = false, value = "cash", defaultValue = "0") int cash,
                                        @RequestParam(required = false, value = "stored", defaultValue = "0") int stored,
                                        @RequestParam(required = false, value = "otherStored", defaultValue = "0") int otherStored,
                                        @RequestParam(required = false, value = "score", defaultValue = "0") int score,
                                        @RequestParam(required = false, value = "makeStatus", defaultValue = "6") int makeStatus,
                                        String desc) throws Exception {
        try {
            ClientBusinessOrder res = businessOrderService.settlementOrder(boId, cash, stored, otherStored, score, makeStatus, desc);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }



    /***
     * 员工的订单记录
     * @Param: [request, response, pager]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/9/4
     */
    @RequestMapping("/getUserList")
    public ModelAndView getUserList(HttpServletRequest request, HttpServletResponse response,
                                    Pager pager) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            pager = businessOrderService.getPager(pager, user.getId(), BusinessOrder.status_pay, BusinessOrder.makeStatus_qu_yes);
            return this.viewNegotiating(request, response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }

    }

    /***
     * 今日销售记录
     * @Param: [request, response, pager, type, subId]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/8/24
     */
    @RequestMapping("/saleLogs")
    public ModelAndView saleLogs(HttpServletRequest request, HttpServletResponse response,
                                 Pager pager, int type, long subId,
                                 @RequestParam(required = false, value = "day", defaultValue = "0") long day) throws Exception {
        try {
            Subordinate subordinate = subordinateService.load(subId);
            if (null == subordinate || subordinate.getPid() == 0) throw new StoreSystemException("分店ID错误");
            if (type == Constant.type_today) {
                long startTime = TimeUtils.getTodayTime(0, 0, 0);
                long endTime = TimeUtils.getTodayTime(23, 59, 59);
                pager = businessOrderService.getPager(pager, subId, startTime, endTime,
                        BusinessOrder.status_pay, BusinessOrder.makeStatus_qu_yes);
            } else if (type == Constant.type_yesterday) {
                long startTime = TimeUtils.getYesterdayTime(0, 0, 0);
                long endTime = TimeUtils.getYesterdayTime(23, 59, 59);
                pager = businessOrderService.getPager(pager, subId, startTime, endTime,
                        BusinessOrder.status_pay, BusinessOrder.makeStatus_qu_yes);
            } else if (type == Constant.type_week) {
                long startTime = TimeUtils.getWeekFirstTime();
                long endTime = TimeUtils.getWeekLastTime();
                pager = businessOrderService.getPager(pager, subId, startTime, endTime,
                        BusinessOrder.status_pay, BusinessOrder.makeStatus_qu_yes);
            } else if (type == Constant.type_month) {
                long startTime = TimeUtils.getMonthFirstTime();
                long endTime = TimeUtils.getMonthLastTime();
                pager = businessOrderService.getPager(pager, subId, startTime, endTime,
                        BusinessOrder.status_pay, BusinessOrder.makeStatus_qu_yes);
            } else if (type == Constant.type_search) {
                if (day == 0) {
                    throw new StoreSystemException("请选择日期！");
                }
                pager = businessOrderService.getPager(pager, subId, day,
                        BusinessOrder.status_pay, BusinessOrder.makeStatus_qu_yes);
            }
            return this.viewNegotiating(request, response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }

    }


}
