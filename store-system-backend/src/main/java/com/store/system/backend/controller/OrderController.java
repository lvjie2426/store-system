package com.store.system.backend.controller;

import com.quakoo.webframework.BaseController;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Order;
import com.store.system.model.RefundOrder;
import com.store.system.model.User;
import com.store.system.service.OrderService;
import com.store.system.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {

    @Resource
    private OrderService orderService;

    //支付宝条形码支付
    @RequestMapping("/handleAliBarcodeOrder")
    public ModelAndView handleAliBarcodeOrder(HttpServletRequest request, HttpServletResponse response,
                                              String authCode, int type, int price, long boId) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            String title = "";
            if (type == Order.type_goods) {
                title = "商品购买支付";
            } else if (type == Order.type_other) {
                title = "其他支付";
            }
            ResultClient res = orderService.handleAliBarcodeOrder(authCode, type, title,"",price,user.getSid(),boId);
            return this.viewNegotiating(request, response, res);
        } catch (StoreSystemException s) {
            return this.viewNegotiating(request, response, new ResultClient(false, s.getMessage()));
        }
    }

    ///////////////支付宝退款订单//////////////////
    @RequestMapping("/createAliRefundOrder")
    public ModelAndView createAliRefundOrder(HttpServletRequest request,HttpServletResponse response,
                                             long oid)throws Exception{
        try {
            RefundOrder refundOrder = orderService.createAliRefundOrder(oid);
            return this.viewNegotiating(request,response,new ResultClient(refundOrder));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response, new ResultClient(false, s.getMessage()));
        }
    }

    ///////////////支付宝退款//////////////////
    @RequestMapping("/handleAliRefundOrder")
    public ModelAndView handleAliRefundOrder(HttpServletRequest request,HttpServletResponse response,
                                             long roid)throws Exception{
        try {
            return this.viewNegotiating(request,response,new ResultClient(true,orderService.handleAliRefundOrder(roid)));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response, new ResultClient(false, s.getMessage()));
        }
    }

    ///////////////微信条形码支付//////////////////
    @RequestMapping("/handleWxBarcodeOrder")
    public ModelAndView handleWxBarcodeOrder(HttpServletRequest request, HttpServletResponse response,
                                             String authCode, int type, int price, long boId) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            String title = "";
            if (type == Order.type_goods) {
                title = "商品购买支付";
            } else if (type == Order.type_other) {
                title = "其他支付";
            }
            ResultClient res = orderService.handleWxBarcodeOrder(request, authCode, type, title, "", price, user.getSid(), boId);
            return this.viewNegotiating(request, response, res);
        } catch (StoreSystemException s) {
            return this.viewNegotiating(request, response, new ResultClient(false, s.getMessage()));
        }
    }

    ///////////////微信退款订单//////////////////
    @RequestMapping("/createWxRefundOrder")
    public ModelAndView createWxRefundOrder(HttpServletRequest request,HttpServletResponse response,
                                             long oid)throws Exception{
        try {
            return this.viewNegotiating(request,response,new ResultClient(true,orderService.createWxRefundOrder(oid)));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response, new ResultClient(false, s.getMessage()));
        }
    }

    ///////////////微信退款//////////////////
    @RequestMapping("/handleWxRefundOrder")
    public ModelAndView handleWxRefundOrder(HttpServletRequest request,HttpServletResponse response,
                                             long roid)throws Exception{
        try {
            return this.viewNegotiating(request,response,new ResultClient(true,orderService.handleWxRefundOrder(request,roid)));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response, new ResultClient(false, s.getMessage()));
        }
    }

    /***
    * 其他支付方式支付
    * @Param: [request, response, type, price, boId]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/7/27
    */
    @RequestMapping("/handleOtherPay")
    public ModelAndView handleOtherPay(HttpServletRequest request,HttpServletResponse response,
                                       int type, int price, long boId) throws Exception {
        try {
            orderService.handleOtherPay(type, price, boId);
            return this.viewNegotiating(request, response, new ResultClient());
        } catch (StoreSystemException s) {
            return this.viewNegotiating(request, response, new ResultClient(false, s.getMessage()));
        }
    }

}
