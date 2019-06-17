package com.store.system.ext.job;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.store.system.dao.StatisticsCustomerJobDao;
import com.store.system.dao.SubordinateDao;
import com.store.system.model.StatisticsCustomerJob;
import com.store.system.model.Subordinate;
import com.store.system.model.User;
import com.store.system.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: store-system
 * @Author: LiHaoJie
 * @Description: 统计任务
 * @Date: 2019/6/15 19:13
 * @Version: 1.0
 */
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
            while(true) {
                Uninterruptibles.sleepUninterruptibly(1000, TimeUnit.MILLISECONDS);
                try {
                    long currentTime = System.currentTimeMillis();
                    List<Subordinate> subordinates = subordinateDao.getAllList(0);
                    for(Subordinate subordinate:subordinates){
                        long subid = subordinate.getId();
                        //查询今天ctime大于 00:00的一条记录
                        String sqlSubordinate = " SELECT * FORM statistics_customer_job WHERE 1=1 AND ctime <"
                                + DateUtils.getStartTimeOfDay(new Date().getTime(),"GMT+8") + " AND subid = " + subid;
                        List<StatisticsCustomerJob> statisticsCustomers = jdbcTemplate.query(sqlSubordinate,
                                new HyperspaceBeanPropertyRowMapper<StatisticsCustomerJob>(StatisticsCustomerJob.class));
                        //查询今天创建的所有用户
                        String sqlUser = " SELECT * FORM user WHERE 1=1 AND ctime <" + DateUtils.getStartTimeOfDay(new Date().getTime(),"GMT+8")
                                +" AND userType = " + User.userType_user + " AND subid = " + subid;
                        List<User> users =  jdbcTemplate.query(sqlUser,new HyperspaceBeanPropertyRowMapper<User>(User.class));
                        if(statisticsCustomers.size()>0){
                            StatisticsCustomerJob statisticsCustomer = statisticsCustomers.get(0);
                            if(users.size()>0){
                                for(User user : users){
                                    statisticsCustomer.getAge().add(user.getAge());
                                    if(user.getSex()==User.sex_mai){
                                        statisticsCustomer.setMan(statisticsCustomer.getMan()+1);
                                    }else{
                                        statisticsCustomer.setMonth(statisticsCustomer.getWoman()+1);
                                    }
                                }
                                statisticsCustomerJobDao.update(statisticsCustomer);
                            }
                        }else{
                            StatisticsCustomerJob statisticsCustomerJob = new StatisticsCustomerJob();
                            Calendar calendar = Calendar.getInstance();
                            statisticsCustomerJob.setSubid(subid);
                            statisticsCustomerJob.setDay(DateUtils.getDate());
                            statisticsCustomerJob.setWeek(calendar.get(Calendar.WEEK_OF_YEAR));//今年第几周
                            statisticsCustomerJob.setMonth(DateUtils.getDate());
                            if(users.size()>0){
                                for(User user : users){
                                    statisticsCustomerJob.getAge().add(user.getAge());
                                    if(user.getSex()==User.sex_mai){
                                        statisticsCustomerJob.setMan(statisticsCustomerJob.getMan()+1);
                                    }else{
                                        statisticsCustomerJob.setMonth(statisticsCustomerJob.getWoman()+1);
                                    }
                                }
                                statisticsCustomerJobDao.insert(statisticsCustomerJob);
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }finally{
                    try {
                        Thread.sleep(1000*60*60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
