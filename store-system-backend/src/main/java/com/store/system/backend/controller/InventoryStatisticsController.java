package com.store.system.backend.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientInventoryStatistics;
import com.store.system.client.ClientSaleStatistics;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.InventoryStatistics;
import com.store.system.service.InventoryStatisticsService;
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
 * @ClassName InventoryStatisticsController
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/27 17:03
 * @Version 1.0
 **/
@Controller
@RequestMapping("/statisticsInventory")
public class InventoryStatisticsController extends BaseController{

    @Resource
    private InventoryStatisticsService inventoryStatisticsService;

    @RequestMapping("/saleSubordinates")
    public ModelAndView statisticsBySubordinates(HttpServletRequest request, HttpServletResponse response,
                                                 long subId, int type,
                                                 @RequestParam(name = "startTime",required = false,defaultValue = "0") long startTime,
                                                 @RequestParam(name = "endTime",required = false,defaultValue = "0") long endTime)throws Exception{
        try {
            long currentTime = System.currentTimeMillis();
            Map<Long,List<ClientInventoryStatistics>> res = Maps.newHashMap();
                if (type == ClientSaleStatistics.type_week) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(currentTime);
                    List<Long> days = TimeUtils.getPastDays(calendar.get(Calendar.DAY_OF_WEEK) - 1);
                    res = inventoryStatisticsService.getDayList(subId, days);
                } else if (type == ClientSaleStatistics.type_month) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(currentTime);
                    List<Long> days = TimeUtils.getPastDays(calendar.get(Calendar.DAY_OF_MONTH));
                    res = inventoryStatisticsService.getDayList(subId, days);
                } else if (type == ClientSaleStatistics.type_halfYear) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(currentTime);
                    List<Long> days = TimeUtils.getPastMonthDays(calendar.get(Calendar.MONTH) + 1);
                    res = inventoryStatisticsService.getDayList(subId, days);
                } else if (type == ClientSaleStatistics.type_search) {
                    res = inventoryStatisticsService.searchSale(startTime, endTime, subId);
                }

            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }


}
