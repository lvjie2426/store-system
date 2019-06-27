package com.store.system.ext.job;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;
import com.store.system.client.ClientInventoryDetail;
import com.store.system.client.ClientInventoryWarehouse;
import com.store.system.client.ClientOrder;
import com.store.system.dao.InventoryStatisticsDao;
import com.store.system.model.*;
import com.store.system.service.InventoryDetailService;
import com.store.system.service.InventoryWarehouseService;
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
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName InventoryStatisticsJob
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/27 16:37
 * @Version 1.0
 **/
public class InventoryStatisticsJob implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(InventoryStatisticsJob.class);

    @Resource
    private ProductCategoryService productCategoryService;
    @Resource
    private SubordinateService subordinateService;
    @Resource
    private InventoryStatisticsDao inventoryStatisticsDao;
    @Resource
    private InventoryDetailService inventoryDetailService;
    @Resource
    private InventoryWarehouseService inventoryWarehouseService;

    @Override
    public void afterPropertiesSet() throws Exception {
        Thread thread = new Thread(new InventoryStatisticsJob.handle());
        thread.start();
    }

    class handle implements Runnable {
        @Override
        public void run() {
            while (true) {
                Uninterruptibles.sleepUninterruptibly(1000, TimeUnit.MILLISECONDS);
                try {
                    long currentTime = System.currentTimeMillis();
                    long day = TimeUtils.getDayFormTime(currentTime);
                    long month = TimeUtils.getMonthFormTime(currentTime);

                    List<ProductCategory> categories = productCategoryService.getAllList();
                    for (ProductCategory category : categories) {
                        List<Subordinate> subordinates = subordinateService.getAllList();
                        for (Subordinate subordinate : subordinates) {
                            createInventoryStatisticsJobs(subordinate.getId(), category.getId(), day, month);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    public void createInventoryStatisticsJobs(long subId, long cid, long day, long month) throws Exception {
        InventoryStatistics statistics = new InventoryStatistics();
        statistics.setSubId(subId);
        statistics.setDay(day);
        statistics.setCid(cid);
        statistics.setWeek(TimeUtils.getWeekDayFormDay(day));
        statistics.setMonth(month);

        InventoryStatistics dbInfo = inventoryStatisticsDao.load(statistics);
        boolean update = true;
        if (dbInfo == null) {
            update = false;
            dbInfo = statistics;
        }

        int num = 0;
        double price = 0;
        List<ClientInventoryDetail> details = inventoryDetailService.getAllList(subId);
        for (ClientInventoryDetail detail : details) {
            num += detail.getNum();
            price += detail.getP_costPrice();
        }


        dbInfo.setNum(num);
        dbInfo.setPrice(ArithUtils.div(price, 10.0, 2));

        if (update) {
            inventoryStatisticsDao.update(dbInfo);
        } else {
            inventoryStatisticsDao.insert(dbInfo);
        }

    }

}
