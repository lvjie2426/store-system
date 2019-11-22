package com.store.system.ext.job;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;
import com.quakoo.ext.RowMapperHelp;
import com.quakoo.space.annotation.cache.CacheDaoMethod;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.store.system.dao.StatisticsCustomerJobDao;
import com.store.system.dao.SubordinateDao;
import com.store.system.model.*;
import com.store.system.util.DateUtils;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: store-system
 * @Author: LiHaoJie
 * @Description: 统计任务
 * @Date: 2019/6/15 19:13
 * @Version: 1.0
 */
@Component
@EnableScheduling
public class CustomerStatisticsJob implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(SaleStatisticsJob.class);

    @Resource
    private SubordinateDao subordinateDao;
    @Resource
    private StatisticsCustomerJobDao statisticsCustomerJobDao;
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        Thread thread = new Thread(new CustomerStatisticsJob.handle());
        thread.start();
    }

    class handle implements Runnable {
        @Override
        public void run() {
            while (true) {
                Uninterruptibles.sleepUninterruptibly(1000, TimeUnit.MILLISECONDS);
                try {
                    List<Subordinate> subordinates = subordinateDao.getAllList(0, Subordinate.status_online);
                    for (Subordinate subordinate : subordinates) {
                        long subid = subordinate.getId();
                        List<Subordinate> childrens = subordinateDao.getAllList(subid, Subordinate.status_online);
                        for (Subordinate children : childrens) {
                            //查询今天ctime大于 00:00的一条记录
                            String sqlSubordinate = " SELECT * from statistics_customer_job WHERE 1=1 AND ctime >"
                                    + DateUtils.getStartTimeOfDay(new Date().getTime(), "GMT+8") + " AND subid = " + children.getId();
                            List<StatisticsCustomerJob> statisticsCustomers = jdbcTemplate.query(sqlSubordinate,
                                    new RowMapperHelp<StatisticsCustomerJob>(StatisticsCustomerJob.class));
                            //查询今天创建的所有用户
                            String sqlUser = " SELECT * from user WHERE 1=1 AND ctime >" + DateUtils.getStartTimeOfDay(new Date().getTime(), "GMT+8")
                                    + " AND userType = " + User.userType_user + " AND sid = " + children.getId();
                            List<User> users = jdbcTemplate.query(sqlUser, new HyperspaceBeanPropertyRowMapper<User>(User.class));

                            // 查询今日下单
                            String businessSql = "select count(1) as num from business_order where ctime > "
                                    + DateUtils.getStartTimeOfDay(new Date().getTime(), "GMT+8") ;
                            List<Integer> businessOrders = jdbcTemplate.queryForList(businessSql, Integer.class);
//
                            int ten = 0;
                            int twenty = 0;
                            int forty = 0;
                            int sixty = 0;
                            int more = 0;

                            int Wten = 0;
                            int Wtwenty = 0;
                            int Wforty = 0;
                            int Wsixty = 0;
                            int Wmore = 0;

                            if (statisticsCustomers.size() > 0) {
                                StatisticsCustomerJob statisticsCustomer = statisticsCustomers.get(0);
                                if (users.size() > 0) {
                                    for (User user : users) {
                                        statisticsCustomer.getAge().add(user.getAge());
                                        if (user.getSex() == User.sex_mai) {
                                            statisticsCustomer.setMan(statisticsCustomer.getMan() + 1);
                                            ten += (getCount(Lists.newArrayList(user.getAge()), 0, 10));
                                            twenty += (getCount(Lists.newArrayList(user.getAge()), 11, 20));
                                            forty += (getCount(Lists.newArrayList(user.getAge()), 21, 40));
                                            sixty += (getCount(Lists.newArrayList(user.getAge()), 41, 60));
                                            more += (getCount(Lists.newArrayList(user.getAge()), 61, 999));
                                        } else {
                                            statisticsCustomer.setWoman(statisticsCustomer.getWoman() + 1);
                                            Wten += (getCount(Lists.newArrayList(user.getAge()), 0, 10));
                                            Wtwenty += (getCount(Lists.newArrayList(user.getAge()), 11, 20));
                                            Wforty += (getCount(Lists.newArrayList(user.getAge()), 21, 40));
                                            Wsixty += (getCount(Lists.newArrayList(user.getAge()), 41, 60));
                                            Wmore += (getCount(Lists.newArrayList(user.getAge()), 61, 999));
                                        }
                                    }
                                    Map<String, Object> manAge = compute(ten, twenty, forty, sixty, more);
                                    Map<String, Object> wAge = compute(Wten, Wtwenty, Wforty, Wsixty, Wmore);

                                    statisticsCustomer.setManAge(manAge);
                                    statisticsCustomer.setWomanAge(wAge);

                                    if (businessOrders.size() > 0) {
                                        for (Integer list : businessOrders) {
                                            if (list == 2) {
                                                // 回头客
                                                statisticsCustomer.setReturnNum(statisticsCustomer.getReturnNum() + 1);
                                            } else if (list >= 3) {
                                                // 老顾客
                                                statisticsCustomer.setOldNum(statisticsCustomer.getOldNum() + 1);
                                            }
                                        }
                                    }

                                    statisticsCustomer.setTotal(statisticsCustomer.getMan() + statisticsCustomer.getWoman());
                                    statisticsCustomerJobDao.update(statisticsCustomer);
                                }
                            } else {
                                StatisticsCustomerJob statisticsCustomerJob = new StatisticsCustomerJob();
                                Calendar calendar = Calendar.getInstance();
                                statisticsCustomerJob.setSubid(subid);
                                statisticsCustomerJob.setDay(DateUtils.getDate());
                                //拼接 201929 2019年的29周
                                String week = String.valueOf((calendar.get(Calendar.YEAR))) + String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));
                                statisticsCustomerJob.setWeek(Integer.valueOf(week));//今年第几周
                                statisticsCustomerJob.setMonth(DateUtils.getDate());
                                if (users.size() > 0) {
                                    for (User user : users) {
                                        statisticsCustomerJob.getAge().add(user.getAge());
                                        if (user.getSex() == User.sex_mai) {
                                            statisticsCustomerJob.setMan(statisticsCustomerJob.getMan() + 1);
                                            ten += (getCount(Lists.newArrayList(user.getAge()), 0, 10));
                                            twenty += (getCount(Lists.newArrayList(user.getAge()), 11, 20));
                                            forty += (getCount(Lists.newArrayList(user.getAge()), 21, 40));
                                            sixty += (getCount(Lists.newArrayList(user.getAge()), 41, 60));
                                            more += (getCount(Lists.newArrayList(user.getAge()), 61, 999));
                                        } else {
                                            statisticsCustomerJob.setWoman(statisticsCustomerJob.getWoman() + 1);
                                            Wten += (getCount(Lists.newArrayList(user.getAge()), 0, 10));
                                            Wtwenty += (getCount(Lists.newArrayList(user.getAge()), 11, 20));
                                            Wforty += (getCount(Lists.newArrayList(user.getAge()), 21, 40));
                                            Wsixty += (getCount(Lists.newArrayList(user.getAge()), 41, 60));
                                            Wmore += (getCount(Lists.newArrayList(user.getAge()), 61, 999));
                                        }
                                    }
                                    Map<String, Object> manAge = compute(ten, twenty, forty, sixty, more);
                                    Map<String, Object> wAge = compute(Wten, Wtwenty, Wforty, Wsixty, Wmore);

                                    statisticsCustomerJob.setManAge(manAge);
                                    statisticsCustomerJob.setWomanAge(wAge);
                                    if (businessOrders.size() > 0) {
                                        for (Integer list : businessOrders) {
                                            if (list == 2) {
                                                // 回头客
                                                statisticsCustomerJob.setReturnNum(statisticsCustomerJob.getReturnNum() + 1);
                                            } else if (list >= 3) {
                                                // 老顾客
                                                statisticsCustomerJob.setOldNum(statisticsCustomerJob.getOldNum() + 1);
                                            }
                                        }
                                    }
                                    statisticsCustomerJob.setTotal(statisticsCustomerJob.getMan() + statisticsCustomerJob.getWoman());
                                    statisticsCustomerJobDao.insert(statisticsCustomerJob);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                } finally {
                    try {
                        Thread.sleep(1000 * 60 * 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private Map<String, Object> compute(int wten, int wtwenty, int wforty, int wsixty, int wmore) {
        Map<String, Object> list = new HashedMap();
        list.put("ten", wten);
        list.put("twenty", wtwenty);
        list.put("forty", wforty);
        list.put("sixty", wsixty);
        list.put("more", wmore);
        return list;

    }

    //计算 某个年龄段的人数
    private int getCount(List<Integer> ages, int start, int end) throws Exception {
        int count = 0;
        for (Integer age : ages) {
            if (age >= start && age <= end) {
                count++;
            }
        }
        return count;
    }

}
