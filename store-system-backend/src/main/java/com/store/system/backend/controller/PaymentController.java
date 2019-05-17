package com.store.system.backend.controller;

import com.google.common.collect.Lists;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientPayment;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName PaymentController
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/5/17 18:47
 * @Version 1.0
 **/
@Controller
@RequestMapping("/payment")
public class PaymentController extends BaseController {

    @Resource
    private PaymentService paymentService;

    /***
    * 获取已使用的门店列表
    * @Param: [psid, payType, request, response, model]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/5/17
    */
    @RequestMapping("/getUsedList")
    public ModelAndView getAllList(@RequestParam(value = "psid") long psid, int payType,
                                   HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            List<ClientPayment> payments = paymentService.getUsedList(psid,payType);
            List<String> res = Lists.newArrayList();
            for(ClientPayment client:payments){
                res.add(client.getSubName());
            }
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }
}
