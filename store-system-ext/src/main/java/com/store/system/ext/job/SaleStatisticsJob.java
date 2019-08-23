package com.store.system.ext.job;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.Uninterruptibles;
import com.store.system.client.ClientBusinessOrder;
import com.store.system.client.ResultClient;
import com.store.system.dao.ProductSKUDao;
import com.store.system.dao.SaleStatisticsDao;
import com.store.system.model.*;
import com.store.system.service.BusinessOrderService;
import com.store.system.service.SubordinateService;
import com.store.system.util.ArithUtils;
import com.store.system.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**门店销售数据统计
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
    private BusinessOrderService businessOrderService;
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

    private void createSaleStatisticsJobs(long subId, long day, long month) throws Exception {
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
        List<ClientBusinessOrder> businessOrders = businessOrderService.getAllList(subId, BusinessOrder.status_pay, BusinessOrder.makeStatus_qu_yes);
        for(ClientBusinessOrder order:businessOrders){
            for(OrderSku sku:order.getSkuList()){
                skuIds.add(sku.getSkuId());
            }
            ResultClient client =  businessOrderService.currentCalculate(order);
            BusinessOrder info = (BusinessOrder) client.getData();
            sale += info.getTotalPrice();
        }

        perPrice = ArithUtils.div((double) sale,(double) businessOrders.size(),2);

        List<ProductSKU> skus = productSKUDao.load(Lists.newArrayList(skuIds));
        for(ProductSKU sku:skus){
            profits += sku.getCostPrice();
        }
        dbInfo.setSale(sale/100d);
        dbInfo.setNum(businessOrders.size());
        dbInfo.setPerPrice(perPrice);
        dbInfo.setProfits(profits/100d);

        if (update) {
            saleStatisticsDao.update(dbInfo);
        } else {
            saleStatisticsDao.insert(dbInfo);
        }

    }
}
