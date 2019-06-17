package com.store.system.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.quakoo.baseFramework.jackson.JsonUtils;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.bean.InventoryOutBillItem;
import com.store.system.client.ClientOrder;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.dao.SubordinateDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.*;
import com.store.system.service.OrderService;
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

@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {

    @Resource
    private OrderService orderService;
    @Resource
    private SubordinateDao subordinateDao;

    /**
     * create by: zhangmeng
     * description: 获取全部订单/作废订单 makeStatus=5
     * @Param: request
     * @Param: response
     * @Param: pager
     * @Param: startTime
     * @Param: endTime
     * @Param: status
     * @Param: personnelid
     * @return
     */
    @RequestMapping("/getAllOrder")
    public ModelAndView getSubOrder(HttpServletRequest request, HttpServletResponse response,
                                    Pager pager,
                                    @RequestParam(required = false,value = "startTime",defaultValue = "0")long startTime,
                                    @RequestParam(required = false,value = "subid",defaultValue = "0")long subid,
                                    @RequestParam(required = false,value = "endTime",defaultValue = "0")long endTime,
                                    @RequestParam(required = false, value = "personnelid",defaultValue = "0") long personnelid,
                                    @RequestParam(required = false, value = "status",defaultValue = "0") int status,
                                    @RequestParam(required = false, value = "uid",defaultValue = "0") long uid,
                                    @RequestParam(required = false, value = "name",defaultValue = "") String name,
                                    @RequestParam(required = false, value = "makeStatus",defaultValue = "0")  int makeStatus,
                                                   Model model) throws Exception {
        try {
            Subordinate subordinate = subordinateDao.load(subid);
            if (null == subordinate || subordinate.getPid() == 0) throw new StoreSystemException("分店ID错误");
            pager= orderService.getAll(pager,startTime,endTime,personnelid,status,uid,name, makeStatus,subid);
            return this.viewNegotiating(request, response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }

    }
    /**
     * create by: zhangmeng
     * description: 获取未完成订单 makeStatus不传。
     *              根据订单状态查询，makeStatus =1=2=3
     * create time: 2019/05/29 0029 13:54:17
     *
     * @Param: request
     * @Param: response
     * @Param: pager
     * @Param: startTime
     * @Param: subid
     * @Param: endTime
     * @Param: personnelid
     * @Param: status
     * @Param: uid
     * @Param: name
     * @Param: makeStatus
     * @Param: model
     * @return
     */
    @RequestMapping("/getIncomplete")
    public ModelAndView getIncomplete(HttpServletRequest request, HttpServletResponse response,
                                    Pager pager,
                                    @RequestParam(required = false,value = "startTime",defaultValue = "0")long startTime,
                                    @RequestParam(required = false,value = "subid",defaultValue = "0")long subid,
                                    @RequestParam(required = false,value = "endTime",defaultValue = "0")long endTime,
                                    @RequestParam(required = false, value = "personnelid",defaultValue = "0") long personnelid,
                                    @RequestParam(required = false, value = "status",defaultValue = "0") int status,
                                    @RequestParam(required = false, value = "uid",defaultValue = "0") long uid,
                                    @RequestParam(required = false, value = "name",defaultValue = "") String name,
                                      @RequestParam(required = false, value = "makeStatus",defaultValue = "0")  int makeStatus,
                                    Model model) throws Exception {

        try {
            Subordinate subordinate = subordinateDao.load(subid);
            if (null == subordinate || subordinate.getPid() == 0) throw new StoreSystemException("分店ID错误");
            pager= orderService.getAllIncomplete(pager,startTime,endTime,personnelid,status,uid,name,subid,makeStatus);
            return this.viewNegotiating(request, response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }

    }


    /**
     * create by: zhangmeng
     * description: 计算订单金额
     * create time: 2019/05/28 0028 13:54:43
     * @return
     */
    @RequestMapping("/countPrice")
    public ModelAndView countPrice(HttpServletRequest request, HttpServletResponse response,
                                  Order order,
                                   @RequestParam( value = "skuidsList")   String skuidsList,
                                   @RequestParam( value = "surchargesJson",defaultValue = "")   String surchargesJson
                                 ) throws Exception {

        try {
            List<Surcharge> billItems = Lists.newArrayList();
            List<OrderSku> orderskuids = Lists.newArrayList();
            if(StringUtils.isNotBlank(surchargesJson)) {
                billItems = JsonUtils.fromJson(surchargesJson, new TypeReference<List<Surcharge>>() {});
                order.setSurcharges(billItems);
            }if(StringUtils.isNotBlank(skuidsList)) {
                orderskuids = JsonUtils.fromJson(skuidsList, new TypeReference<List<OrderSku>>() {});
                order.setSkuids(orderskuids);
            }
            order = orderService.countPrice(order);
            return this.viewNegotiating(request, response, new ResultClient(order));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }

    }


    /**
     * create by: zhangmeng
     * description: 创建订单（临时订单）。
     * create time: 2019/05/28 0028 13:54:43
     * @return
     */
    @RequestMapping("/saveOrder")
    public ModelAndView saveOrder(HttpServletRequest request, HttpServletResponse response,
                                      Order order,
                                  @RequestParam( value = "skuidsList")   String skuidsList,
                                  @RequestParam( value = "surchargesJson",defaultValue = "")   String surchargesJson


    ) throws Exception {

        try {
            List<Surcharge> billItems = Lists.newArrayList();
            List<OrderSku> orderskuids = Lists.newArrayList();
            if(StringUtils.isNotBlank(surchargesJson)) {
                billItems = JsonUtils.fromJson(surchargesJson, new TypeReference<List<Surcharge>>() {});
                order.setSurcharges(billItems);
            }if(StringUtils.isNotBlank(skuidsList)) {
                orderskuids = JsonUtils.fromJson(skuidsList, new TypeReference<List<OrderSku>>() {});
                order.setSkuids(orderskuids);
            }
            order = orderService.saveOrder(order);
            return this.viewNegotiating(request, response, new ResultClient(order));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }

    }

    /**
     * create by: zhangmeng
     * description: 获取门店历史消费记录
     * create time: 2019/05/28 0028 13:54:43
     * @return
     */
    @RequestMapping("/getOrderBySubid")
    public ModelAndView getOrderBySubid(HttpServletRequest request, HttpServletResponse response,
                                  long subid) throws Exception {

        try {
            Subordinate subordinate = subordinateDao.load(subid);
            if (null == subordinate || subordinate.getPid() == 0) throw new StoreSystemException("分店ID错误");
           List<ClientOrder> orderList = orderService.getAllBySubid(subid);
            return this.viewNegotiating(request, response, new ResultClient(orderList));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }

    }
    /**
     * create by: zhangmeng
     * description: 获取临时订单
     * create time: 2019/05/28 0028 13:54:43
     * @return
     */
    @RequestMapping("/getTemporaryOrder")
    public ModelAndView getTemporaryOrder(HttpServletRequest request, HttpServletResponse response,
                                        long subid) throws Exception {

        try {
            List<ClientOrder> orderList = orderService.getTemporaryOrder(subid);
            return this.viewNegotiating(request, response, new ResultClient(orderList));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }

    }

    /***
    * 查询订单
    * @Param: [request, response, name, phone, orderNo, pager]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/6/10
    */
    @RequestMapping("/searchOrders")
    public ModelAndView searchOrders(HttpServletRequest request, HttpServletResponse response,
                                     long subid, String name, String phone, String orderNo,Pager pager) throws Exception {

        try {
            pager = orderService.getBackPager(pager,subid,name,phone,orderNo);
            return this.viewNegotiating(request, response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }

    }

    @RequestMapping("/loadOrder")
    public ModelAndView loadOrder(HttpServletRequest request, HttpServletResponse response,
                                  long id) throws Exception {
        try {
            ClientOrder order = orderService.loadOrder(id);
            return this.viewNegotiating(request, response, new ResultClient(order));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    ///////////////支付宝条形码支付//////////////////
    @RequestMapping("/handleAliBarcodeOrder")
    public ModelAndView handleAliBarcodeOrder(HttpServletRequest request,HttpServletResponse response,
                                              @RequestParam(name = "passportId")long passportId,
                                              String authCode, int type, String typeInfo,
                                              String title, String desc, int price)throws Exception{
        try {
            return this.viewNegotiating(request,response,new ResultClient(true,orderService.handleAliBarcodeOrder(passportId,authCode,type,typeInfo,title,desc,price)));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response, new ResultClient(false, s.getMessage()));
        }
    }

    ///////////////微信条形码支付//////////////////
    @RequestMapping("/handleWxBarcodeOrder")
    public ModelAndView handleWxBarcodeOrder(HttpServletRequest request,HttpServletResponse response,
                                             long passportId, String authCode, int type, String typeInfo,
                                             String title, String desc, int price, String ip)throws Exception{
        try {
            return this.viewNegotiating(request,response,new ResultClient(true,orderService.handleWxBarcodeOrder(request,passportId,authCode,type,typeInfo,title,desc,price,ip)));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response, new ResultClient(false, s.getMessage()));
        }
    }


}
