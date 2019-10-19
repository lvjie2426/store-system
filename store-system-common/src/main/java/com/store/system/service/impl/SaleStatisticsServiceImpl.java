package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.store.system.client.ClientSaleStatistics;
import com.store.system.dao.SaleStatisticsDao;
import com.store.system.model.BusinessOrder;
import com.store.system.model.SaleStatistics;
import com.store.system.model.Subordinate;
import com.store.system.service.BusinessOrderService;
import com.store.system.service.SaleStatisticsService;
import com.store.system.service.SubordinateService;
import com.store.system.util.ArithUtils;
import com.store.system.util.TimeUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName SaleStatisticsServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/15 11:06
 * @Version 1.0
 **/
@Service
public class SaleStatisticsServiceImpl implements SaleStatisticsService{

    @Resource
    private SaleStatisticsDao saleStatisticsDao;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private SubordinateService subordinateService;
    @Resource
    private BusinessOrderService businessOrderService;


    @Override
    public ClientSaleStatistics getDayList(List<Long> days, long subId) throws Exception {
        List<SaleStatistics> list = Lists.newArrayList();
        for(Long day:days) {
            list.addAll(saleStatisticsDao.getDayList(day, subId));
        }
        ClientSaleStatistics res = transformClient(list,new ArrayList<SaleStatistics>(),subId);
        res.setDetails(list);
        return res;
    }


    @Override
    public ClientSaleStatistics getDay(long dayTime, long subId) throws Exception {
        List<SaleStatistics> list=Lists.newArrayList();
        List<SaleStatistics> oldList=Lists.newArrayList();
        SaleStatistics saleStatistics = new SaleStatistics();
        saleStatistics.setDay(TimeUtils.getDayFormTime(dayTime));
        saleStatistics.setSubId(subId);
        saleStatistics.setWeek(TimeUtils.getWeekFormTime(dayTime));
        saleStatistics.setMonth(TimeUtils.getMonthFormTime(dayTime));
        saleStatistics = saleStatisticsDao.load(saleStatistics);
        if(saleStatistics!=null){
            list.add(saleStatistics);
        }

        //去年同期数据
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
        long oldTime = calendar.getTimeInMillis();
        SaleStatistics old = new SaleStatistics();
        old.setDay(TimeUtils.getDayFormTime(oldTime));
        old.setSubId(subId);
        old.setWeek(TimeUtils.getWeekFormTime(oldTime));
        old.setMonth(TimeUtils.getMonthFormTime(oldTime));
        old = saleStatisticsDao.load(old);
        if(old!=null){
            oldList.add(old);
        }

        ClientSaleStatistics res = transformClient(list,oldList,subId);

        if (saleStatistics != null && old != null) {
            //计算销售额去年今日比较今日的增长率或减少率
            if (saleStatistics.getSale() - old.getSale() >= 0) {
                res.setSaleStatus(ClientSaleStatistics.saleStatus_increase);
                double subSale = ArithUtils.sub(saleStatistics.getSale(), old.getSale());
                if(old.getSale()>0) {
                    double rate = ArithUtils.div(subSale, old.getSale(), 2);
                    res.setSaleRate(rate);
                }
            } else {
                res.setSaleStatus(ClientSaleStatistics.saleStatus_decrement);
                double subSale = ArithUtils.sub(old.getSale(), saleStatistics.getSale());
                if(old.getSale()>0) {
                    double rate = ArithUtils.div(subSale, old.getSale(), 2);
                    res.setSaleRate(rate);
                }
            }
            //计算销售单数去年今日比较今日的增长率或减少率
            if (saleStatistics.getNum() - old.getNum() >= 0) {
                res.setNumStatus(ClientSaleStatistics.numStatus_increase);
                int subNum = saleStatistics.getNum() - old.getNum();
                if(old.getNum()>0) {
                    double rate = ArithUtils.div((double) subNum, (double) old.getNum(), 2);
                    res.setNumRate(rate);
                }
            } else {
                res.setNumStatus(ClientSaleStatistics.saleStatus_decrement);
                int subNum = saleStatistics.getNum() - old.getNum();
                if(old.getNum()>0) {
                    double rate = ArithUtils.div((double) subNum, (double) old.getNum(), 2);
                    res.setNumRate(rate);
                }
            }
        }
        return res;
    }

