package com.store.system.backend.controller;

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

    //本周
    @RequestMapping("/saleWeek")
    public ModelAndView saleWeek(HttpServletRequest request, HttpServletResponse response, long subId)throws Exception{
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            List<Long> days = TimeUtils.getPastDays(calendar.get(Calendar.DAY_OF_WEEK)-1);
            Map<Long,ClientCategoryStatistics> res = saleCategoryStatisticsService.getDayList(subId,days);
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //本月
    @RequestMapping("/saleMonth")
    public ModelAndView saleMonth(HttpServletRequest request, HttpServletResponse response, long subId)throws Exception{
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            List<Long> days = TimeUtils.getPastDays(calendar.get(Calendar.DAY_OF_MONTH));
            Map<Long,ClientCategoryStatistics> res = saleCategoryStatisticsService.getDayList(subId,days);
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
            Map<Long,ClientCategoryStatistics> res = saleCategoryStatisticsService.getDayList(subId,days);
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //查询
    @RequestMapping("/searchSale")
    public ModelAndView searchSale(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(name = "startTime") long startTime,
                                   @RequestParam(name = "endTime") long endTime,long subId)throws Exception{
        try {
            Map<Long,ClientCategoryStatistics> res = saleCategoryStatisticsService.searchSale(startTime,endTime,subId);
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }
}
