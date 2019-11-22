package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.store.system.client.ClientStatisticsCustomer;
import com.store.system.dao.StatisticsCustomerJobDao;
import com.store.system.dao.SubordinateDao;
import com.store.system.model.StatisticsCustomerJob;
import com.store.system.model.Subordinate;
import com.store.system.service.StatisticsCustomerJobService;
import com.store.system.util.DateUtils;
import com.store.system.util.TimeUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ProjectName: store-system
 * @Author: LiHaoJie
 * @Description:
 * @Date: 2019/6/15 16:20
 * @Version: 1.0
 */
@Service
public class StatisticsCustomerJobServiceImpl implements StatisticsCustomerJobService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private SubordinateDao subordinateDao;

    @Resource
    private StatisticsCustomerJobDao statisticsCustomerJobDao;


    //右侧 查询当前门店 总数据
    @Override
    public ClientStatisticsCustomer getCustomerCount(long subid, String date, int type) throws Exception {
        List<StatisticsCustomerJob> customers = Lists.newArrayList();
        if(type == 1){//本周
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis((Long.valueOf(date)));
            String week = String.valueOf(calendar.get(Calendar.YEAR))+String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));
            customers = statisticsCustomerJobDao.getWeekList(subid,Integer.valueOf(week));
        }else if(type == 2){//本月
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(date));
            List<Long> days = TimeUtils.getPastDays(calendar.get(Calendar.DATE));//20190615 获得15
            for(Long day:days){
               List<StatisticsCustomerJob> statisticsCustomers = statisticsCustomerJobDao.getDayList(subid,day.intValue());
               if(statisticsCustomers.size()>0){
                    customers.add(statisticsCustomers.get(0));
               }
            }
        }else if(type == 3){//近半年
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(date));
            List<Long> days = TimeUtils.getPastMonthDays(calendar.get(Calendar.MONTH)+1);//20190615获得6
            for(Long day:days){
                List<StatisticsCustomerJob> statisticsCustomers = statisticsCustomerJobDao.getDayList(subid,day.intValue());
                if(statisticsCustomers.size()>0){
                    customers.add(statisticsCustomers.get(0));
                }
            }
        }
        return statisticsCustomer(customers,subid);
    }

    @Override
    public ClientStatisticsCustomer getCustomerByTime(long subid, long startTime, long endTime) throws Exception {
        String sql = " SELECT * FROM statistics_customer_job where 1=1 AND ctime > "+startTime + " AND ctime < " + endTime + " AND subid = " + subid;
        List<StatisticsCustomerJob> customers = jdbcTemplate.query(sql,new HyperspaceBeanPropertyRowMapper<StatisticsCustomerJob>(StatisticsCustomerJob.class));
        return statisticsCustomer(customers,subid);
    }

    @Override
    public ClientStatisticsCustomer getWebCustomerByTime(long subid, long startTime, long endTime) throws Exception {
        List<StatisticsCustomerJob> list = statisticsCustomerJobDao.getList(subid);
        List<StatisticsCustomerJob> customers =new ArrayList<>();
        for(StatisticsCustomerJob statisticsCustomerJob:list){
            if(statisticsCustomerJob.getCtime()>startTime&&statisticsCustomerJob.getCtime()<endTime){
                customers.add(statisticsCustomerJob);
            }
        }
        return statisticsCustomer(customers,subid);
    }

    private ClientStatisticsCustomer statisticsCustomer(List<StatisticsCustomerJob> customers,long subid)throws Exception{
        ClientStatisticsCustomer res = new ClientStatisticsCustomer(new StatisticsCustomerJob());
        List<StatisticsCustomerJob> details = Lists.newArrayList();

        int man =  0;
        int woman = 0;
        int total = 0;
        int oldNum = 0;
        int returnNum = 0;
        List<Integer> ages = Lists.newArrayList();
       List<Map<String,Object>> ListMap=new ArrayList<>();
       List<Map<String,Object>> wListMap=new ArrayList<>();
        if(customers.size()>0){
            for(StatisticsCustomerJob customer:customers){
                ages.addAll(customer.getAge());
                man+=customer.getMan();
                oldNum+=customer.getOldNum();
                returnNum+=customer.getReturnNum();
                woman+=customer.getWoman();
                total=man+woman;
                details.add(customer);
                ListMap.add(customer.getManAge());
                wListMap.add(customer.getWomanAge());

            }
            res.setAge(ages);
            res.setMan(man);
            res.setWoman(woman);
            res.setTotal(total);
            res.setManProportion(calculator(man,total));
            res.setWomanProportion(calculator(woman,total));

            res.setTen(getCount(ages,0,10));//年龄 0 - 10人数
            res.setTwenty(getCount(ages,11,20));
            res.setForty(getCount(ages,21,40));
            res.setSixty(getCount(ages,41,60));
            res.setMore(getCount(ages,61,999));

            res.setSubid(subid);
            res.setDetails(details);

            res.setOldNum(oldNum);
            res.setReturnNum(returnNum);

            res.setManAge(countMap(ListMap));
            res.setWomanAge(countMap(wListMap));

        }
        Subordinate subordinate = subordinateDao.load(subid);
        if(subordinate!=null){ res.setSubName(subordinate.getName()); }
        return res;
    }

    public Map<String,Object>  countMap( List<Map<String,Object>> ListMap){

        Map<String,Object> mapAll = new HashMap<>();
        for(Map<String,Object> map:ListMap){
            for(Map.Entry<String, Object> entry:map.entrySet()){
                String name = entry.getKey();
                Object score = entry.getValue();
                Object scoreAll = mapAll.get(entry.getKey());
                if(scoreAll == null){
                    mapAll.put(name, score);
                }else{
                    scoreAll = new Integer((((Integer)scoreAll).intValue() + ((Integer)score).intValue()));
                    mapAll.put(name, scoreAll);
                }
            }
        }
        return mapAll;
    }


        //计算 某个年龄段的人数
    private int getCount(List<Integer> ages,int start,int end)throws Exception{
        int count = 0;
        for(Integer age : ages){
            if(age>=start && age<=end){
                count++;
            }
        }
        return count;
    }

    //计算比例
    private int calculator(int num,int allNum){
        float persion =  num;
        float total = allNum;
        int rel = (int) (persion/total*(float)100);
        return rel;
    }

}