    @Override
    public ClientSaleStatistics getWeek(long week, long subId) throws Exception {
        List<SaleStatistics> saleStatistics = saleStatisticsDao.getWeekList(week, subId);

        //去年同期数据
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
        long oldWeek = TimeUtils.getWeekFormTime(calendar.getTimeInMillis());
        List<SaleStatistics> oldList = saleStatisticsDao.getWeekList(oldWeek, subId);
        ClientSaleStatistics res = transformClient(saleStatistics, oldList, subId);
        res.setDetails(saleStatistics);

        if (oldList.size() > 0) {
            ClientSaleStatistics last = transformClient(oldList, Lists.<SaleStatistics>newArrayList(), subId);
            if (last != null) {
                //计算销售额去年本周比较本周的增长率或减少率
                if (res.getSale() - last.getSale() >= 0) {
                    res.setSaleStatus(ClientSaleStatistics.saleStatus_increase);
                    double subSale = ArithUtils.sub(res.getSale(), last.getSale());
                    if(last.getSale()>0) {
                        double rate = ArithUtils.div(subSale, last.getSale(), 2);
                        res.setSaleRate(rate);
                    }
                } else {
                    res.setSaleStatus(ClientSaleStatistics.saleStatus_decrement);
                    double subSale = ArithUtils.sub(last.getSale(), res.getSale());
                    if(res.getSale()>0) {
                        double rate = ArithUtils.div(subSale, last.getSale(), 2);
                        res.setSaleRate(rate);
                    }
                }
                //计算销售单数去年本周比较本周的增长率或减少率
                if (res.getNum() - last.getNum() >= 0) {
                    res.setNumStatus(ClientSaleStatistics.numStatus_increase);
                    int subNum = res.getNum() - last.getNum();
                    if(last.getNum()>0) {
                        double rate = ArithUtils.div((double) subNum, (double) last.getNum(), 2);
                        res.setNumRate(rate);
                    }
                } else {
                    res.setNumStatus(ClientSaleStatistics.saleStatus_decrement);
                    int subNum = res.getNum() - last.getNum();
                    if(last.getNum()>0) {
                        double rate = ArithUtils.div((double) subNum, (double) last.getNum(), 2);
                        res.setNumRate(rate);
                    }
                }
            }
        }
        return res;
    }

    @Override
    public ClientSaleStatistics getMonth(long month, long subId) throws Exception {
        List<SaleStatistics> saleStatistics = saleStatisticsDao.getMonthList(month,subId);

        //去年同期数据
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
        long oldMonth = TimeUtils.getMonthFormTime(calendar.getTimeInMillis());
        List<SaleStatistics> oldList = saleStatisticsDao.getMonthList(oldMonth,subId);
        ClientSaleStatistics res = transformClient(saleStatistics,oldList,subId);
        res.setDetails(saleStatistics);

        if(oldList.size()>0) {
            ClientSaleStatistics last = transformClient(oldList, Lists.<SaleStatistics>newArrayList(), subId);
            if (last != null) {
                //计算销售额去年本月比较本月的增长率或减少率
                if (res.getSale() - last.getSale() >= 0) {
                    res.setSaleStatus(ClientSaleStatistics.saleStatus_increase);
                    double subSale = ArithUtils.sub(res.getSale(), last.getSale());
                    if (last.getSale() > 0) {
                        double rate = ArithUtils.div(subSale, last.getSale(), 2);
                        res.setSaleRate(rate);
                    }
                } else {
                    res.setSaleStatus(ClientSaleStatistics.saleStatus_decrement);
                    double subSale = ArithUtils.sub(last.getSale(), res.getSale());
                    double rate = ArithUtils.div(subSale, last.getSale(), 2);
                    res.setSaleRate(rate);
                }
                //计算销售单数去年本月比较本月的增长率或减少率
                if (res.getNum() - last.getNum() >= 0) {
                    res.setNumStatus(ClientSaleStatistics.numStatus_increase);
                    int subNum = res.getNum() - last.getNum();
                    if (last.getNum() > 0) {
                        double rate = ArithUtils.div((double) subNum, (double) last.getNum(), 2);
                        res.setNumRate(rate);
                    }
                } else {
                    res.setNumStatus(ClientSaleStatistics.saleStatus_decrement);
                    int subNum = res.getNum() - last.getNum();
                    if (last.getNum() > 0) {
                        double rate = ArithUtils.div((double) subNum, (double) last.getNum(), 2);
                        res.setNumRate(rate);
                    }
                }
            }
        }
        return res;
    }

