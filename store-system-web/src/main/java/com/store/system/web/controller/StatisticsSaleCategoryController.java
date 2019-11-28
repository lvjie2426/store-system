package com.store.system.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientCategoryStatistics;
import com.store.system.client.ClientOrderSku;
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


    @RequestMapping("/saleSubordinatesSum")
    public ModelAndView saleSubordinatesSum(HttpServletRequest request, HttpServletResponse response,
                                                 @RequestParam(name = "subIds[]") List<Long> subIds, int type,
                                                 @RequestParam(name = "startTime",required = false,defaultValue = "0") long startTime,
                                                 @RequestParam(name = "endTime",required = false,defaultValue = "0") long endTime)throws Exception{
        try {
            List<ClientCategoryStatistics> res = Lists.newArrayList();
            for(Long subId:subIds) {
                ClientCategoryStatistics statistics = new ClientCategoryStatistics();
                if (type == ClientSaleStatistics.type_week) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    List<Long> days = TimeUtils.getPastDays(calendar.get(Calendar.DAY_OF_WEEK) - 1);
                    statistics = saleCategoryStatisticsService.getDayListSum(subId, days);
                } else if (type == ClientSaleStatistics.type_month) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    List<Long> days = TimeUtils.getPastDays(calendar.get(Calendar.DAY_OF_MONTH));
                    statistics = saleCategoryStatisticsService.getDayListSum(subId, days);
                } else if (type == ClientSaleStatistics.type_halfYear) {
                    List<Long> days = TimeUtils.getPastMonthDays(6);
                    statistics = saleCategoryStatisticsService.getDayListSum(subId, days);
                } else if (type == ClientSaleStatistics.type_search) {
                    statistics  = saleCategoryStatisticsService.searchSaleSum(startTime, endTime, subId);
                }
                res.add(statistics);
            }
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }


    @RequestMapping("/detail")
    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(name = "subIds[]") List<Long> subIds,
                               @RequestParam(name = "cid", required = false, defaultValue = "0") long cid,
                               @RequestParam(name = "startDay", required = false, defaultValue = "0") long startDay,
                               @RequestParam(name = "endDay", required = false, defaultValue = "0") long endDay) throws Exception {
        try {
            List<ClientOrderSku> res = saleCategoryStatisticsService.getDetail(startDay, endDay, subIds, cid);
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //客单价统计
    @RequestMapping("/saleSubordinatesByCid")
    public ModelAndView saleSubordinatesByCid(HttpServletRequest request, HttpServletResponse response,
                                                 @RequestParam(name = "subIds[]") List<Long> subIds, int type, long cid,
                                                 @RequestParam(name = "startTime",required = false,defaultValue = "0") long startTime,
                                                 @RequestParam(name = "endTime",required = false,defaultValue = "0") long endTime)throws Exception{
        try {
            ClientCategoryStatistics res = new ClientCategoryStatistics();
            for(Long subId:subIds) {
                if (type == ClientSaleStatistics.type_week) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    List<Long> days = TimeUtils.getPastDays(calendar.get(Calendar.DAY_OF_WEEK) - 1);
                    res = saleCategoryStatisticsService.getDayList(subId, cid, days);
                } else if (type == ClientSaleStatistics.type_month) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    List<Long> days = TimeUtils.getPastDays(calendar.get(Calendar.DAY_OF_MONTH));
                    res = saleCategoryStatisticsService.getDayList(subId, cid, days);
                } else if (type == ClientSaleStatistics.type_halfYear) {
                    List<Long> days = TimeUtils.getPastMonthDays(6);
                    res = saleCategoryStatisticsService.getDayList(subId, cid, days);
                } else if (type == ClientSaleStatistics.type_search) {
                    res = saleCategoryStatisticsService.searchSale(startTime, endTime, subId, cid);
                }
            }
            return this.viewNegotiating(request,response,new ResultClient(true,new ResultClient(res)));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }




}
