package com.store.system.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientCategoryStatistics;
import com.store.system.client.ClientSaleStatistics;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.service.SaleCategoryStatisticsService;
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
import java.util.Map;

/**
 * @ClassName StatisticsSaleCategoryController
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/21 15:30
 * @Version 1.0
 **/
@Controller
@RequestMapping("/statisticsSaleCategory")
public class StatisticsSaleCategoryController extends BaseController{

    @Resource
    private SaleCategoryStatisticsService saleCategoryStatisticsService;

    @RequestMapping("/saleSubordinates")
    public ModelAndView statisticsBySubordinates(HttpServletRequest request, HttpServletResponse response,
                                                 @RequestParam(name = "subIds[]") List<Long> subIds, int type,
                                                 @RequestParam(name = "startTime",required = false,defaultValue = "0") long startTime,
                                                 @RequestParam(name = "endTime",required = false,defaultValue = "0") long endTime)throws Exception{
        try {
            Map<Long,List<ClientCategoryStatistics>> map = Maps.newHashMap();
            List<Map<Long,List<ClientCategoryStatistics>>> res = Lists.newArrayList();
            for(Long subId:subIds) {
                if (type == ClientSaleStatistics.type_week) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    List<Long> days = TimeUtils.getPastDays(calendar.get(Calendar.DAY_OF_WEEK) - 1);
                    map = saleCategoryStatisticsService.getDayList(subId, days);
                } else if (type == ClientSaleStatistics.type_month) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    List<Long> days = TimeUtils.getPastDays(calendar.get(Calendar.DAY_OF_MONTH));
                    map = saleCategoryStatisticsService.getDayList(subId, days);
                } else if (type == ClientSaleStatistics.type_halfYear) {
                    List<Long> days = TimeUtils.getPastMonthDays(6);
                    map = saleCategoryStatisticsService.getDayList(subId, days);
                } else if (type == ClientSaleStatistics.type_search) {
                    map = saleCategoryStatisticsService.searchSale(startTime, endTime, subId);
                }
                res.add(map);
            }
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }
}