    @Override
    public ClientSaleStatistics searchSale(long startTime, long endTime, long subId) throws Exception {
        String sql = "SELECT  *  FROM `sale_statistics` where  1=1 ";
        if (subId > 0) {
            sql = sql + " and `subId` = " + subId;
        }
        if (startTime > 0) {
            sql = sql + " and `ctime` > " + startTime;
        }
        if (endTime > 0) {
            sql = sql + " and `ctime` < " + endTime;
        }

        sql = sql + " order  by ctime desc";
        List<SaleStatistics> saleStatistics = jdbcTemplate.query(sql, new HyperspaceBeanPropertyRowMapper(SaleStatistics.class));
        return transformClient(saleStatistics,new ArrayList<SaleStatistics>(),subId);
    }

    @Override
    public ClientSaleStatistics getDate(long day, long subId) throws Exception {
        List<SaleStatistics> list=Lists.newArrayList();
        List<SaleStatistics> oldList=Lists.newArrayList();
        SaleStatistics saleStatistics = new SaleStatistics();
        saleStatistics.setDay(day);
        saleStatistics.setSubId(subId);
        saleStatistics.setWeek(TimeUtils.getWeekFormTime(TimeUtils.getTimeFormDay(day)));
        saleStatistics.setMonth(TimeUtils.getMonthFormTime(TimeUtils.getTimeFormDay(day)));
        saleStatistics = saleStatisticsDao.load(saleStatistics);
        if(saleStatistics!=null){
            list.add(saleStatistics);
        }

        //去年同期数据
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, -1);
        calendar.setTime(new Date(TimeUtils.getTimeFormDay(day)));
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
        long oldTime = calendar.getTimeInMillis();
        SaleStatistics old = new SaleStatistics();
        old.setDay(TimeUtils.getDayFormTime(oldTime));
        old.setSubId(subId);
        old.setWeek(TimeUtils.getWeekFormTime(oldTime));
        old.setMonth(TimeUtils.getMonthFormTime(oldTime));
        old = saleStatisticsDao.load(old);
        if(old!=null){
            oldList.add(old);
        }

        ClientSaleStatistics res = transformClient(list,oldList,subId);

