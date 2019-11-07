package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.store.system.client.ClientRankingFirst;
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
    public ClientSaleStatistics getDayList(List<Long> days, List<Long> subIds) throws Exception {
        ClientSaleStatistics res = new ClientSaleStatistics();
        Map<Long, List<SaleStatistics>> map = Maps.newHashMap();
        Map<Long, List<SaleStatistics>> oldMap = Maps.newHashMap();
        List<SaleStatistics> details = Lists.newArrayList();
        for(Long subId:subIds) {
            List<SaleStatistics> list = Lists.newArrayList();
            for (Long day : days) {
                list.addAll(saleStatisticsDao.getDayList(day, subId));
            }
            map.put(subId, list);
            oldMap.put(subId, new ArrayList<SaleStatistics>());
            details.addAll(list);
        }
        res = transformClientMap(map, oldMap, subIds);
        res.setDetails(details);
        return res;
    }

    @Override
    public ClientSaleStatistics getDay(long dayTime, List<Long> subIds) throws Exception {
        ClientSaleStatistics res = new ClientSaleStatistics();
        Map<Long, List<SaleStatistics>> map = Maps.newHashMap();
        Map<Long, List<SaleStatistics>> oldMap = Maps.newHashMap();

        for(Long subId:subIds) {
            List<SaleStatistics> saleStatistics = map.get(subId);
            if (null == saleStatistics) {
                SaleStatistics one = new SaleStatistics();
                one.setDay(TimeUtils.getDayFormTime(dayTime));
                one.setSubId(subId);
                one.setWeek(TimeUtils.getWeekFormTime(dayTime));
                one.setMonth(TimeUtils.getMonthFormTime(dayTime));
                one = saleStatisticsDao.load(one);
                saleStatistics = Lists.newArrayList();
                if (one != null) {
                    saleStatistics.add(one);
                    map.put(subId, saleStatistics);
                }
                map.put(subId, saleStatistics);
            }

            //去年同期数据
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DATE, -1);
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
            long oldTime = calendar.getTimeInMillis();

            List<SaleStatistics> oldList = oldMap.get(subId);
            if (null == oldList) {
                SaleStatistics old = new SaleStatistics();
                old.setDay(TimeUtils.getDayFormTime(oldTime));
                old.setSubId(subId);
                old.setWeek(TimeUtils.getWeekFormTime(oldTime));
                old.setMonth(TimeUtils.getMonthFormTime(oldTime));
                old = saleStatisticsDao.load(old);
                oldList = Lists.newArrayList();
                if (old != null) {
                    oldList.add(old);
                    oldMap.put(subId, oldList);
                }
                oldMap.put(subId, oldList);
            }
        }
        res = transformClientMap(map, oldMap, subIds);
        if(oldMap.size()>0){
            ClientSaleStatistics last = transformClientMap(oldMap, Maps.<Long, List<SaleStatistics>>newHashMap(), subIds);
            //计算销售额去年今日比较今日的增长率或减少率
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
                if (last.getSale() > 0) {
                    double rate = ArithUtils.div(subSale, last.getSale(), 2);
                    res.setSaleRate(rate);
                }
            }
            //计算销售单数去年今日比较今日的增长率或减少率
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
        return res;
    }

    @Override
    public ClientSaleStatistics getWeek(long week, List<Long> subIds) throws Exception {
        ClientSaleStatistics res = new ClientSaleStatistics();
        Map<Long, List<SaleStatistics>> map = Maps.newHashMap();
        Map<Long, List<SaleStatistics>> oldMap = Maps.newHashMap();
        List<SaleStatistics> details = Lists.newArrayList();
        for (Long subId : subIds) {
            List<SaleStatistics> saleStatistics = map.get(subId);
            if (null == saleStatistics) {
                saleStatistics = saleStatisticsDao.getWeekList(week, subId);
                map.put(subId, saleStatistics);
            }
            map.put(subId, saleStatistics);

            //去年同期数据
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
            long oldWeek = TimeUtils.getWeekFormTime(calendar.getTimeInMillis());
            List<SaleStatistics> oldList = oldMap.get(subId);
            if (null == oldList) {
                oldList = saleStatisticsDao.getWeekList(oldWeek, subId);
                oldMap.put(subId, oldList);
            }
            oldMap.put(subId, oldList);
            details.addAll(saleStatistics);
        }
        res = transformClientMap(map, oldMap, subIds);
        res.setDetails(details);
        if (oldMap.size() > 0) {
//            ClientSaleStatistics last = transformClient(oldList, Lists.<SaleStatistics>newArrayList(), subId);
            ClientSaleStatistics last = transformClientMap(oldMap, Maps.<Long, List<SaleStatistics>>newHashMap(), subIds);
            if (last != null) {
                //计算销售额去年本周比较本周的增长率或减少率
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
                    if (res.getSale() > 0) {
                        double rate = ArithUtils.div(subSale, last.getSale(), 2);
                        res.setSaleRate(rate);
                    }
                }
                //计算销售单数去年本周比较本周的增长率或减少率
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
    public ClientSaleStatistics getMonth(long month, List<Long> subIds) throws Exception {
        ClientSaleStatistics res = new ClientSaleStatistics();
        Map<Long, List<SaleStatistics>> map = Maps.newHashMap();
        Map<Long, List<SaleStatistics>> oldMap = Maps.newHashMap();
        List<SaleStatistics> details = Lists.newArrayList();

        for (Long subId : subIds) {
            List<SaleStatistics> saleStatistics = map.get(subId);
            if (null == saleStatistics) {
                saleStatistics = saleStatisticsDao.getMonthList(month, subId);
                map.put(subId, saleStatistics);
            }
            map.put(subId, saleStatistics);

            //去年同期数据
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
            long oldMonth = TimeUtils.getMonthFormTime(calendar.getTimeInMillis());

            List<SaleStatistics> oldList = oldMap.get(subId);
            if (null == oldList) {
                oldList = saleStatisticsDao.getMonthList(oldMonth, subId);
                oldMap.put(subId, oldList);
            }
            oldMap.put(subId, oldList);
            details.addAll(saleStatistics);
        }
        res = transformClientMap(map, oldMap, subIds);
        res.setDetails(details);

        if (oldMap.size() > 0) {
//            ClientSaleStatistics last = transformClient(oldList, Lists.<SaleStatistics>newArrayList(), subId);
            ClientSaleStatistics last = transformClientMap(oldMap, Maps.<Long, List<SaleStatistics>>newHashMap(), subIds);
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
    public ClientSaleStatistics searchSale(long startTime, long endTime, List<Long> subIds) throws Exception {
        ClientSaleStatistics res = new ClientSaleStatistics();
        Map<Long, List<SaleStatistics>> map = Maps.newHashMap();
        Map<Long, List<SaleStatistics>> oldMap = Maps.newHashMap();
        List<SaleStatistics> details = Lists.newArrayList();
        for(Long subId:subIds) {
//        ClientSaleStatistics res = transformClient(saleStatistics,new ArrayList<SaleStatistics>(),subId);
            List<SaleStatistics> saleStatistics = map.get(subId);
            if (null == saleStatistics) {
                String sql = "SELECT  *  FROM `sale_statistics` where  1=1 ";
                if (subId > 0) {
                    sql = sql + " and `subId` = " + subId;
                }
                if (startTime > 0) {
                    sql = sql + " and `day` > " + startTime;
                }
                if (endTime > 0) {
                    sql = sql + " and `day` <= " + endTime;
                }

                sql = sql + " order  by `day` desc";
                saleStatistics = jdbcTemplate.query(sql, new HyperspaceBeanPropertyRowMapper(SaleStatistics.class));
                map.put(subId, saleStatistics);
            }
            map.put(subId, saleStatistics);
            oldMap.put(subId, new ArrayList<SaleStatistics>());
            details.addAll(saleStatistics);
        }
        res = transformClientMap(map, oldMap, subIds);
        res.setDetails(details);
        sort(details);
        return res;
    }

    private void sort(List<SaleStatistics> list){
        Collections.sort(list, new Comparator<SaleStatistics>(){
            public int compare(SaleStatistics p1, SaleStatistics p2) {
                if(p1.getDay() > p2.getDay()){
                    return 1;
                }
                if(p1.getDay() == p2.getDay()){
                    return 0;
                }
                return -1;
            }
        });
    }

    @Override
    public ClientSaleStatistics getDate(long day, List<Long> subIds) throws Exception {
        ClientSaleStatistics res = new ClientSaleStatistics();
        Map<Long, List<SaleStatistics>> map = Maps.newHashMap();
        Map<Long, List<SaleStatistics>> oldMap = Maps.newHashMap();
        List<SaleStatistics> details = Lists.newArrayList();
        for(Long subId:subIds) {
            List<SaleStatistics> saleStatistics = map.get(subId);
            if (null == saleStatistics) {
                SaleStatistics one = new SaleStatistics();
                one.setDay(day);
                one.setSubId(subId);
                one.setWeek(TimeUtils.getWeekFormTime(TimeUtils.getTimeFormDay(day)));
                one.setMonth(TimeUtils.getMonthFormTime(TimeUtils.getTimeFormDay(day)));
                one = saleStatisticsDao.load(one);
                saleStatistics = Lists.newArrayList();
                if (one != null) {
                    saleStatistics.add(one);
                    map.put(subId, saleStatistics);
                }
                map.put(subId, saleStatistics);
            }


            //去年同期数据
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DATE, -1);
            calendar.setTime(new Date(TimeUtils.getTimeFormDay(day)));
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
            long oldTime = calendar.getTimeInMillis();

            List<SaleStatistics> oldList = oldMap.get(subId);
            if (null == oldList) {
                SaleStatistics old = new SaleStatistics();
                old.setDay(TimeUtils.getDayFormTime(oldTime));
                old.setSubId(subId);
                old.setWeek(TimeUtils.getWeekFormTime(oldTime));
                old.setMonth(TimeUtils.getMonthFormTime(oldTime));
                old = saleStatisticsDao.load(old);
                oldList = Lists.newArrayList();
                if (old != null) {
                    oldList.add(old);
                    oldMap.put(subId, oldList);
                }
                oldMap.put(subId, oldList);
            }
        }

        res = transformClientMap(map, oldMap, subIds);
        if(oldMap.size()>0) {
            ClientSaleStatistics last = transformClientMap(oldMap, Maps.<Long, List<SaleStatistics>>newHashMap(), subIds);
            if (res != null && last != null) {
                //计算销售额去年的日期比较选择的日期的增长率或减少率
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
                    if (last.getSale() > 0) {
                        double rate = ArithUtils.div(subSale, last.getSale(), 2);
                        res.setSaleRate(rate);
                    }
                }
                //计算销售单数去年的日期比较选择的日期的增长率或减少率
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


    private ClientSaleStatistics transformClientMap(Map<Long,List<SaleStatistics>> statisticsMap,Map<Long,List<SaleStatistics>> oldMap,List<Long> subIds) throws Exception{
        ClientSaleStatistics client = new ClientSaleStatistics();
        double sale=0;
        int num=0;
        double perPrice=0;
        double profits=0;
        int customer = 0;

        double saleOld=0;
        int numOld=0;
        double perPriceOld=0;
        double profitsOld=0;
        int customerOld = 0;

        for(Long subId:subIds){
            Subordinate subordinate = subordinateService.load(subId);
            if(subordinate!=null) client.setSubName(subordinate.getName());

            for(SaleStatistics statistics:statisticsMap.get(subId)){
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


            if(oldMap!=null&&oldMap.size()>0){
                for(SaleStatistics statistics:oldMap.get(subId)){
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

        }
        client.setCustomer(customer);
        client.setSale(sale);
        client.setNum(num);
        client.setPerPrice(perPrice);
        client.setProfits(profits);
        client.setSaleOld(saleOld);
        client.setNumOld(numOld);
        client.setPerPriceOld(perPriceOld);
        client.setProfitsOld(profitsOld);
        client.setCustomerOld(customerOld);
        return client;
    }
}
