package com.store.system.web.controller;

import com.google.common.collect.Lists;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientSaleStatistics;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.User;
import com.store.system.service.SaleStatisticsService;
import com.store.system.util.TimeUtils;
import com.store.system.util.UserUtils;
import org.springframework.data.repository.query.Param;
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
            } else if (type == ClientSaleStatistics.type_search) {
                List<Long> days = Lists.newArrayList();
                days.add(day);
                ClientSaleStatistics client = saleStatisticsService.getDayList(days, subId);
                if (client != null) {
                    res.add(client);
                }
            }
            return this.viewNegotiating(request, response, new ResultClient(true, res));
        } catch (StoreSystemException s) {
            return this.viewNegotiating(request, response, new ResultClient(false, s.getMessage()));
        }
    }

}
