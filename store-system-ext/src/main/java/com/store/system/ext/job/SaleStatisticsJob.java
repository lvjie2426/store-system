package com.store.system.ext.job;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.Uninterruptibles;
import com.store.system.client.ClientOrder;
import com.store.system.dao.ProductSKUDao;
import com.store.system.dao.ProductSPUDao;
import com.store.system.dao.SaleStatisticsDao;
import com.store.system.model.*;
import com.store.system.service.OrderService;
import com.store.system.service.ProductCategoryService;
import com.store.system.service.SubordinateService;
import com.store.system.util.ArithUtils;
import com.store.system.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SaleStatisticsJob
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/15 11:09
 * @Version 1.0
 **/
public class SaleStatisticsJob implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(SaleStatisticsJob.class);

    @Resource
    private SubordinateService subordinateService;
    @Resource
    private SaleStatisticsDao saleStatisticsDao;
    @Resource
    private OrderService orderService;
    @Resource
    private ProductSKUDao productSKUDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        Thread thread = new Thread(new handle());
        thread.start();
    }

    class handle implements Runnable {
        @Override
        public void run() {
            while(true) {
                Uninterruptibles.sleepUninterruptibly(1000, TimeUnit.MILLISECONDS);
                try {
                    long currentTime = System.currentTimeMillis();
                    long day = TimeUtils.getDayFormTime(currentTime);
                    long month = TimeUtils.getMonthFormTime(currentTime);
                    List<Subordinate> subordinates = subordinateService.getAllList();
                    for(Subordinate subordinate:subordinates){
                        createSaleStatisticsJobs(subordinate.getId(), day, month);
                    }

                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    public void createSaleStatisticsJobs(long subId, long day, long month) throws Exception {
        SaleStatistics saleStatistics = new SaleStatistics();
        saleStatistics.setSubId(subId);
        saleStatistics.setDay(day);
        saleStatistics.setWeek(TimeUtils.getWeekDayFormDay(day));
        saleStatistics.setMonth(month);
        SaleStatistics dbInfo = saleStatisticsDao.load(saleStatistics);
        boolean update = true;
        if (dbInfo == null) {
            update = false;
            dbInfo = saleStatistics;
        }

        long sale=0;
        double perPrice=0;
        long profits=0;
        Set<Long> skuIds = Sets.newHashSet();
        List<ClientOrder> orders = orderService.getAllBySubid(subId);
        for(ClientOrder order:orders){
            for(OrderSku sku:order.getSkuids()){
                skuIds.add(sku.getSkuid());
            }
            ClientOrder client =  orderService.countPrice(order);
            sale += client.getPrice();
        }

        perPrice = ArithUtils.div((double) sale,(double) orders.size(),2);

        List<ProductSKU> skus = productSKUDao.load(Lists.newArrayList(skuIds));
        for(ProductSKU sku:skus){
            profits += sku.getCostPrice();
        }
        dbInfo.setSale(sale/100d);
        dbInfo.setNum(orders.size());
        dbInfo.setPerPrice(perPrice);
        dbInfo.setProfits(profits/100d);

        if (update) {
            saleStatisticsDao.update(dbInfo);
        } else {
            saleStatisticsDao.insert(dbInfo);
        }

    }
}
