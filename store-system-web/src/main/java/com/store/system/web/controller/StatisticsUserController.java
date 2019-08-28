package com.store.system.web.controller;

import com.google.common.collect.Lists;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientSaleStatistics;
import com.store.system.client.ClientStatisticsCustomer;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.service.SaleStatisticsService;
import com.store.system.service.StatisticsCustomerJobService;
import com.store.system.util.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.List;

/**
 * @ClassName StatisticsSaleController
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/19 15:09
 * @Version 1.0
 **/
@Controller
@RequestMapping("/statisticsUser")
public class StatisticsUserController extends BaseController{

    @Resource
    private StatisticsCustomerJobService statisticsCustomerJobService;

    //本周注册用户人数 week当前时间戳
    @RequestMapping("/statisticsByWeek")
    public ModelAndView statisticsByWeek(HttpServletRequest request, HttpServletResponse response,String date,long subid)throws Exception{
        try {
            return this.viewNegotiating(request,response,new ResultClient(true,statisticsCustomerJobService.getCustomerCount(subid,date,1)));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }
    //本月  month当前时间戳
    @RequestMapping("/statisticsByMonth")
    public ModelAndView statisticsByMonth(HttpServletRequest request, HttpServletResponse response,String date,long subid)throws Exception{
        try {
            return this.viewNegotiating(request,response,new ResultClient(true,statisticsCustomerJobService.getCustomerCount(subid,date,2)));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //近半年 year当前时间戳
    @RequestMapping("/statisticsByHalfYear")
    public ModelAndView statisticsByHalfYear(HttpServletRequest request, HttpServletResponse response,String date,long subid)throws Exception{
        try {
            return this.viewNegotiating(request,response,new ResultClient(true,statisticsCustomerJobService.getCustomerCount(subid,date,3)));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //多店对比查询
    @RequestMapping("/statisticsBysubids")
    public ModelAndView statisticsBySubordinates(HttpServletRequest request,HttpServletResponse response,
                                                 @RequestParam(name = "subids[]") List<Long> subids,String date,int type)throws Exception{
        try {
            List<ClientStatisticsCustomer> res = Lists.newArrayList();
            for(Long subid:subids){
                ClientStatisticsCustomer customerList = statisticsCustomerJobService.getCustomerCount(subid,date,type);
                res.add(customerList);
            }
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //多店对比时间查询
    @RequestMapping("/timeInterval")
    public ModelAndView timeInterval(HttpServletRequest request,HttpServletResponse response,
                                     @RequestParam(name = "subids[]") List<Long> subids, long startTime, long endTime)throws Exception{
        try{
            List<ClientStatisticsCustomer> res = Lists.newArrayList();
            for(Long subid:subids){
                ClientStatisticsCustomer customerList = statisticsCustomerJobService.getWebCustomerByTime(subid,startTime,endTime);
                res.add((customerList));
            }
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

}
