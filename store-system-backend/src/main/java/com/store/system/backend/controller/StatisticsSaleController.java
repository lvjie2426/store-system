package com.store.system.backend.controller;

import com.google.common.collect.Lists;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientSaleStatistics;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.service.SaleStatisticsService;
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

    //今日销售额统计
    @RequestMapping("/saleToday")
    public ModelAndView saleToday(HttpServletRequest request, HttpServletResponse response, long subId)throws Exception{
        try {
            long dayTime = System.currentTimeMillis();
            ClientSaleStatistics res = saleStatisticsService.getDay(dayTime,Lists.<Long>newArrayList(subId));
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
            ClientSaleStatistics res = saleStatisticsService.getDay(yesterdayTime,Lists.<Long>newArrayList(subId));
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
            ClientSaleStatistics res = saleStatisticsService.getWeek(week,Lists.<Long>newArrayList(subId));
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
            ClientSaleStatistics res = saleStatisticsService.getMonth(month,Lists.<Long>newArrayList(subId));
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //近半年
    @RequestMapping("/saleHalfYear")
    public ModelAndView saleHalfYear(HttpServletRequest request, HttpServletResponse response, long subId)throws Exception{
        try {
            List<Long> days = TimeUtils.getPastMonthDays(6);
            ClientSaleStatistics res = saleStatisticsService.getDayList(days,Lists.<Long>newArrayList(subId));
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
            ClientSaleStatistics res = saleStatisticsService.searchSale(startTime,endTime,Lists.<Long>newArrayList(subId));
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
                    ClientSaleStatistics statisticsWeek = saleStatisticsService.getWeek(week,Lists.<Long>newArrayList(subId));
                    if (statisticsWeek != null) {
                        res.add(statisticsWeek);
                    }
                }else if(type==ClientSaleStatistics.type_month){
                    ClientSaleStatistics statisticsMonth = saleStatisticsService.getMonth(month, Lists.<Long>newArrayList(subId));
                    if (statisticsMonth != null) {
                        res.add(statisticsMonth);
                    }
                }else if(type==ClientSaleStatistics.type_halfYear){
                    List<Long> days = TimeUtils.getPastMonthDays(6);
                    ClientSaleStatistics statisticsYear = saleStatisticsService.getDayList(days,Lists.<Long>newArrayList(subId));
                    if (statisticsYear != null) {
                        res.add(statisticsYear);
                    }
                }else if(type==ClientSaleStatistics.type_search){
                    ClientSaleStatistics statistics = saleStatisticsService.searchSale(startTime,endTime,Lists.<Long>newArrayList(subId));
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

}
