package com.store.system.web.controller;

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


    /***
     * 实时销售数据
    * @Param: [request, response, subIds, type, startTime, endTime]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/8/23
    */
    @RequestMapping("/getSaleList")
    public ModelAndView getSaleList(HttpServletRequest request, HttpServletResponse response,
                                    long subId, int type,
                                    @RequestParam(required = false, value = "day", defaultValue = "0") long day) throws Exception {
        try {
            long week = TimeUtils.getWeekFormTime(System.currentTimeMillis());
            long month = TimeUtils.getMonthFormTime(System.currentTimeMillis());
            List<ClientSaleStatistics> res = Lists.newArrayList();
            if (type == ClientSaleStatistics.type_today) {
                long dayTime = System.currentTimeMillis();
                ClientSaleStatistics statisticsWeek = saleStatisticsService.getDay(dayTime, subId);
                if (statisticsWeek != null) {
                    res.add(statisticsWeek);
                }
            } else if (type == ClientSaleStatistics.type_yesterday) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);
                long yesterdayTime = cal.getTimeInMillis();
                ClientSaleStatistics statisticsWeek = saleStatisticsService.getDay(yesterdayTime, subId);
                if (statisticsWeek != null) {
                    res.add(statisticsWeek);
                }
            } else if (type == ClientSaleStatistics.type_week) {
                ClientSaleStatistics statisticsWeek = saleStatisticsService.getWeek(week, subId);
                if (statisticsWeek != null) {
                    res.add(statisticsWeek);
                }
            } else if (type == ClientSaleStatistics.type_month) {
                ClientSaleStatistics statisticsMonth = saleStatisticsService.getMonth(month, subId);
                if (statisticsMonth != null) {
                    res.add(statisticsMonth);
                }
            } else if (type == ClientSaleStatistics.type_day) {
                ClientSaleStatistics client = saleStatisticsService.getDate(day, subId);
                if (client != null) {
                    res.add(client);
                }
            }
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException s) {
            return this.viewNegotiating(request, response, new ResultClient(false, s.getMessage()));
        }
    }

    /***
    * 多店对比查询销售额
    * @Param: [request, response, subIds, type, startTime, endTime]
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: LaoMa
    * @Date: 2019/8/26
    */
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
}
