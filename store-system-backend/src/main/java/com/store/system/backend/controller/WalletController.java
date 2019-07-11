package com.store.system.backend.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ResultClient;
import com.store.system.exception.QuakooWalletException;
import com.store.system.model.User;
import com.store.system.model.WalletDetail;
import com.store.system.service.PayService;
import com.store.system.service.WalletService;
import com.store.system.util.Constant;
import com.store.system.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/wallet")
public class WalletController extends BaseController {

    @Resource
    private PayService payService;

    @Resource
    private WalletService walletService;

    @RequestMapping("/aliAuthParam")
    public ModelAndView aliAuthParam(HttpServletRequest request, HttpServletResponse response, final Model model) throws Exception {
        String authParam = payService.authParam(Constant.defaultPassportId);
        return super.viewNegotiating(request, response, new ResultClient(true, authParam));
    }

    @RequestMapping("/loadMoney")
    public ModelAndView loadMoney(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            double money = walletService.loadMoney(user.getId());
            return this.viewNegotiating(request, response, new ResultClient(true, money));
        } catch (QuakooWalletException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getPager")
    public ModelAndView getPager(Pager pager, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            pager = walletService.getPager(user.getId(), pager);
            return this.viewNegotiating(request,response, pager.toModelAttribute());
        } catch (QuakooWalletException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getInPager")
    public ModelAndView getInPager(Pager pager, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            pager = walletService.getPager(user.getId(), WalletDetail.type_in, pager);
            return this.viewNegotiating(request,response, pager.toModelAttribute());
        } catch (QuakooWalletException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getOutPager")
    public ModelAndView getOutPager(Pager pager, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            pager = walletService.getPager(user.getId(), WalletDetail.type_out, pager);
            return this.viewNegotiating(request,response, pager.toModelAttribute());
        } catch (QuakooWalletException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

}