        if (saleStatistics != null && old != null) {
            //计算销售额去年的日期比较选择的日期的增长率或减少率
            if (saleStatistics.getSale() - old.getSale() >= 0) {
                res.setSaleStatus(ClientSaleStatistics.saleStatus_increase);
                double subSale = ArithUtils.sub(saleStatistics.getSale(), old.getSale());
                if(old.getSale()>0) {
                    double rate = ArithUtils.div(subSale, old.getSale(), 2);
                    res.setSaleRate(rate);
                }
            } else {
                res.setSaleStatus(ClientSaleStatistics.saleStatus_decrement);
                double subSale = ArithUtils.sub(old.getSale(), saleStatistics.getSale());
                if(old.getSale()>0) {
                    double rate = ArithUtils.div(subSale, old.getSale(), 2);
                    res.setSaleRate(rate);
                }
            }
            //计算销售单数去年的日期比较选择的日期的增长率或减少率
            if (saleStatistics.getNum() - old.getNum() >= 0) {
                res.setNumStatus(ClientSaleStatistics.numStatus_increase);
                int subNum = saleStatistics.getNum() - old.getNum();
                if(old.getNum()>0) {
                    double rate = ArithUtils.div((double) subNum, (double) old.getNum(), 2);
                    res.setNumRate(rate);
                }
            } else {
                res.setNumStatus(ClientSaleStatistics.saleStatus_decrement);
                int subNum = saleStatistics.getNum() - old.getNum();
                if(old.getNum()>0) {
                    double rate = ArithUtils.div((double) subNum, (double) old.getNum(), 2);
                    res.setNumRate(rate);
                }
            }
        }
        return res;
    }

    private ClientSaleStatistics transformClient(List<SaleStatistics> statisticsList,List<SaleStatistics> oldList,long subId) throws Exception{
        ClientSaleStatistics client = new ClientSaleStatistics();
        double sale=0;
        int num=0;
        double perPrice=0;
        double profits=0;
        int customer = 0;
        for(SaleStatistics statistics:statisticsList){
            sale = ArithUtils.add(sale,statistics.getSale());
            num += statistics.getNum();
            perPrice = ArithUtils.add(perPrice,statistics.getPerPrice());
            profits = ArithUtils.add(profits,statistics.getProfits());
        }

        long currentTime = System.currentTimeMillis();
        List<BusinessOrder> businessOrders = businessOrderService.getList(subId, BusinessOrder.status_pay,BusinessOrder.makeStatus_qu_yes,currentTime);

        if(businessOrders.size()>0) {
            Map<Long, List<BusinessOrder>> map = Maps.newHashMap();
            for (BusinessOrder one : businessOrders) {
                List<BusinessOrder> list = map.get(one.getUid());
                if(null == list) {
                    list = Lists.newArrayList();
                    map.put(one.getUid(), list);
                }
                list.add(one);
            }
            for (Map.Entry<Long, List<BusinessOrder>> entry : map.entrySet()) {
                if (entry.getValue().size() >= 2) {
                    customer++;
                }
            }
        }
        client.setCustomer(customer);
        client.setSale(sale);
        client.setNum(num);
        client.setPerPrice(perPrice);
        client.setProfits(profits);
        Subordinate subordinate = subordinateService.load(subId);
        if(subordinate!=null) client.setSubName(subordinate.getName());

        double saleOld=0;
        int numOld=0;
        double perPriceOld=0;
        double profitsOld=0;
        int customerOld = 0;
        if(oldList.size()>0){
            for(SaleStatistics statistics:oldList){
                saleOld = ArithUtils.add(saleOld,statistics.getSale());
                numOld += statistics.getNum();
                perPriceOld = ArithUtils.add(perPriceOld,statistics.getPerPrice());
                profitsOld = ArithUtils.add(profitsOld,statistics.getProfits());
            }
        }

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
        long oldTime = calendar.getTimeInMillis();
        List<BusinessOrder> ordersOld = businessOrderService.getList(subId, BusinessOrder.status_pay,BusinessOrder.makeStatus_qu_yes,oldTime);
        List<BusinessOrder> oldlist = null;
        if(ordersOld.size()>0) {
            Map<Long, List<BusinessOrder>> map = Maps.newHashMap();
            for (BusinessOrder one : ordersOld) {
                if(null == oldlist) {
                    oldlist = Lists.newArrayList();
                }
                oldlist.add(one);
                map.put(one.getUid(), oldlist);
            }
            for (Map.Entry<Long, List<BusinessOrder>> entry : map.entrySet()) {
                if (entry.getValue().size() >= 2) {
                    customerOld++;
                }
            }
        }

        client.setSaleOld(saleOld);
        client.setNumOld(numOld);
        client.setPerPriceOld(perPriceOld);
        client.setProfitsOld(profitsOld);
        client.setCustomerOld(customerOld);
        return client;
    }
}
