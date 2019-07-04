package com.store.system.backend.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.model.PayPassport;
import com.store.system.client.ResultClient;
import com.store.system.service.PayService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/pay")
@Controller
public class PayPassportController extends BaseController {


    @Resource
    private PayService payService;

    //获取全部数据
    @RequestMapping("/getAllList")
    public ModelAndView getAllList(HttpServletRequest request, HttpServletResponse response, Pager pager) throws Exception{
        return this.viewNegotiating(request,response,new ResultClient(payService.getAllList()));
    }

    @RequestMapping("/getAllListBySub")
    public ModelAndView getAllListBySub(HttpServletRequest request, HttpServletResponse response, long subId) throws Exception{
        return this.viewNegotiating(request,response,new ResultClient(payService.getAllList(subId)));
    }

    //新增数据
    @RequestMapping("/addPay")
    public ModelAndView addPay(HttpServletRequest request, HttpServletResponse response,PayPassport payPassport) throws Exception{
        PayPassport result = payService.insertPayPassport(payPassport);
        return this.viewNegotiating(request,response,new ResultClient(result));
    }

    //删除数据
    @RequestMapping("/del")
    public ModelAndView del(HttpServletRequest request, HttpServletResponse response,long id) throws Exception {
        return this.viewNegotiating(request,response,new ResultClient(payService.deletePayPassport(id)));
    }

    //编辑数据
    @RequestMapping("/updatePay")
    public ModelAndView updatePay(HttpServletRequest request, HttpServletResponse response,PayPassport payPassport) throws Exception {
    return this.viewNegotiating(request,response,new ResultClient(payService.updatePayPassport(payPassport)));
    }

    //关闭或启用
    @RequestMapping("/updateStatus")
    public ModelAndView updateStatus(HttpServletRequest request, HttpServletResponse response,
                            long id, int status) throws Exception {
        boolean res = payService.updateStatus(id,status);
        return this.viewNegotiating(request,response,new ResultClient(res));
    }
}
