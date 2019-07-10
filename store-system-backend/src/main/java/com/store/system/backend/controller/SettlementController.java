package com.store.system.backend.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientFinanceLogDetail;
import com.store.system.client.ClientSettlement;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Settlement;
import com.store.system.service.FinanceLogService;
import com.store.system.service.SettlementService;
import com.store.system.util.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName SettlementController
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/7/8 18:21
 * @Version 1.0
 **/
@Controller
@RequestMapping("/settlement")
public class SettlementController extends BaseController{

    @Resource
    private SettlementService settlementService;
    @Resource
    private FinanceLogService financeLogService;

    /***
    * 进行结算
    * @Param: [request, response, model, settlement]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/7/8
    */
    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response, Model model,
                            Settlement settlement) throws Exception {
        try {
            settlement = settlementService.insert(settlement);
            return this.viewNegotiating(request, response, new ResultClient(true, settlement));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /*** 
    * 获取最新的一次结算
    * @Param: [request, response, model, subId]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/7/8
    */ 
    @RequestMapping("/loadClient")
    public ModelAndView loadClient(HttpServletRequest request, HttpServletResponse response, Model model,
                                   long subId) throws Exception {
        try {
            ClientSettlement res = settlementService.loadClient(subId);
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
    * 获取结算记录
    * @Param: [request, response, model, pager, subId]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/7/8
    */
    @RequestMapping("/getPagerLog")
    public ModelAndView getPagerLog(HttpServletRequest request, HttpServletResponse response, Model model,
                                    Pager pager, long subId) throws Exception {
        try {
            pager = settlementService.getPager(pager,subId);
            return this.viewNegotiating(request, response, new PagerResult<>(pager));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

    /***
     * 获取今日账户流水
     * @Param: [request, response, model, pager, subId]
     * @return: org.springframework.web.servlet.ModelAndView
     * @Author: LaoMa
     * @Date: 2019/7/8
     */
    @RequestMapping("/getFinanceLogs")
    public ModelAndView getFinanceLogs(HttpServletRequest request, HttpServletResponse response, Model model,
                                   long subId) throws Exception {
        try {
            long day = TimeUtils.getDayFormTime(System.currentTimeMillis());
            ClientFinanceLogDetail res = financeLogService.getDay(subId,day);
            return this.viewNegotiating(request, response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request, response, new ResultClient(false, e.getMessage()));
        }
    }

}
