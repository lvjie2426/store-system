package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.store.system.client.ClientCategoryStatistics;
import com.store.system.client.ClientSaleStatistics;
import com.store.system.dao.SaleStatisticsDao;
import com.store.system.model.SaleCategoryStatistics;
import com.store.system.model.SaleStatistics;
import com.store.system.model.Subordinate;
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


    @Override
    public ClientSaleStatistics getDayList(List<Long> days, long subId) throws Exception {
        List<SaleStatistics> list = Lists.newArrayList();
        for(Long day:days) {
            list.addAll(saleStatisticsDao.getDayList(day, subId));
        }
        ClientSaleStatistics res = transformClient(list,new ArrayList<SaleStatistics>());
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

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        long yesterdayTime = cal.getTimeInMillis();
        SaleStatistics yesterday = new SaleStatistics();
        yesterday.setDay(TimeUtils.getDayFormTime(yesterdayTime));
        yesterday.setSubId(subId);
        yesterday.setWeek(TimeUtils.getWeekFormTime(yesterdayTime));
        yesterday.setMonth(TimeUtils.getMonthFormTime(yesterdayTime));
        yesterday = saleStatisticsDao.load(yesterday);
        ClientSaleStatistics res = transformClient(list,oldList);

        //计算销售额昨日比较今日的增长率或减少率
        if(saleStatistics!=null&&yesterday!=null) {
            if (saleStatistics.getSale() - yesterday.getSale() >= 0) {
                res.setSaleStatus(ClientSaleStatistics.saleStatus_increase);
                double subSale = ArithUtils.sub(saleStatistics.getSale(), yesterday.getSale());
                double rate = ArithUtils.div(subSale, yesterday.getSale(), 2);
                res.setSaleRate(rate);
            } else {
                res.setSaleStatus(ClientSaleStatistics.saleStatus_decrement);
                double subSale = ArithUtils.sub(yesterday.getSale(), saleStatistics.getSale());
                double rate = ArithUtils.div(subSale, yesterday.getSale(), 2);
                res.setSaleRate(rate);
            }
            //计算销售单数昨日比较今日的增长率或减少率
            if (saleStatistics.getNum() - yesterday.getNum() >= 0) {
                res.setNumStatus(ClientSaleStatistics.numStatus_increase);
                int subNum = saleStatistics.getNum() - yesterday.getNum();
                double rate = ArithUtils.div((double) subNum, (double) yesterday.getNum(), 2);
                res.setNumRate(rate);
            } else {
                res.setNumStatus(ClientSaleStatistics.saleStatus_decrement);
                int subNum = saleStatistics.getNum() - yesterday.getNum();
                double rate = ArithUtils.div((double) subNum, (double) yesterday.getNum(), 2);
                res.setNumRate(rate);
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
        ClientSaleStatistics res = transformClient(saleStatistics, oldList);
        res.setDetails(saleStatistics);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long lastWeek = TimeUtils.getWeekFormTime(cal.getTimeInMillis());
        List<SaleStatistics> lastList = saleStatisticsDao.getWeekList(lastWeek, subId);
        if (lastList.size() > 0) {
            ClientSaleStatistics last = transformClient(lastList, Lists.<SaleStatistics>newArrayList());
            //计算销售单数上周比较这周的增长率或减少率
            if (res != null && last != null) {
                if (res.getSale() - last.getSale() >= 0) {
                    res.setSaleStatus(ClientSaleStatistics.saleStatus_increase);
                    double subSale = ArithUtils.sub(res.getSale(), last.getSale());
                    double rate = ArithUtils.div(subSale, last.getSale(), 2);
                    res.setSaleRate(rate);
                } else {
                    res.setSaleStatus(ClientSaleStatistics.saleStatus_decrement);
                    double subSale = ArithUtils.sub(last.getSale(), res.getSale());
                    double rate = ArithUtils.div(subSale, last.getSale(), 2);
                    res.setSaleRate(rate);
                }
                //计算销售单数上周比较这周的增长率或减少率
                if (res.getNum() - last.getNum() >= 0) {
                    res.setNumStatus(ClientSaleStatistics.numStatus_increase);
                    int subNum = res.getNum() - last.getNum();
                    double rate = ArithUtils.div((double) subNum, (double) last.getNum(), 2);
                    res.setNumRate(rate);
                } else {
                    res.setNumStatus(ClientSaleStatistics.saleStatus_decrement);
                    int subNum = res.getNum() - last.getNum();
                    double rate = ArithUtils.div((double) subNum, (double) last.getNum(), 2);
                    res.setNumRate(rate);
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
        long oldMonth = TimeUtils.getWeekFormTime(calendar.getTimeInMillis());
        List<SaleStatistics> oldList = saleStatisticsDao.getMonthList(oldMonth,subId);
        ClientSaleStatistics res = transformClient(saleStatistics,oldList);
        res.setDetails(saleStatistics);


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long lastMonth = TimeUtils.getMonthFormTime(cal.getTimeInMillis());
        List<SaleStatistics> lastList = saleStatisticsDao.getMonthList(lastMonth,subId);
        ClientSaleStatistics last = transformClient(lastList,Lists.<SaleStatistics>newArrayList());
        //计算销售单数上月比较这月的增长率或减少率
        if(res!=null&&last!=null) {
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
                double rate = ArithUtils.div(subSale, last.getSale(), 2);
                res.setSaleRate(rate);
            }
            //计算销售单数上月比较这月的增长率或减少率
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
        return transformClient(saleStatistics,new ArrayList<SaleStatistics>());
    }


    private ClientSaleStatistics transformClient(List<SaleStatistics> statisticsList,List<SaleStatistics> oldList) throws Exception{
        ClientSaleStatistics client = new ClientSaleStatistics();
        double sale=0;
        int num=0;
        double perPrice=0;
        double profits=0;
        long subId=0;
        for(SaleStatistics statistics:statisticsList){
            subId=statistics.getSubId();
            sale = ArithUtils.add(sale,statistics.getSale());
            num += statistics.getNum();
            perPrice = ArithUtils.add(perPrice,statistics.getPerPrice());
            profits = ArithUtils.add(profits,statistics.getProfits());
        }
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
        if(oldList.size()>0){
            for(SaleStatistics statistics:oldList){
                saleOld = ArithUtils.add(saleOld,statistics.getSale());
                numOld += statistics.getNum();
                perPriceOld = ArithUtils.add(perPriceOld,statistics.getPerPrice());
                profitsOld = ArithUtils.add(profitsOld,statistics.getProfits());
            }
        }
        client.setSaleOld(saleOld);
        client.setNumOld(numOld);
        client.setPerPriceOld(perPriceOld);
        client.setProfitsOld(profitsOld);
        return client;
    }
}
