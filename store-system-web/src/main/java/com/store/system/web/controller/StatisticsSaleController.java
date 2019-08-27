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
@RequestMapping("/statisticsSale")
public class StatisticsSaleController extends BaseController{

    @Resource
    private SaleStatisticsService saleStatisticsService;

    @Resource
    private StatisticsCustomerJobService statisticsCustomerJobService;

    //今日销售额统计
    @RequestMapping("/saleToday")
    public ModelAndView saleToday(HttpServletRequest request, HttpServletResponse response, long subId)throws Exception{
        try {
            long dayTime = System.currentTimeMillis();
            ClientSaleStatistics res = saleStatisticsService.getDay(dayTime,subId);
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //昨日销售额统计
    @RequestMapping("/saleYesterday")
    public ModelAndView saleYesterday(HttpServletRequest request, HttpServletResponse response, long subId)throws Exception{
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            long yesterdayTime = cal.getTimeInMillis();
            ClientSaleStatistics res = saleStatisticsService.getDay(yesterdayTime,subId);
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //本周销售额统计
    @RequestMapping("/saleWeek")
    public ModelAndView saleWeek(HttpServletRequest request, HttpServletResponse response, long subId)throws Exception{
        try {
            long week = TimeUtils.getWeekFormTime(System.currentTimeMillis());
            ClientSaleStatistics res = saleStatisticsService.getWeek(week,subId);
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //本月销售额统计
    @RequestMapping("/saleMonth")
    public ModelAndView saleMonth(HttpServletRequest request, HttpServletResponse response, long subId)throws Exception{
        try {
            long month = TimeUtils.getMonthFormTime(System.currentTimeMillis());
            ClientSaleStatistics res = saleStatisticsService.getMonth(month,subId);
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //近半年
    @RequestMapping("/saleHalfYear")
    public ModelAndView saleHalfYear(HttpServletRequest request, HttpServletResponse response, long subId)throws Exception{
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            List<Long> days = TimeUtils.getPastMonthDays(calendar.get(Calendar.MONTH)+1);
            ClientSaleStatistics res = saleStatisticsService.getDayList(days,subId);
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //查询销售额统计
    @RequestMapping("/searchSale")
    public ModelAndView searchSale(HttpServletRequest request, HttpServletResponse response,
                                         @RequestParam(name = "startTime") long startTime,
                                         @RequestParam(name = "endTime") long endTime,long subId)throws Exception{
        try {
            ClientSaleStatistics res = saleStatisticsService.searchSale(startTime,endTime,subId);
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //多店对比查询销售额
    @RequestMapping("/saleSubordinates")
    public ModelAndView statisticsBySubordinates(HttpServletRequest request, HttpServletResponse response,
                                                 @RequestParam(name = "subIds[]") List<Long> subIds, int type,
                                                 @RequestParam(name = "startTime",required = false,defaultValue = "0") long startTime,
                                                 @RequestParam(name = "endTime",required = false,defaultValue = "0") long endTime)throws Exception{
        try {
            long week = TimeUtils.getWeekFormTime(System.currentTimeMillis());
            long month = TimeUtils.getMonthFormTime(System.currentTimeMillis());
            List<ClientSaleStatistics> res = Lists.newArrayList();
            for(Long subId:subIds){
                if(type==ClientSaleStatistics.type_week) {
                    ClientSaleStatistics statisticsWeek = saleStatisticsService.getWeek(week,subId);
                    if (statisticsWeek != null) {
                        res.add(statisticsWeek);
                    }
                }else if(type==ClientSaleStatistics.type_month){
                    ClientSaleStatistics statisticsMonth = saleStatisticsService.getMonth(month, subId);
                    if (statisticsMonth != null) {
                        res.add(statisticsMonth);
                    }
                }else if(type==ClientSaleStatistics.type_halfYear){
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    List<Long> days = TimeUtils.getPastMonthDays(calendar.get(Calendar.MONTH)+1);
                    ClientSaleStatistics statisticsYear = saleStatisticsService.getDayList(days,subId);
                    if (statisticsYear != null) {
                        res.add(statisticsYear);
                    }
                }else if(type==ClientSaleStatistics.type_search){
                    ClientSaleStatistics statistics = saleStatisticsService.searchSale(startTime,endTime,subId);
                    if (statistics != null) {
                        res.add(statistics);
                    }
                }
            }
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }


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
                ClientStatisticsCustomer customerList = statisticsCustomerJobService.getCustomerByTime(subid,startTime,endTime);
                res.add((customerList));
            }
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

}
