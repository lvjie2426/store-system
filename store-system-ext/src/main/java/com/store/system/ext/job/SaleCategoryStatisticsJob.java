package com.store.system.ext.job;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.Uninterruptibles;
import com.store.system.client.ClientOrder;
import com.store.system.client.ClientOrderSku;
import com.store.system.dao.ProductSKUDao;
import com.store.system.dao.ProductSPUDao;
import com.store.system.dao.SaleCategoryStatisticsDao;
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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SaleCategoryStatisticsJob
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/18 14:38
 * @Version 1.0
 **/
public class SaleCategoryStatisticsJob implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(SaleCategoryStatisticsJob.class);

    @Resource
    private SubordinateService subordinateService;
    @Resource
    private SaleCategoryStatisticsDao saleCategoryStatisticsDao;
    @Resource
    private ProductCategoryService productCategoryService;
    @Resource
    private OrderService orderService;
    @Resource
    private ProductSPUDao productSPUDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        Thread thread = new Thread(new SaleCategoryStatisticsJob.handle());
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
                        List<ProductCategory> categories = productCategoryService.getAllList();
                        for(ProductCategory category:categories) {
                            createSaleStatisticsJobs(subordinate.getId(), category.getId(), day, month);
                        }
                    }

                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }


    public void createSaleStatisticsJobs(long subId, long cid, long day, long month) throws Exception {
        SaleCategoryStatistics statistics = new SaleCategoryStatistics();
        statistics.setSubId(subId);
        statistics.setCid(cid);
        statistics.setDay(day);
        statistics.setWeek(TimeUtils.getWeekDayFormDay(day));
        statistics.setMonth(month);
        SaleCategoryStatistics dbInfo = saleCategoryStatisticsDao.load(statistics);
        boolean update = true;
        if (dbInfo == null) {
            update = false;
            dbInfo = statistics;
        }

        double sale=0;
        double perPrice=0;
        List<ClientOrderSku> skus = Lists.newArrayList();
        List<ClientOrderSku> skuList = Lists.newArrayList();
        List<ClientOrder> orders = orderService.getAllBySubid(subId);
        for(ClientOrder order:orders){
            for(OrderSku sku:order.getSkuids()){
                ProductSPU productSPU = productSPUDao.load(sku.getSpuid());
                if(productSPU.getCid()==cid){
                    Map<Object,Object> map = orderService.countSkuPrice(order.getUid(),
                            Lists.newArrayList(sku),0,new ArrayList<Surcharge>());
                    List<ClientOrderSku> clientOrderSkus = (List<ClientOrderSku>) map.get("clientOrderSkus");
                    skus.addAll(clientOrderSkus);
                    skuList.addAll(clientOrderSkus);
                }
            }
        }
        for(ClientOrderSku sku:skus){
            sale = ArithUtils.add(sale,sku.getLastSubtotal());
        }
        perPrice = ArithUtils.div(sale,(double) skus.size(),2);
        dbInfo.setSale(sale);
        dbInfo.setNum(skus.size());
        dbInfo.setPerPrice(perPrice);
        dbInfo.setSalesLog(skuList);

        if (update) {
            saleCategoryStatisticsDao.update(dbInfo);
        } else {
            saleCategoryStatisticsDao.insert(dbInfo);
        }

    }
}
