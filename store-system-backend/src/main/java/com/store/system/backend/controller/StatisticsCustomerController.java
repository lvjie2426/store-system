package com.store.system.backend.controller;

import com.google.common.collect.Lists;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientStatisticsCustomer;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.service.StatisticsCustomerJobService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ProjectName: store-system
 * @Author: LiHaoJie
 * @Description: 客户注册人数统计
 * @Date: 2019/6/15 16:22
 * @Version: 1.0
 */
@Controller
@RequestMapping("/statisticsCustomer")
public class StatisticsCustomerController extends BaseController{

    @Resource
    private StatisticsCustomerJobService statisticsCustomerJobService;

    //本周注册用户人数 week传第几周
    @RequestMapping("/statisticsByWeek")
    public ModelAndView statisticsByWeek(HttpServletRequest request, HttpServletResponse response,String week,long subid)throws Exception{
        try {
            return this.viewNegotiating(request,response,new ResultClient(true,statisticsCustomerJobService.getCustomerCount(subid,week,1)));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //本月  month当前时间戳
    @RequestMapping("/statisticsByMonth")
    public ModelAndView statisticsByMonth(HttpServletRequest request, HttpServletResponse response,String month,long subid)throws Exception{
        try {
            return this.viewNegotiating(request,response,new ResultClient(true,statisticsCustomerJobService.getCustomerCount(subid,month,2)));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //近半年 year当前时间戳
    @RequestMapping("/statisticsByHalfYear")
    public ModelAndView statisticsByHalfYear(HttpServletRequest request, HttpServletResponse response,String year,long subid)throws Exception{
        try {
            return this.viewNegotiating(request,response,new ResultClient(true,statisticsCustomerJobService.getCustomerCount(subid,year,3)));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //开始时间结束时间
    @RequestMapping("/statisticsByTime")
    public ModelAndView statisticsByTime(HttpServletRequest request, HttpServletResponse response,
                                                 @RequestParam(name = "startTime") long startTime,
                                                 @RequestParam(name = "endTime") long endTime,long subid)throws Exception{
        try {
            return this.viewNegotiating(request,response,new ResultClient(true,statisticsCustomerJobService.getCustomerByTime(subid,startTime,endTime)));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }

    //多店对比查询
    @RequestMapping("/statisticsBySubordinates")
    public ModelAndView statisticsBySubordinates(HttpServletRequest request,HttpServletResponse response,
                                                 @RequestParam(name = "subids") List<Long> subids,String date,int type)throws Exception{
        try {
            List<ClientStatisticsCustomer> res = Lists.newArrayList();
            for(Long subid:subids){
                ClientStatisticsCustomer customer = statisticsCustomerJobService.getCustomerCount(subid,date,type);
                if(customer!=null){ res.add(customer); }
            }
            return this.viewNegotiating(request,response,new ResultClient(true,res));
        }catch (StoreSystemException s){
            return this.viewNegotiating(request,response,new ResultClient(false,s.getMessage()));
        }
    }


}
