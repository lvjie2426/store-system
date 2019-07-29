package com.store.system.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.BusinessOrderService;
import com.store.system.service.DictionaryService;
import com.store.system.service.SubordinateService;
import com.store.system.util.DictionaryUtils;
import com.store.system.util.UserUtils;
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


    /**
    * 获取全部订单/作废订单 makeStatus=5
    * @Param: [request, response, pager, name, model, startTime, endTime, staffId, status, uid, subId, makeStatus]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/7/23
    */
    @RequestMapping("/getAllList")
    public ModelAndView getSubOrder(HttpServletRequest request, HttpServletResponse response,
                                    Pager pager,String name, Model model,
                                    @RequestParam(required = false, value = "startTime", defaultValue = "0") long startTime,
                                    @RequestParam(required = false, value = "endTime", defaultValue = "0") long endTime,
                                    @RequestParam(required = false, value = "staffId", defaultValue = "0") long staffId,
                                    @RequestParam(required = false, value = "status", defaultValue = "0") int status,
                                    @RequestParam(required = false, value = "uid", defaultValue = "0") long uid,
                                    @RequestParam(required = false, value = "subId", defaultValue = "0") long subId,
                                    @RequestParam(required = false, value = "makeStatus", defaultValue = "0") int makeStatus) throws Exception {
        try {
            Subordinate subordinate = subordinateService.load(subId);
            if (null == subordinate || subordinate.getPid() == 0) throw new StoreSystemException("分店ID错误");
            pager = businessOrderService.getAllList(pager, startTime, endTime, staffId, status, uid, name, makeStatus, subId);
            return this.viewNegotiating(request, response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }

    }

    /**
    *   description: 获取未完成订单 makeStatus不传。
    *   根据订单状态查询，makeStatus =1=2=3
    * @Param: [request, response, pager, startTime, subId, endTime, staffId, status, uid, name, makeStatus, model]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/7/23
    */
    @RequestMapping("/getIncomplete")
    public ModelAndView getIncomplete(HttpServletRequest request, HttpServletResponse response,
                                      Pager pager,Model model, String name,
                                      @RequestParam(required = false, value = "startTime", defaultValue = "0") long startTime,
                                      @RequestParam(required = false, value = "endTime", defaultValue = "0") long endTime,
                                      @RequestParam(required = false, value = "staffId", defaultValue = "0") long staffId,
                                      @RequestParam(required = false, value = "status", defaultValue = "0") int status,
                                      @RequestParam(required = false, value = "uid", defaultValue = "0") long uid,
                                      @RequestParam(required = false, value = "subId", defaultValue = "0") long subId,
                                      @RequestParam(required = false, value = "makeStatus", defaultValue = "0") int makeStatus) throws Exception {

        try {
            Subordinate subordinate = subordinateService.load(subId);
            if (null == subordinate || subordinate.getPid() == 0) throw new StoreSystemException("分店ID错误");
            pager = businessOrderService.getUnfinishedList(pager, startTime, endTime, staffId, status, uid, name, subId, makeStatus);
            return this.viewNegotiating(request, response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }

    }

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
            businessOrder = businessOrderService.add(businessOrder);
            return this.viewNegotiating(request, response, new ResultClient(businessOrder));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }

    }

    /***
    * 获取临时订单
    * @Param: [request, response, subId]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/7/24
    */
    @RequestMapping("/getTemporaryOrder")
    public ModelAndView getTemporaryOrder(HttpServletRequest request, HttpServletResponse response,
                                          long subId) throws Exception {

        try {
            List<BusinessOrder> orderList = businessOrderService.getAllList(subId);
            return this.viewNegotiating(request, response, new ResultClient(orderList));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
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
            List<BusinessOrder> orderList = businessOrderService.getAllList(subid);
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
/*    @RequestMapping("/calculateOrders")
    public ModelAndView calculateOrders(HttpServletRequest request, HttpServletResponse response,
                                        @RequestParam(required = false, value = "startTime", defaultValue = "0") long startTime,
                                        @RequestParam(required = false, value = "endTime", defaultValue = "0") long endTime,
                                        long subId) throws Exception {
        try {
            CalculateOrder res = orderService.calculateOrders(subId, startTime, endTime);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }

    }*/




}