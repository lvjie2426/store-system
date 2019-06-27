package com.store.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quakoo.space.mapper.HyperspaceBeanPropertyRowMapper;
import com.store.system.client.ClientCategoryStatistics;
import com.store.system.dao.SaleCategoryStatisticsDao;
import com.store.system.model.OrderSku;
import com.store.system.model.ProductCategory;
import com.store.system.model.SaleCategoryStatistics;
import com.store.system.service.ProductCategoryService;
import com.store.system.service.SaleCategoryStatisticsService;
import com.store.system.util.ArithUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SaleCategoryStatisticsServiceImpl
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/6/20 14:52
 * @Version 1.0
 **/
@Service
public class SaleCategoryStatisticsServiceImpl implements SaleCategoryStatisticsService{

    @Resource
    private SaleCategoryStatisticsDao saleCategoryStatisticsDao;
    @Resource
    private ProductCategoryService productCategoryService;
    @Resource
    private JdbcTemplate jdbcTemplate;



    @Override
    public Map<Long,List<ClientCategoryStatistics>>  getDayList(long subId, List<Long> days) throws Exception {
        List<SaleCategoryStatistics> list=Lists.newArrayList();
        for (Long day : days) {
            list.addAll(saleCategoryStatisticsDao.getSubList(subId, day));
        }
        return transformClientSale(list);
    }

    @Override
    public Map<Long,List<ClientCategoryStatistics>> searchSale(long startTime, long endTime, long subId) throws Exception {
        String sql = "SELECT  *  FROM `sale_category_statistics` where  1=1 ";
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
        List<SaleCategoryStatistics> saleStatistics = jdbcTemplate.query(sql, new HyperspaceBeanPropertyRowMapper(SaleCategoryStatistics.class));
        return transformClient(saleStatistics);
    }

    private Map<Long,List<ClientCategoryStatistics>> transformClient(List<SaleCategoryStatistics> saleCategoryStatistics) throws Exception {
        Map<Long,List<ClientCategoryStatistics>>  map = Maps.newHashMap();
        Map<Long,List<OrderSku>> listMap = Maps.newHashMap();
        Map<Long,List<SaleCategoryStatistics>> salesMap = Maps.newHashMap();


        List<ProductCategory> productCategories = productCategoryService.getAllList();
        List<OrderSku> list = Lists.newArrayList();

        List<ClientCategoryStatistics> clients = Lists.newArrayList();
        if (saleCategoryStatistics != null) {
            for (ProductCategory productCategory : productCategories) {
                for (SaleCategoryStatistics statistics : saleCategoryStatistics) {
                    ClientCategoryStatistics client = new ClientCategoryStatistics();
                    double perPrice = 0;
                    int num = 0;
                    double sale = 0;
                    if (productCategory.getId() == statistics.getCid()) {
                        list.addAll(statistics.getSalesLog());
                        num += statistics.getNum();
                        sale = ArithUtils.add(sale, statistics.getSale());
                        perPrice = ArithUtils.add(perPrice, statistics.getPerPrice());
                    }
                    client.setCName(productCategory.getName());
                    client.setNum(num);
                    client.setSale(sale);
                    client.setPerPrice(perPrice);
                    clients.add(client);
                }
                map.put(productCategory.getId(), clients);
            }
//            for(ProductCategory productCategory:productCategories) {
//                List<ClientOrderSku> skuList = listMap.get(productCategory.getId());
//                ClientCategoryStatistics client = new ClientCategoryStatistics();
//                client.setCName(productCategory.getName());
//                client.setNum(num);
//                client.setSale(sale);
//                client.setPerPrice(ArithUtils.div(perPrice,(double) saleCategoryStatistics.size(),2));
//                int rate_0to100=0;
//                int rate_100to500=0;
//                int rate_500to1000=0;
//                int rate_1000to2000=0;
//                int rate_2000=0;
//                for(ClientOrderSku sku:skuList){
//                    if(sku.getLastSubtotal()>0&&sku.getLastSubtotal()<=100){
//                        rate_0to100++;
//                    }
//                    if(sku.getLastSubtotal()>100&&sku.getLastSubtotal()<=500){
//                        rate_100to500++;
//                    }
//                    if(sku.getLastSubtotal()>500&&sku.getLastSubtotal()<=1000){
//                        rate_500to1000++;
//                    }
//                    if(sku.getLastSubtotal()>1000&&sku.getLastSubtotal()<=2000){
//                        rate_1000to2000++;
//                    }
//                    if(sku.getLastSubtotal()>2000){
//                        rate_2000++;
//                    }
//                }
//                client.setRate_0to100(ArithUtils.div(rate_0to100,num,2));
//                client.setRate_100to500(ArithUtils.div(rate_100to500,num,2));
//                client.setRate_500to1000(ArithUtils.div(rate_500to1000,num,2));
//                client.setRate_1000to2000(ArithUtils.div(rate_1000to2000,num,2));
//                client.setRate_2000(ArithUtils.div(rate_2000,num,2));
//                map.put(productCategory.getId(),client);
//            }
        }
        return map;
    }

    private Map<Long,List<ClientCategoryStatistics>> transformClientSale(List<SaleCategoryStatistics> saleCategoryStatistics) throws Exception {
        List<ProductCategory> productCategories = productCategoryService.getAllList();
        double total=0;
        int num=0;
        for(ProductCategory category:productCategories) {
            List<ClientCategoryStatistics> clientList = Lists.newArrayList();
            List<SaleCategoryStatistics> list = Lists.newArrayList();
            for (SaleCategoryStatistics statistics : saleCategoryStatistics) {
                if (statistics.getCid() == category.getId()) {
                    list.add(statistics);
                }
            }
            ClientCategoryStatistics client = transformClientList(list);
            total += client.getSale();
            num += client.getNum();
        }

        Map<Long,List<ClientCategoryStatistics>> map=new HashMap<>();
        for(ProductCategory category:productCategories){
            List<ClientCategoryStatistics> clientList = Lists.newArrayList();
            List<SaleCategoryStatistics> list = Lists.newArrayList();
            for(SaleCategoryStatistics statistics:saleCategoryStatistics){
                if(statistics.getCid()==category.getId()){
                    list.add(statistics);
                }
            }
            ClientCategoryStatistics client = transformClientList(list);
            client.setCName(category.getName());
            clientList.add(client);
            double rate = ArithUtils.div(client.getSale(),total,2);
            client.setRate(rate);

            Map<Long,List<OrderSku>> listMap = Maps.newHashMap();
            List<OrderSku> skuList = listMap.get(category.getId());
            int rate_0to100=0;
            int rate_100to500=0;
            int rate_500to1000=0;
            int rate_1000to2000=0;
            int rate_2000=0;
            for(OrderSku sku:skuList){
                if(sku.getLastSubtotal()>0&&sku.getLastSubtotal()<=100){
                    rate_0to100++;
                }
                if(sku.getLastSubtotal()>100&&sku.getLastSubtotal()<=500){
                    rate_100to500++;
                }
                if(sku.getLastSubtotal()>500&&sku.getLastSubtotal()<=1000){
                    rate_500to1000++;
                }
                if(sku.getLastSubtotal()>1000&&sku.getLastSubtotal()<=2000){
                    rate_1000to2000++;
                }
                if(sku.getLastSubtotal()>2000){
                    rate_2000++;
                }
            }
            client.setRate_0to100(ArithUtils.div(rate_0to100,num,2));
            client.setRate_100to500(ArithUtils.div(rate_100to500,num,2));
            client.setRate_500to1000(ArithUtils.div(rate_500to1000,num,2));
            client.setRate_1000to2000(ArithUtils.div(rate_1000to2000,num,2));
            client.setRate_2000(ArithUtils.div(rate_2000,num,2));
            map.put(category.getId(),clientList);
        }
        return map;
    }


    private ClientCategoryStatistics transformClientList(List<SaleCategoryStatistics> saleCategoryStatistics) throws Exception {
        ClientCategoryStatistics client = new ClientCategoryStatistics();
        double perPrice = 0;
        int num = 0;
        double sale = 0;
        for(SaleCategoryStatistics statistics:saleCategoryStatistics){
            num += statistics.getNum();
            sale = ArithUtils.add(sale, statistics.getSale());
            perPrice = ArithUtils.add(perPrice, statistics.getPerPrice());
        }
        client.setNum(num);
        client.setSale(sale);
        client.setPerPrice(perPrice);
        client.setStatistics(saleCategoryStatistics);
        return client;
    }

}
